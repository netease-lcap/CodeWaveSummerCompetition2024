package com.fdddf.emailfetcher;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmailFetcher implements Iterator<Message> {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final int batchSize = 200;

    private final EmailConfig config;
    private final String[] excludes;
    private final String[] includes;

    private Store mailbox;

    private List<String> keywords;

    private FolderIterator folderIter;
    private MessageIterator msgIter;

    private static final Logger logger = LoggerFactory.getLogger(EmailFetcher.class);

    public EmailFetcher(EmailConfig config, String[] includes, String[] excludes) {
        this.config = config;
        this.includes = includes;
        this.excludes = excludes;
    }

    public List<String> getFolders() throws EmailFetchException, MessagingException {
        List<String> retFolders = new ArrayList<>();
        Folder defaultFolder = mailbox.getDefaultFolder();

        // List all folders
        Folder[] folders = defaultFolder.list();
        for (Folder folder : folders) {
            System.out.println("Folder: " + folder.getName());
            retFolders.add(folder.getName());
        }
        return retFolders;
    }

    /**
     * Get inbox mails
     *
     * @param pageNumber page number, default 1
     * @param pageSize   page size, default 10
     * @return list of Email
     */
    public List<Email> getInboxMails(Integer pageNumber, Integer pageSize) {
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        try {
            Folder inbox = mailbox.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            int start = (pageNumber - 1) * pageSize;
            int end = start + pageSize;
            int count = inbox.getMessageCount();
            System.out.printf("total emails %d\n", count);
            if (end > count) {
                end = count;
            }

            List<Email> emails = new ArrayList<>();
            Message[] messages = inbox.getMessages(start + 1, end);

            StringBuilder sb = new StringBuilder();
            for (Message message : messages) {
                sb.setLength(0);
                getPartContent(message, sb);
                Date receivedDate = message.getReceivedDate();
                Date sentDate = message.getSentDate();
                Email email = new Email(message, inbox.getName());
                for (Address address : message.getFrom()) {
                    InternetAddress from = (InternetAddress) address;
                    email.from = from.getAddress();
                }
                Map<String, InputStream> attachmentsMap = new HashMap<>();
                saveAttachment(message, attachmentsMap);
                AmazonOSS obs = new AmazonOSS(this.config);
                email.attachments = obs.saveAttachmentToOSS(attachmentsMap);
                email.content = sb.toString();
                email.receivedDate = receivedDate != null ? FORMAT.format(receivedDate) : "";
                email.sentDate = sentDate != null ? FORMAT.format(sentDate) : "";
                email.folderName = inbox.getName();
                emails.add(email);
            }

            return emails;
        } catch (Exception e) {
            logger.error("Error while fetching emails", e);
            throw new RuntimeException(e);
        }
    }

    public boolean hasNext() {
        try {
            if (folderIter == null) {
                folderIter = new FolderIterator(mailbox, excludes, includes);
            }
            // get next message from the folder
            // if folder is exhausted get next folder
            // loop till a valid mail or all folders exhausted.
            while (msgIter == null || !msgIter.hasNext()) {
                Folder next = folderIter.hasNext() ? folderIter.next() : null;
                if (next == null) {
                    return false;
                }
                msgIter = new MessageIterator(next, batchSize, keywords);
            }
        } catch (EmailFetchException e) {
            logger.error("Fetching email failed", e);
            return false;
        }
        return true;
    }

    public void remove() {
        throw new UnsupportedOperationException("Its read only mode.");
    }

    public Message next() {
        return msgIter.next();
    }

    public String getFolder() {
        if (msgIter == null) {
            return null;
        }
        return msgIter.getFolder();
    }

    public void setFilterKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public boolean jumpToMessageId(String id) {
        while (hasNext()) {
            Message mail = next();

            try {
                if (mail instanceof IMAPMessage) {
                    if (((IMAPMessage) mail).getMessageID().equals(id)) {
                        return true;
                    }
                } else {
                    return false;
                }
            } catch (MessagingException e) {
                return false;
            }
        }
        return false;
    }

    public boolean jumpToFolder(String folderName) {
        FolderIterator newFolderIter = null;
        MessageIterator newMsgIter = null;
        try {
            newFolderIter = new FolderIterator(mailbox, excludes, includes);

            while (newFolderIter.hasNext()) {
                Folder next = newFolderIter.next();
                if (folderName.equals(next.getFullName())) {
                    newMsgIter = new MessageIterator(next, batchSize, keywords);
                    folderIter = newFolderIter;
                    msgIter = newMsgIter;
                    return true;
                }
            }

            return false;
        } catch (EmailFetchException e) {
            logger.error("Fetching email failed", e);
            return false;
        }
    }

    public void getPartContent(Part part, StringBuilder sb) throws Exception {
        if (part.isMimeType("text/*")) {
            String s = (String) part.getContent();
            if (s != null) {
                sb.append(s).append(" ");
            }
        } else if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            int count = mp.getCount();
            if (part.isMimeType("multipart/alternative")) {
                count = 1;
            }

            for (int i = 0; i < count; i++) {
                getPartContent(mp.getBodyPart(i), sb);
            }
        } else if (part.isMimeType("message/rfc822")) {
            getPartContent((Part) part.getContent(), sb);
        }
    }

    public Map<String, InputStream> saveAttachment(Part part, Map<String, InputStream> attachmentsMap) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getInputStream();
                    String fileName = MimeUtility.decodeText(bodyPart.getFileName());
                    attachmentsMap.put(fileName, is);
                } else if (bodyPart.isMimeType("multipart/*")) {
                    // Recursively call saveAttachment if there's nested multipart
                    attachmentsMap.putAll(saveAttachment(bodyPart, attachmentsMap));
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("name") || contentType.contains("application")) {
                        InputStream is = bodyPart.getInputStream();
                        String fileName = MimeUtility.decodeText(bodyPart.getFileName());
                        attachmentsMap.put(fileName, is);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            // Recursively call saveAttachment if it's a nested message
            attachmentsMap.putAll(saveAttachment((Part) part.getContent(), attachmentsMap));
        }

        return attachmentsMap;
    }

    public static Session getSession(EmailConfig emailConfig) {
        EmailServerProps props = new EmailServerProps(
                emailConfig.protocol, emailConfig.host, emailConfig.port, emailConfig.sslEnable);
        Properties properties = props.getProperties();
        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(emailConfig.username, emailConfig.password);
            }
        });
    }

    public Boolean connectToMailBox() {
        try {
            Session session = getSession(config);
            mailbox = session.getStore();
            mailbox.connect(config.username, config.password);
            logger.info("Connected to mailbox");
            if (config.protocol.equals("imap") && (
                    config.host.contains("163.com") || config.host.contains("126.com"))
            ) {
                HashMap<String, String> IAM = new HashMap<>();
                IAM.put("name", config.username);
                IAM.put("version", "1.0.0");
                IAM.put("vendor", "emailfetchor");
                IAM.put("support-email", config.username);
                ((IMAPStore) mailbox).id(IAM);
            }
        } catch (AuthenticationFailedException e) {
            handleAuthenticationFailure(e);
        } catch (MessagingException e) {
            logger.error("Error while connecting to mailbox", e);
            return false;
        }
        return true;
    }

    public void disconnectFromMailBox() {
        folderIter = null;
        msgIter = null;
        try {
            mailbox.close();
            logger.info("Disconnected from mailbox");
        } catch (MessagingException e) {
            logger.error("Connection failed", e);
        }
    }

    private void handleAuthenticationFailure(Exception e) {
        logger.error("authentication failed {}", decode(e.getLocalizedMessage(), "gbk"));
        throw new EmailRuntimeException(e);
    }

    public static String decode(String message, String charset) {
        try {
            return new String(message.getBytes(StandardCharsets.ISO_8859_1), charset);
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    public String getPriority(MimeMessage msg) throws MessagingException {
        String priority = "normal";
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.contains("1") || headerPriority.contains("High"))
                priority = "high";
            else if (headerPriority.contains("5") || headerPriority.contains("Low"))
                priority = "low";
            else
                priority = "normal";
        }
        return priority;
    }
}

package com.fdddf.emailfetcher;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
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
    private FolderIterator folderIter;
    private MessageIterator msgIter;

    private static final Logger log = LoggerFactory.getLogger(EmailFetcher.class);

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
     * @param pageNumber page number, default 1
     * @param pageSize page size, default 10
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
            if (end > inbox.getMessageCount()) {
                end = inbox.getMessageCount();
            }

            List<Email> emails = new ArrayList<>();
            Message[] messages = inbox.getMessages(start + 1, end);

            StringBuilder sb = new StringBuilder();
            for (Message message : messages) {
                sb.setLength(0);
                getPartContent(message, sb);
                Date receivedDate = message.getReceivedDate();
                Email email = new Email(message, inbox.getName());
                for (Address address : message.getFrom()) {
                    InternetAddress from = (InternetAddress) address;
                    email.from = from.getAddress();
                }
                email.content = sb.toString();
                email.receivedDate = FORMAT.format(receivedDate);
                emails.add(email);
            }

            return emails;
        } catch (Exception e) {
            log.error("Error while fetching emails", e);
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
                msgIter = new MessageIterator(next, batchSize);
            }
        } catch (EmailFetchException e) {
            log.error("Fetching email failed", e);
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

    public boolean jumpToMessageId(String id) {
        while (hasNext()) {
            Message mail = next();

            try {
                if (mail instanceof IMAPMessage) {
                    if (((IMAPMessage)mail).getMessageID().equals(id)) {
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
                    newMsgIter = new MessageIterator(next, batchSize);
                    folderIter = newFolderIter;
                    msgIter = newMsgIter;
                    return true;
                }
            }

            return false;
        } catch (EmailFetchException e) {
            log.error("Fetching email failed", e);
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

    public Boolean connectToMailBox() {
        try {
            EmailServerProps props = new EmailServerProps(config.protocol, config.host, config.port, config.sslEnable);
            Properties properties = props.getProperties();
            Session session = Session.getDefaultInstance(properties);
            mailbox = session.getStore();
            mailbox.connect(config.username, config.password);
            log.info("Connected to mailbox");
            if (config.protocol.equals("imap")) {
                HashMap<String, String> IAM = new HashMap<>();
                IAM.put("name", config.username);
                IAM.put("version", "1.0.0");
                IAM.put("vendor", "emailfetchor");
                IAM.put("support-email", config.username);
                ((IMAPStore)mailbox).id(IAM);
            }
        } catch (AuthenticationFailedException e) {
            handleAuthenticationFailure(e);
        }catch (MessagingException e) {
            log.error("Error while connecting to mailbox", e);
            return false;
        }
        return true;
    }

    public void disconnectFromMailBox() {
        folderIter = null;
        msgIter = null;
        try {
            mailbox.close();
            log.info("Disconnected from mailbox");
        } catch (MessagingException e) {
            log.error("Connection failed", e);
        }
    }

    private void handleAuthenticationFailure(Exception e) {
        log.error("authentication failed {}", decode(e.getLocalizedMessage(), "gbk"));
        throw new RuntimeException(e);
    }

    public static String decode(String message, String charset) {
        try {
            return new String(message.getBytes(StandardCharsets.ISO_8859_1), charset);
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }
}

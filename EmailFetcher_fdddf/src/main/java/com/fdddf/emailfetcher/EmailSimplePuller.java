package com.fdddf.emailfetcher;

import com.sun.mail.imap.IMAPStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Folder;

public class EmailSimplePuller {

    private static final Logger logger = LoggerFactory.getLogger(EmailFetcher.class);
    private Store mailbox;
    private final EmailConfig config;

    public EmailSimplePuller(EmailConfig config) {
        this.config = config;

    }

    public Boolean connectToMailBox() {
        try {
            EmailServerProps props = new EmailServerProps(config.protocol, config.host, config.port, config.sslEnable);
            Properties properties = props.getProperties();
            Session session = Session.getDefaultInstance(properties);
            mailbox = session.getStore();
            mailbox.connect(config.username, config.password);
            logger.info("Connected to mailbox");
            if (config.protocol.equals("imap")) {
                HashMap<String, String> IAM = new HashMap<>();
                IAM.put("name", config.username);
                IAM.put("version", "1.0.0");
                IAM.put("vendor", "emailfetchor");
                IAM.put("support-email", config.username);
                ((IMAPStore)mailbox).id(IAM);
            }
        } catch (AuthenticationFailedException e) {
            logger.error("authentication failed {}", decode(e.getLocalizedMessage(), "gbk"));
            throw new RuntimeException(e);
        }catch (MessagingException e) {
            logger.error("Error while connecting to mailbox", e);
            return false;
        }
        return true;
    }

    public Folder[] getFolders(Folder parentFolder) throws EmailFetchException, MessagingException {
        if (parentFolder == null) {
            parentFolder = mailbox.getDefaultFolder();
        }

        Folder[] folders = parentFolder.list();
        for (Folder folder : folders) {
            logger.info("Folder: {}", folder.getName());

            if (folder.getType() == Folder.HOLDS_FOLDERS) {
                return getFolders(folder);
            }
        }
        return folders;
    }

    public static String decode(String message, String charset) {
        try {
            return new String(message.getBytes(StandardCharsets.ISO_8859_1), charset);
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    public void disconnectFromMailBox() {
        try {
            mailbox.close();
            logger.info("Disconnected from mailbox");
        } catch (MessagingException e) {
            logger.error("Connection failed", e);
        }
    }
}

package com.fdddf.emailfetcher.api;

import com.fdddf.emailfetcher.*;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.sun.mail.imap.IMAPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class EmailExtractor {

    /**
     * EmailConfig
     */
    @Resource
    private EmailConfig cfg;
    private static final Logger log = LoggerFactory.getLogger(EmailExtractor.class);
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public void setCfg(EmailConfig cfg) {
        this.cfg = cfg;
    }

    /**
     * Get all folders
     * @return List of folders
     */
    @NaslLogic
    public List<String> getFolders()  {
        String[] includes = new String[]{"INBOX"};
        EmailFetcher fetcher = new EmailFetcher(cfg, includes, null);
        try {
            if (!fetcher.connectToMailBox()) {
                log.error("Can't connect to mailbox");
                return null;
            }

            List <String> folders = fetcher.getFolders();
            fetcher.disconnectFromMailBox();

            return folders;
        } catch (EmailFetchException e) {
            log.error("Can't connect to mailbox");
            return null;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all emails
     * @param pageNumber Page number
     * @param pageSize Page size
     * @return List of emails
     */
    @NaslLogic
    public List<Email> getInboxEmails(Integer pageNumber, Integer pageSize) {
        try {
            EmailFetcher fetcher = new EmailFetcher(cfg, null, null);
            if (!fetcher.connectToMailBox()) {
                log.error("Can't connect to mailbox");
                return null;
            }
            List<Email> emails = fetcher.getInboxMails(pageNumber, pageSize);
            fetcher.disconnectFromMailBox();
            return emails;
        } catch (Exception e) {
            log.error("Can't connect to mailbox or fetch emails %s", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Extract emails
     * @param includes List of folders to include
     * @param excludes List of folders to exclude
     * @param keywords Keywords to filter emails
     * @return List of emails
     */
    @NaslLogic
    public List<Email> extractEmails(List<String> includes, List<String> excludes, List<String> keywords) {
        String[] incs = includes.toArray(new String[0]);
        String[] excs = excludes.toArray(new String[0]);
        EmailFetcher fetcher = new EmailFetcher(cfg, incs, excs);

        fetcher.setFilterKeywords(keywords);
        if (!fetcher.connectToMailBox()) {
            log.error("Can't connect to mailbox");
            return null;
        }

        int restartCount = 0;
        String lastFolder = "";
        String lastSuccessMsgId = null;

        List<Email> emails = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        while (fetcher.hasNext()) {
            Message mail = fetcher.next();
            if (!lastFolder.equals(fetcher.getFolder())) {
                lastFolder = fetcher.getFolder();
                restartCount = 0;
            }

            try {
                sb.setLength(0);
                fetcher.getPartContent(mail, sb);

                Date receivedDate = mail.getReceivedDate();
                Date sentDate = mail.getSentDate();
                Email email = new Email(mail, fetcher.getFolder());

                for (Address address : mail.getFrom()) {
                    InternetAddress from = (InternetAddress) address;
                    email.from = from.getAddress();
                    System.out.format("from %s %s at %s \n", from.getAddress(), fetcher.getFolder(), FORMAT.format(sentDate));
                }
                email.content = sb.toString();
                email.receivedDate = receivedDate != null ? FORMAT.format(receivedDate) : "";
                email.sentDate = sentDate != null ? FORMAT.format(sentDate) : "";
                emails.add(email);
                if (mail instanceof IMAPMessage) {
                    lastSuccessMsgId = ((IMAPMessage)mail).getMessageID();
                }
            } catch (Exception e) {
                log.error("Can't read content from email", e);

                restartCount++;
                // restart (connect/disconnect) and continue from current folder
                if (restartCount <= 3) {
                    String curFolder = fetcher.getFolder();
                    log.info("Restart at folder {} time {}", curFolder, restartCount);
                    fetcher.disconnectFromMailBox();
                    if (!fetcher.connectToMailBox() || !fetcher.jumpToFolder(curFolder)) {
                        log.info("Jump to folder {} failed. Skip the failed email and continue", curFolder);
                    }
                    if (lastSuccessMsgId != null) {
                        if (fetcher.jumpToMessageId(lastSuccessMsgId)) {
                            log.info("Jump to last failed mail");
                        } else {
                            log.info("Can't jump to last failed mail");
                        }
                    }
                } else {
                    log.info("Skip the failed email and continue");
                }
            }
        }

        fetcher.disconnectFromMailBox();
        return emails;
    }

}

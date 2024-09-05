package com.fdddf.emailfetcher;

import com.netease.lowcode.core.annotation.NaslStructure;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.util.ArrayList;
import java.util.List;

@NaslStructure
public class Email {
    /**
     * 邮件发送人
     */
    public String from;
    /**
     * 邮件主题
     */
    public String subject;
    /**
     * 邮件接收时间
     */
    public String receivedDate;

    /**
     * 邮件发送时间
     */
    public String sentDate;
    /**
     * 邮件内容
     */
    public String content;

    /**
     * 邮件接收人TO
     */
    public List<String> recipientTo;
    /**
     * 邮件接收人CC
     */
    public List<String> recipientCc;
    /**
     * 邮件接收人BCC
     */
    public List<String> recipientBcc;

    /**
     * 邮件附件 OSS存储的URL
     */
    public List<String> attachments;

    public String folderName;

    /**
     * 邮件优先级, high, normal, low
     */
    public String priority;

    public Email() {
        recipientTo = new ArrayList<>();
        recipientCc = new ArrayList<>();
        recipientBcc = new ArrayList<>();
        attachments = new ArrayList<>();
    }

    private static String decode(String recipient) {
        try {
            return MimeUtility.decodeText(recipient);
        } catch (Exception e) {
            return recipient;
        }
    }

    public Email(Message message, String folderName) throws Exception {
        receivedDate = String.valueOf(message.getReceivedDate());
        subject = message.getSubject();
        content = EmailFetcher.decode(message.getContent().toString(), "UTF-8");
        from = ((InternetAddress) message.getFrom()[0]).getAddress();
        recipientTo = new ArrayList<>();
        recipientCc = new ArrayList<>();
        recipientBcc = new ArrayList<>();
        attachments = new ArrayList<>();

        if (message.getRecipients(Message.RecipientType.TO) != null) {
            Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);
            for (Address address : toAddresses) {
                recipientTo.add(decode(address.toString()));
            }
        }
        if (message.getRecipients(Message.RecipientType.CC) != null) {
            Address[] ccAddresses = message.getRecipients(Message.RecipientType.CC);
            for (Address address : ccAddresses) {
                recipientCc.add(decode(address.toString()));
            }
        }
        if (message.getRecipients(Message.RecipientType.BCC) != null) {
            Address[] bccAddresses = message.getRecipients(Message.RecipientType.BCC);
            for (Address address : bccAddresses) {
                recipientBcc.add(decode(address.toString()));
            }
        }
        this.folderName = folderName;
    }

    @Override
    public String toString() {
        return "From: " + from + "\n" +
                "Subject: " + subject + "\n" +
                "Sent Date: " + sentDate + "\n" +
                "Priority: " + priority + "\n" +
                "Recipient To: " + recipientTo + "\n" +
                "Recipient Cc: " + recipientCc + "\n" +
                "Recipient Bcc: " + recipientBcc + "\n" +
                "Received Date: " + receivedDate + "\n" +
                "Folder: " + folderName + "\n";
//                "Content: " + content;
    }
}
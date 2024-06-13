package com.yu.connector;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/11 14:44
 **/

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.netease.lowcode.core.annotation.NaslConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@NaslConnector(connectorKind = "qq-email-connector")
public class QQEmailConnector {
    public static final String EMAIL_PATTERN = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static final Logger log = LoggerFactory.getLogger(QQEmailConnector.class);
    /**
     * 你的qq账号
     */
    private String account;
    /**
     * 你授权给第三方的授权码
     */
    private String authCode;
    private String host = "smtp.qq.com";
    private Session session;

    /**
     * 对中文字符进行UTF-8编码
     *
     * @param source 要转义的字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String tranformStyle(String source) throws UnsupportedEncodingException {
        char[] arr = source.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            char temp = arr[i];
            if (isChinese(temp)) {
                sb.append(URLEncoder.encode("" + temp, "UTF-8"));
                continue;
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * 判断是不是中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        String account = "your_qq_email@qq.com";
        String authCode = "your_auth_code";
        QQEmailConnector emailConnector = new QQEmailConnector().init(account, authCode);
        System.out.println(emailConnector.connectTest(account, authCode));
        emailConnector.sendEmailWithFile("recipient@qq.com", "Subject", "Email body", null, new ArrayList<String>() {{
            add("https://example.com/image1.jpg");
            add("https://example.com/image2.jpg");
        }});
        emailConnector.sendEmail("recipient@qq.com", "Subject", "Email body", null);
        for (MailBody mailBody : emailConnector.getEmail(5)) {
            System.out.println(mailBody);
        }
    }

    @NaslConnector.Creator
    public QQEmailConnector init(String account, String authCode) {
        this.account = account;
        this.authCode = authCode;
        this.session = createSession(account, authCode);
        return this;
    }

    @NaslConnector.Tester
    public Boolean connectTest(String account, String authCode) {
        init(account, authCode);
        try {
            //连接邮箱服务器
            Store store = this.session.getStore("imap");
            store.connect(host, account, authCode);
            store.close();
            return true;
        } catch (AuthenticationFailedException e) {
            log.error("密码校验失败：", e);
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            log.error("服务器连接失败：", e);
            throw new RuntimeException(e);
        }
    }

    private Session createSession(String account, String authCode) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.host);
        properties.put("mail.smtp.port", 25);
        properties.put("mail.smtp.auth", true);
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, authCode);
            }
        });
    }

    private boolean isEmpty(String cs) {
        return cs == null || cs.trim().length() == 0;
    }

    /**
     * 收取邮件，支持获取最新的指定数量的邮件
     *
     * @param needMsgCount 领取的邮件数量数量越大，拉取越慢
     * @return
     */
    @NaslConnector.Logic
    public List<MailBody> getEmail(Integer needMsgCount) {
        List<MailBody> list = new ArrayList<>();
        try {
            Store store = this.session.getStore("imap");
            store.connect(host, account, authCode);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            int totalMsgCount = inbox.getMessageCount();
            if (needMsgCount > totalMsgCount) {
                log.error("大于总邮件数量，请重新输入");
                throw new IllegalArgumentException("大于总邮件数量" + totalMsgCount + "，请重新输入");
            }
            int[] msgNums = new int[needMsgCount];
            for (int i = 0; i < msgNums.length; i++) {
                msgNums[i] = totalMsgCount - i;
            }
            Message[] messages = inbox.getMessages(msgNums);
            for (Message message : messages) {
                MailBody mailBody = new MailBody();
                mailBody.setSubject(message.getSubject());
                mailBody.setFrom(convertAddress(message.getFrom()));
                mailBody.setTo(convertAddress(message.getRecipients(Message.RecipientType.TO)));
                mailBody.setCc(convertAddress(message.getRecipients(Message.RecipientType.CC)));
                if (message.getSentDate() != null)
                    mailBody.setSendDate(message.getSentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                Object content = message.getContent();
                if (content instanceof MimeMultipart) mailBody.setContent("此为带附件的邮件，请移步邮箱内查看~");
                else mailBody.setContent(content.toString());
                list.add(mailBody);
            }
            return list;
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertAddress(Address[] address) {
        if (address == null) return null;
        return Arrays.stream(address).map(e -> {
            InternetAddress internetAddress = (InternetAddress) e;
            return internetAddress.getAddress();
        }).collect(Collectors.joining(","));
    }

    /**
     * 发送邮件
     *
     * @param receiveAccount 接收方账号 必填
     * @param subject        主题 必填
     * @param content        内容 必填
     * @param ccRecipients   抄送人 非必填
     */
    @NaslConnector.Logic
    public Boolean sendEmail(String receiveAccount, String subject, String content, List<String> ccRecipients) throws IllegalArgumentException {
        try {
            MimeMessage email = getMimeMessage(receiveAccount, subject, content, ccRecipients);
            //邮件内容
            email.setText(content);
            //发送
            Transport.send(email);
            return true;
        } catch (MessagingException e) {
            log.error("邮件发送失败：", e);
            throw new RuntimeException(e);
        }

    }

    public MimeMessage getMimeMessage(String receiveAccount, String subject, String content, List<String> ccRecipients) throws MessagingException {
        if (isEmpty(receiveAccount)) throw new IllegalArgumentException("接受者账号不能为空");
        if (isEmpty(subject)) throw new IllegalArgumentException("邮件主题不能为空");
        if (isEmpty(content)) throw new IllegalArgumentException("邮件内容不能为空");
        if (!receiveAccount.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("接受者账号校验不合法");
        }
        MimeMessage email = new MimeMessage(session);
        if (ccRecipients != null && ccRecipients.size() > 0) {
            for (String ccRecipient : ccRecipients) {
                if (!ccRecipient.matches(EMAIL_PATTERN))
                    throw new IllegalArgumentException("抄送人：" + ccRecipient + "账号校验不合法");
                else email.addRecipients(Message.RecipientType.CC, ccRecipient);
            }
        }
        //填写发送者的邮箱
        email.setFrom(new InternetAddress(account));
        //填写邮件标题
        email.setSubject(subject);
        //填写接收者的邮箱
        email.addRecipients(Message.RecipientType.TO, receiveAccount);
        return email;
    }

    /**
     * 发送带附件的邮件
     *
     * @param receiveAccount 接收方账号 必填
     * @param subject        主题 必填
     * @param content        内容 必填
     * @param ccRecipients   抄送人 非必填
     * @param files          附件路径 大于0小于10
     * @return
     * @throws IllegalArgumentException
     */
    @NaslConnector.Logic
    public Boolean sendEmailWithFile(String receiveAccount, String subject, String content, List<String> ccRecipients, List<String> files) throws IllegalArgumentException {
        if (files == null || files.size() == 0) throw new IllegalArgumentException("请至少传入一个文件路径");
        if (files.size() > 10) throw new IllegalArgumentException("最多同时提交10个附件");
        try {
            MimeMessage email = getMimeMessage(receiveAccount, subject, content, ccRecipients);
            //	邮件主体
            BodyPart textPart = new MimeBodyPart();
            textPart.setContent(content, "text/html;charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            for (String filePath : files) {
                String encodedUrl = tranformStyle(filePath);
                URL url = new URL(encodedUrl);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                String name = FileUtil.getName(filePath);
                //	邮件附件
                BodyPart filePart = new MimeBodyPart();
                //解决附件中文名乱码
                filePart.setFileName(MimeUtility.encodeText(name));
                byte[] fileBytes = IoUtil.readBytes(inputStream);
                //	提交附件文件
                filePart.setDataHandler(new DataHandler(new ByteArrayDataSource(fileBytes, "application/octet-stream")));
                multipart.addBodyPart(filePart);
            }
            //	将邮件装入信封
            email.setContent(multipart);
            //发送
            Transport.send(email);
            return true;
        } catch (MessagingException | MalformedURLException e) {
            log.error("邮件发送失败：", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

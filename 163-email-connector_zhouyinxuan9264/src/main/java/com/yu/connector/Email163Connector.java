package com.yu.connector;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/11 14:44
 **/

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.netease.lowcode.core.annotation.NaslConnector;
import com.sun.mail.imap.IMAPStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@NaslConnector(connectorKind = "email-163-connector")
public class Email163Connector {
    public static final String EMAIL_PATTERN = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");
    /**
     * 你的163邮箱账号 必填 支持个人邮箱和企业邮箱
     */
    private String account;
    /**
     * 你授权给第三方的授权码
     */
    private String authCode;
    /**
     * 你的邮箱的smtp服务器地址 如果是@163.com不需要填写，其他的可以进入网站 https://qiye.163.com/help/client-profile.html 查询后填写
     */
    private String smtpHost = "smtp.163.com";
    private String imapHost = "imap.163.com";
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

    public static void main(String[] args) throws MessagingException {

        String account = "your_email@163.com";
        String authCode = "your_auth_code";
         String smtpHost = "smtphz.qiye.163.com";
         String imapHost = "imaphz.qiye.163.com";


        Email163Connector emailConnector = new Email163Connector().init(account, authCode, smtpHost, smtpHost);

        Boolean isConnect = emailConnector.connectTest(account, authCode, smtpHost, imapHost);
        System.out.println("是否连接成功：" + isConnect);
        if (isConnect) {
            Boolean isSuccess = emailConnector.sendEmail("recipient@example.com", "Subject", "This is the email body sent via low code", null);
            Boolean isSuccess2 = emailConnector.sendEmailWithFile("recipient@example.com","Subject", "This is the email body sent via low code", null, new ArrayList<String>() {{
                add("https://example.com/image1.jpg");
                add("https://example.com/image2.jpg");
            }});
            //System.out.println("是否发送成功：" + isSuccess);
            for (MailBody mailBody : emailConnector.getEmail(3)) {
                System.out.println(mailBody);
            }
        }
    }

    /**
     * @param account  你的163邮箱账号 必填 支持个人邮箱和企业邮箱
     * @param authCode 你授权给第三方的授权码
     * @param smtpHost 你的邮箱的smtp服务器地址 如果是@163.com不需要填写，其他的可以进入网站 https://qiye.163.com/help/client-profile.html 查询后填写
     * @return
     */
    @NaslConnector.Creator
    public Email163Connector init(String account, String authCode, String smtpHost, String imapHost) {
        this.account = account;
        this.authCode = authCode;
        if (!isEmpty(smtpHost)) this.smtpHost = smtpHost;
        if (!isEmpty(smtpHost)) this.imapHost = imapHost;
        this.session = createSession(account, authCode);
        return this;
    }

    /**
     * 收取邮件，支持获取
     *
     * @param needMsgCount 领取的邮件数量数量越大，拉取越慢
     * @return
     */
    @NaslConnector.Logic
    public List<MailBody> getEmail(Integer needMsgCount) {
        List<MailBody> list = new ArrayList<>();
        try {
            IMAPStore store = (IMAPStore) this.session.getStore("imap");
            HashMap<String, String> clientParams = new HashMap<>();
            //带上IMAP ID信息，由key和value组成，例如name，version，vendor，support-email等。
            // 这个value的值随便写就行，否则报错会报一个如下的异常：A3 NO SELECT Unsafe Login. Please contact kefu@188.com for help异常处理
            clientParams.put("name", "myname");
            clientParams.put("version", "2.0.0");
            clientParams.put("vendor", "myclient");
            clientParams.put("support-email", "testmail@test.com");
            store.connect(null, account, authCode);
            if (account.endsWith("@163.com")) store.id(clientParams);
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
     * @param account  你的163邮箱账号 必填 支持个人邮箱和企业邮箱
     * @param authCode 你授权给第三方的授权码
     * @param smtpHost 你的邮箱的smtp服务器地址 如果是@163.com不需要填写，其他的可以进入网站 https://qiye.163.com/help/client-profile.html 查询后填写
     * @return
     */
    @NaslConnector.Tester
    public Boolean connectTest(String account, String authCode, String smtpHost, String imapHost) {
        init(account, authCode, smtpHost, imapHost);
        //开发情况开启调试
        //session.setDebug(true);
        try {
            Store store = session.getStore("imap");
            store.connect(null, account, authCode);
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

    /**
     * 163邮箱不支持明文连接，所以这里要使用tls病指定协议版本，不然可能会被java.security禁用
     *
     * @param account
     * @param authCode
     * @return
     */
    private Session createSession(String account, String authCode) {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", 25);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.required", true);
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        properties.put("mail.imap.host", imapHost);
        properties.put("mail.imap.port", 143);
        properties.put("mail.imap.auth", true);
        properties.put("mail.imap.starttls.required", true);
        properties.put("mail.imap.ssl.protocols", "TLSv1.2");
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
                filePart.setFileName(name);
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
        } catch (FileNotFoundException e) {
            log.error("附件未找到：", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

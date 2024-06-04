package com.yu.connector;

import com.netease.lowcode.core.annotation.NaslStructure;

import java.time.LocalDate;

/**
 * @author 余卫青
 * @version 1.0.0
 * @date 2024/4/15 20:37
 **/
@NaslStructure
public class MailBody {
    public String subject;
    public String from;
    public String to;
    public String cc;
    public LocalDate sendDate;
    public String content;

    @Override
    public String toString() {
        return "MailBody{" +
                "subject='" + subject + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", cc='" + cc + '\'' +
                ", sendDate=" + sendDate +
                ", content='" + content + '\'' +
                '}';
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDate getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDate sendDate) {
        this.sendDate = sendDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

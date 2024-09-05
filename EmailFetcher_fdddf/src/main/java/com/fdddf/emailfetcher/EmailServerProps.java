package com.fdddf.emailfetcher;

import java.util.Properties;

public class EmailServerProps {
    private final String protocol;
    private final String host;
    private final String port;
    private final boolean sslEnable;

    public EmailServerProps(String protocol, String host, String port, boolean sslEnable) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.sslEnable = sslEnable;
    }

    public Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.store.protocol", protocol);
        props.put("mail." + protocol + ".host", host);
        props.put("mail." + protocol + ".port", port);
        props.put("mail." + protocol + ".ssl.protocols", "TLSv1.2");
        props.put("mail." + protocol + ".ssl.enable", String.valueOf(sslEnable));

        props.put("mail.imap.version", "1.0.0");
        props.put("mail.imap.vendor", "fdddf-email-fetchor");
        props.put("mail.imap.support-email", "futuai@163.com");
        return props;
    }
}

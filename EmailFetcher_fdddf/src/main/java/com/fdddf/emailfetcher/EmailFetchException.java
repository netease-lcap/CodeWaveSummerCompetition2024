package com.fdddf.emailfetcher;

public class EmailFetchException extends Exception {
    private static final long serialVersionUID = 1L;

    public EmailFetchException(String message, Throwable e) {
        super(message, e);
    }
}

package com.fdddf.shorturl;

public class LinkDuplicateException extends RuntimeException {
    public LinkDuplicateException(String message) {
        super(message);
    }
    public LinkDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
    public LinkDuplicateException(Throwable cause) {
        super(cause);
    }
}

package com.fdddf.shorturl;

public class LinkRuntimeException extends RuntimeException {
    
    public LinkRuntimeException(String message) {
        super(message);
    }
    public LinkRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    public LinkRuntimeException(Throwable cause) {
        super(cause);
    }
    
}

package com.fdddf.emailfetcher;

import java.lang.RuntimeException;

/**
 * EmailRuntimeException
 */
public class EmailRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailRuntimeException(Throwable cause)
    {        
        super(cause);
    }

    public EmailRuntimeException(String message)
    {        
        super(message);
    }
}
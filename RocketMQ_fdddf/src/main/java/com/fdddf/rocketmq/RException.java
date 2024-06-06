package com.fdddf.rocketmq;

public class RException extends RuntimeException {
    public RException(String message)
    {
        super(message);
    }
    public RException(String message, Throwable cause)
    {
        super(message, cause);
    }
    public RException(Throwable cause)
    {
        super(cause);
    }
}

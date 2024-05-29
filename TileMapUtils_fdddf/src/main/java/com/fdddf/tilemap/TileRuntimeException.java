package com.fdddf.tilemap;

public class TileRuntimeException extends RuntimeException {
    public TileRuntimeException(String message) {
        super(message);
    }
    public TileRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    public TileRuntimeException(Throwable cause) {
        super(cause);
    }
}

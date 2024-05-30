package com.fdddf.tilemap;

public enum ErrorCode {
    SUCCESS(0, "success"),
    INVALID_URL(1, "invalid url"),
    INVALID_TILE_SIZE(2, "invalid tile size"),
    INVALID_OUTPUT_DIRECTORY(3, "invalid output directory, " +
            "it should starts with file:// or just a folder name for OSS usage"),
    INVALID_IMAGE_TYPE(4, "image type is not supported, only support png and jpg"),
    INVALID_IMAGE_SIZE(5, "invalid image size"),
    INVALID_MEMORY_LIMIT(6, "no enough memory to process image"),
    ;

    final long code;
    final String message;

    ErrorCode(final int i, String message) {
        this.code = i;
        this.message = message;
    }
}
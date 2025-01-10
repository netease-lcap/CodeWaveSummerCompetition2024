package com.netease.lowcode.pdf.extension.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Base64Util {
    private final static Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    public static String convertImageToBase64String(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            log.error("convertImageToBase64String error", e);
            return null;
        }
    }
}

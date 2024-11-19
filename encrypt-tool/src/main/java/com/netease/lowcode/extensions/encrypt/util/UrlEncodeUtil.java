package com.netease.lowcode.extensions.encrypt.util;

import com.netease.lowcode.core.annotation.NaslLogic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlEncodeUtil {
    @NaslLogic
    public static String encryptWithUrlEncode(String source) {
        try {
            return URLEncoder.encode(source, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NaslLogic
    public static String encryptWithUrlEncodeByEnc(String source, String enc) {
        try {
            return URLEncoder.encode(source, enc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @NaslLogic
    public static String decryptWithUrlDecode(String source) {
        try {
            return URLDecoder.decode(source, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NaslLogic
    public static String decryptWithUrlDecodeByEnc(String source, String enc)  {
        try {
            return URLDecoder.decode(source, enc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

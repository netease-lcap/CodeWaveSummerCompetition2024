package com.moocsk.lowcode.tencent.message.util;

public class StringUtil {

    public static int getByteLength(String str) {
        if (str == null) {
            return 0;
        }
        byte[] bytes = str.getBytes();
        return bytes.length;
    }

}

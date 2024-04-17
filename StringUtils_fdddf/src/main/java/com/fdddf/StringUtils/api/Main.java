package com.fdddf.StringUtils.api;

public class Main {
    public static void main(String[] args) {
        String str = StringUtils.generateRandomString(10);
        System.out.println(str);
        str = StringUtils.generateRandomStringCustom(10, "+-x$%&thisok139839222mg");
        System.out.println(str);
        str = StringUtils.generateNonConsecutiveRandomString(10);
        System.out.println(str);
        str = StringUtils.subStringAfter("aa-bb", "a");
        System.out.println(str);
        str = StringUtils.subStringBefore("aa-bb", "b");
        System.out.println(str);
        str = StringUtils.stripHtml("<html><body><p>Hello World</p></body></html>");
        System.out.println(str);
        System.out.println(StringUtils.isNumber("123") ? "Yes" : "No");
        str = StringUtils.reverseString("Hello World");
        System.out.println(str);
    }
}

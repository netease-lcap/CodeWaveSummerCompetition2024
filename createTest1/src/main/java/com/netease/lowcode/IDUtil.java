package com.netease.lowcode;

import com.netease.lowcode.core.annotation.NaslLogic;
import java.security.SecureRandom;
public class IDUtil {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static SecureRandom random = new SecureRandom();

    /**
     *
     * @param length 生成id长度
     * @param includeNumbers 是否包含数字
     * @param includeLetters 是否包含字母
     * @return
     */
    @NaslLogic
    public static String getRandomId(Integer idLength, Boolean includeNumbers, Boolean includeLetters) {
        //都为false为不正常传参
    if(!includeNumbers && !includeLetters){
        return "数字或字母至少一个为true才可生成";
    }
        StringBuilder result = new StringBuilder(idLength);
            String source = (includeNumbers ? NUMBER : "") + (includeLetters ? CHAR_LOWER + CHAR_UPPER : "");
            for (int i = 0; i < idLength; i++) {
                int randomCharIndex = random.nextInt(source.length());
                result.append(source.charAt(randomCharIndex));
            }
        return result.toString();
    }

    public static void main(String[] args) {
        String randomId = getRandomId(32,false,false);
        System.out.println(randomId);

    }
}

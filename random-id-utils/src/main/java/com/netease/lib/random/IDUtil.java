package com.netease.lib.random;

import com.netease.lowcode.core.annotation.NaslLogic;
import java.security.SecureRandom;
public class IDUtil {
    //小写字母
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    //大写字母
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static SecureRandom random = new SecureRandom();

    /**
     *  getRandomId方法包含三个入参,根据入参生成输入长度的数字与大小写字母组合的随机字符串
     * @param idLength 生成id长度
     * @param includeNumbers 是否包含数字
     * @param includeLetters 是否包含字母
     * @return 返回的随机字符串
     */
    @NaslLogic
    public static String getRandomId(Integer idLength, Boolean includeNumbers, Boolean includeLetters) {
        //都为false为不正常传参
    if(!includeNumbers && !includeLetters){
        return "数字或字母至少一个为true才可生成";
    }
//        StringBuilder result = new StringBuilder(idLength);
            String source = (includeNumbers ? NUMBER : "") + (includeLetters ? CHAR_LOWER + CHAR_UPPER : "");
//            for (int i = 0; i < idLength; i++) {
//                int randomCharIndex = random.nextInt(source.length());
//                result.append(source.charAt(randomCharIndex));
//            }
//        return result.toString();

        return random.ints(0, source.length())
                .mapToObj(source::charAt)
                .limit(idLength)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

    }
    public static void main(String[] args) {
        String randomId = getRandomId(32,true,true);
        System.out.println(randomId);

    }
}

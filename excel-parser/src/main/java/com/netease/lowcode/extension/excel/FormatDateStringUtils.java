package com.netease.lowcode.extension.excel;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

public class FormatDateStringUtils {

    /**
     * 将输入的日期格式转换为标准的 yyyy/MM/dd,不足位补0
     *
     * @param origin
     * @return
     */
    public static String format(String origin){

        try{
            int i = Integer.parseInt(origin);
            Date date = DateUtils.addDays(new Date(-2209017943000L),i-1);
            return com.alibaba.excel.util.DateUtils.format(date,"yyyy-MM-dd");
        } catch (NumberFormatException e){
            // do nothing
        }

        if (origin == null || origin.length() < 8) {
            // 这里交给上级抛异常
            return origin;
        }

        // yyyy/M/d
        if (origin.length() == 8) {
            String year = origin.substring(0, 5);
            String month = "0" + origin.substring(5, 7);
            String day = "0" + origin.substring(7, 8);
            return year + month + day;
        }

        // yyyy/M/dd or yyyy/MM/d
        if (origin.length() == 9) {

            String year = origin.substring(0,5);

            char c = origin.charAt(4);
            int i = origin.lastIndexOf(c);
            // yyyy/M/dd
            if (i == 6) {
                String month = "0" + origin.substring(5, 7);
                String left = origin.substring(7);
                return year + month + left;
            }
            // yyyy/MM/d
            else {
                return origin.substring(0, 8) + "0" + origin.substring(8);
            }
        }

        return origin;
    }
}

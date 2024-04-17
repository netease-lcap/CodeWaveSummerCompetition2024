package com.wgx;


import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import com.wgx.lowcode.lunar.LunarResult;
import com.wgx.lowcode.util.LunarUtils;

/**
 * @Date: ${DATE} - ${MONTH} - ${DAY} - ${TIME}
 * @Description: com.netease
 * @version: 1.0
 */
public class Main {
    public static void main(String[] args) {

        // 测试阳历转农历
        System.out.println("测试阳历转农历:");
        System.out.println("阳历日期 2024-04-14 转换为农历: " + LunarUtils.convertSolarToLunar("2024-04-14"));
        System.out.println("阳历日期 300-04-14 转换为农历: " + LunarUtils.convertSolarToLunar("300-04-14"));
        System.out.println("阳历日期 null 转换为农历: " + LunarUtils.convertSolarToLunar(null));

        // 测试农历转阳历
        System.out.println("\n测试农历转阳历:");
        System.out.println("农历日期 2024, 1, 1 转换为阳历: " + LunarUtils.convertLunarToSolar(2024, 1, 1));
        System.out.println("农历日期 2024, 18, 1 转换为阳历: " + LunarUtils.convertLunarToSolar(2024, 18, 1));
        System.out.println("农历日期 2024, 1, 111 转换为阳历: " + LunarUtils.convertLunarToSolar(2024, 1, 111));
        System.out.println("农历日期 2024, null, 1 转换为阳历: " + LunarUtils.convertLunarToSolar(2024, null, 1));

        // 测试获取阴历月份信息
        System.out.println("\n测试获取阴历月份信息:");
        System.out.println("阳历日期 2024-02-12 对应的阴历月份信息: " + LunarUtils.getChineseMonthInfo("2024-02-12"));
        System.out.println("阳历日期 2024-18-12 对应的阴历月份信息: " + LunarUtils.getChineseMonthInfo("2024-18-12"));
        System.out.println("阳历日期 null 对应的阴历月份信息: " + LunarUtils.getChineseMonthInfo(null));


        // 测试获取节气
        System.out.println("\n测试获取节气:");
        System.out.println("阳历日期 2024-04-04 对应的节气: " + LunarUtils.getTermInfo("2024-04-04"));
        System.out.println("阳历日期 2024-04-04 对应的节气: " + LunarUtils.getTermInfo("2024-04-05"));
        System.out.println("阳历日期 2024-111-04 对应的节气: " + LunarUtils.getTermInfo("2024-111-04"));
        System.out.println("阳历日期 null 对应的节气: " + LunarUtils.getTermInfo(null));

        // 测试获取阴历年份的闰月信息，以及闰月的大小
        System.out.println("\n测试获取闰月信息:");
        System.out.println("农历年份 2024 年的闰月信息: " + LunarUtils.getLeapMonthInfo(2024));
        System.out.println("农历年份 2050 年的闰月信息: " + LunarUtils.getLeapMonthInfo(2050));
        System.out.println("农历年份 1900 年的闰月信息: " + LunarUtils.getLeapMonthInfo(1900));
        System.out.println("农历年份 2100 年的闰月信息: " + LunarUtils.getLeapMonthInfo(2100));
        System.out.println("农历年份 null 年的闰月信息: " + LunarUtils.getLeapMonthInfo(null));

        // 测试获取阴历年份的生肖
        System.out.println("\n测试获取阴历年份的生肖");
        System.out.println("农历年份 2024 年的生肖: " + LunarUtils.getChineseZodiac(2024));
        System.out.println("农历年份 1899 年的生肖: " + LunarUtils.getChineseZodiac(1899));
        System.out.println("农历年份 2500 年的生肖: " + LunarUtils.getChineseZodiac(2500));
        System.out.println("农历年份 null 年的生肖: " + LunarUtils.getChineseZodiac(null));

        // 测试判断节日
        System.out.println("\n测试判断节日:");
        System.out.println("农历日期 2024, 1, 1 是否为节日: " + LunarUtils.getFestivals(2024, 1, 1));
        System.out.println("农历日期 2024, 3, 11 是否为节日: " + LunarUtils.getFestivals(2024, 3, 11));
        System.out.println("农历日期 2024, 4, 55 是否为节日: " + LunarUtils.getFestivals(2024, 4, 55));
        System.out.println("农历日期 2024, null, 55 是否为节日: " + LunarUtils.getFestivals(2024, null, 55));

        // 测试计算天数差
        System.out.println("\n测试计算天数差:");
        System.out.println("农历日期 2023, 1, 1, 2024, 12, 29 之间的天数差: " + LunarUtils.getLunarDaysDifference(2023, 1, 1, 2024, 12, 29));
        System.out.println("农历日期 2024, 1, 1, 2024, 12, 29 之间的天数差: " + LunarUtils.getLunarDaysDifference(2024, 1, 1, 2024, 12, 29));
        System.out.println("农历日期 2025, 1, 1, 2024, 12, 29 之间的天数差: " + LunarUtils.getLunarDaysDifference(2025, 1, 1, 2024, 12, 29));
        System.out.println("农历日期 null, 1, 1, 2024, 12, 29 之间的天数差: " + LunarUtils.getLunarDaysDifference(null, 1, 1, 2024, 12, 29));
        System.out.println("农历日期 2023, 1, 1, null, 12, 29 之间的天数差: " + LunarUtils.getLunarDaysDifference(2023, 1, 1, null, 12, 29));

    }
}
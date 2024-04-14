package com.wjm.api;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.core.util.StringUtils;
import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class LunarConverter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Logger log = LoggerFactory.getLogger(LunarConverter.class);

    /**
     * 阳历日期转农历
     *
     * @param inputDate
     * @return 农历
     */
    @NaslLogic
    public static String getChineseDate(String inputDate) {

        try {
            LocalDate time = LocalDate.parse(inputDate, formatter);
            // 日期字符串符合格式，可以进行后续处理
            String[] split = inputDate.split("-");
            // 创建一个Solar对象
            Solar solar = new Solar(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            // 将公历日期转换为农历日期
            Lunar lunar = solar.getLunar();
            return lunar.toString();
        } catch (DateTimeParseException e) {
            // 日期字符串不符合格式，可以进行相应的错误处理
            return "请传入yyyy-MM-dd格式的时间";
        }

    }

    /**
     * 农历转阳历
     *
     * @param lunarYear  年（农历）
     * @param lunarMonth 月（农历），1到12，闰月为负，即闰2月=-2
     * @param lunarDay   日（农历），1到31
     * @return 阳历
     */
    @NaslLogic
    public static String getGregorianDate(Integer lunarYear, Integer lunarMonth, Integer lunarDay) {
        Lunar lunar = Lunar.fromYmd(lunarYear, lunarMonth, lunarDay);
        Solar solar = lunar.getSolar();
        return solar.toString();
    }

    /**
     * 获取阴历月份信息
     *
     * @param inputDate 传入的是公历日期
     * @return 阴历月份信息
     */
    @NaslLogic
    public static String getChineseMonthInfo(String inputDate) {
        try {
            LocalDate time = LocalDate.parse(inputDate, formatter);
            // 日期字符串符合格式，可以进行后续处理
            ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate(inputDate));
            // 农历日期
            int year = chineseDate.getChineseYear();
            int month = chineseDate.getMonth();

            // 获取农历月份的名称（正月、二月...）
            String monthName = chineseDate.getChineseMonthName();
            String leapMonth;
            String bigMonth;
            if (chineseDate.isLeapMonth()) {
                leapMonth = "闰月";
            } else {
                leapMonth = "非闰月";
            }
            int monthLength = DateUtils.monthDays(year, month);
            // 判断是大月还是小月
            if (monthLength > 29) {
                bigMonth = "大月";
            } else {
                bigMonth = "小月";
            }
            return monthName + " " + leapMonth + " " + bigMonth;
        } catch (DateTimeParseException e) {
            // 日期字符串不符合格式，可以进行相应的错误处理
            return "请传入yyyy-MM-dd格式的时间";
        }

    }

    /**
     * 获得节气
     *
     * @return 获得节气
     */
    @NaslLogic
    public static String getTerm(String inputDate) {
        LocalDate time = LocalDate.parse(inputDate, formatter);
        // 日期字符串符合格式，可以进行后续处理
        ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate(inputDate));
        String term = chineseDate.getTerm();
        if (StringUtils.isEmpty(term)) {
            term = "此日期非节气";
        }
        return term;
    }

    /**
     * 获取阴历年份的闰月信息，以及闰月的大小
     *
     * @param chineseYear
     * @return 闰月
     */
    @NaslLogic
    public static String getLeapMonth(Integer chineseYear) {
        String bigMonth;
        int month = DateUtils.leapMonth(chineseYear);
        int days = DateUtils.leapDays(chineseYear);
        if (days > 29) {
            bigMonth = "大月";
        } else {
            bigMonth = "小月";
        }
        if (month != 0) {
            return chineseYear + "年闰" + month + "月,是" + bigMonth;
        } else {
            return chineseYear + "年没有闰月";
        }
    }

    /**
     * 获取特定年份对应的生肖信息
     *
     * @param chineseYear 农历年份
     * @return 生肖
     */
    @NaslLogic
    public static String getChineseZodiac(Integer chineseYear) {
        final String[] animals = new String[]{"鼠", "牛", "虎", "兔", "龙", "蛇",
                "马", "羊", "猴", "鸡", "狗", "猪"};
        return animals[(chineseYear - 4) % 12];
    }

    /**
     * 判断特定阴历日期是否为传统节日，如春节、中秋节等
     *
     * @param lunarYear  年（农历）
     * @param lunarMonth 月（农历），1到12，闰月为负，即闰2月=-2
     * @param lunarDay   日（农历），1到31
     * @return 节日
     */
    @NaslLogic
    public static String getFestivals(Integer lunarYear, Integer lunarMonth, Integer lunarDay) {
        Lunar lunar = Lunar.fromYmd(lunarYear, lunarMonth, lunarDay);
        String festival = lunar.getFestivals().toString();
        festival = festival.substring(1, festival.length() - 1);
        String otherFestival = lunar.getOtherFestivals().toString();
        otherFestival = otherFestival.substring(1, otherFestival.length() - 1);
        if (StringUtils.isEmpty(festival) && StringUtils.isEmpty(otherFestival)) {
            return "非传统节日";
        } else {
            return festival + " " + otherFestival;
        }
    }

    /**
     * 计算两个阴历日期之间的天数差
     *
     * @param lunarYear1  年（农历）
     * @param lunarMonth1 月（农历），1到12，闰月为负，即闰2月=-2
     * @param lunarDay1   日（农历），1到31
     * @param lunarYear2
     * @param lunarMonth2
     * @param lunarDay2
     * @return 天数差
     */
    @NaslLogic
    public static String getDaysDifference(Integer lunarYear1, Integer lunarMonth1, Integer lunarDay1, Integer lunarYear2, Integer lunarMonth2, Integer lunarDay2) {
        Lunar lunar1 = Lunar.fromYmd(lunarYear1, lunarMonth1, lunarDay1);
        Lunar lunar2 = Lunar.fromYmd(lunarYear2, lunarMonth2, lunarDay2);
        Solar solar1 = lunar1.getSolar();
        Solar solar2 = lunar2.getSolar();
        LocalDate date1 = LocalDate.parse(solar1.toString());
        LocalDate date2 = LocalDate.parse(solar2.toString());
        long between = ChronoUnit.DAYS.between(date1, date2);
        return String.valueOf(between);
    }
}

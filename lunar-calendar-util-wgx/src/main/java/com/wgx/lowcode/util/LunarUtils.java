package com.wgx.lowcode.util;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateUtil;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.core.util.StringUtils;
import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import com.wgx.lowcode.lunar.LunarResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;


/**
 * @version: 1.0
 * 数学算数工具类
 */
public class LunarUtils {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD);
    private static final Logger logger = LoggerFactory.getLogger(LunarUtils.class);

    /**
     * 阳历日期转农历
     *
     * @param solarDateString 阳历日期字符串（格式：yyyy-MM-dd）
     * @return 农历日期转换结果对象
     */
    @NaslLogic
    public static LunarResult convertSolarToLunar(String solarDateString) {
        if (solarDateString == null) {
            logger.error("输入的阳历日期字符串不能为空");
            return new LunarResult(false, null, "输入的阳历日期字符串不能为空");
        }
        try {
            // 解析阳历日期
            LocalDate solarDate = LocalDate.parse(solarDateString, formatter);
            // 创建一个Solar对象
            Solar solar = new Solar(solarDate.getYear(), solarDate.getMonthValue(), solarDate.getDayOfMonth());
            // 将公历日期转换为农历日期
            Lunar lunar = solar.getLunar();
            return new LunarResult(true, lunar.toString(), "阳历日期转农历成功");
        } catch (DateTimeParseException e) {
            logger.error("输入的阳历日期字符串格式不正确，请使用'yyyy-MM-dd'格式");
            return new LunarResult(false, null, "输入的阳历日期字符串格式不正确，请使用'yyyy-MM-dd'格式");
        } catch (Exception e) {
            logger.error("未知异常导致阳历日期转农历失败", e);
            return new LunarResult(false, null, "未知异常导致阳历日期转农历失败");
        }
    }

    /**
     * 农历日期转阳历
     *
     * @param lunarYear  年（农历）
     * @param lunarMonth 月（农历），1到12或-2，闰月为负，即闰2月=-2
     * @param lunarDay   日（农历），1到30
     * @return 阳历
     */
    @NaslLogic
    public static LunarResult convertLunarToSolar(Integer lunarYear, Integer lunarMonth, Integer lunarDay) {
        if (lunarYear == null || lunarMonth == null || lunarDay == null) {
            logger.error("年(农历)、月(农历)、日(农历)不能为空");
            return new LunarResult(false, null, "年(农历)、月(农历)、日(农历)不能为空");
        }

        //不等于-2 则不是闰月 然后判断是否合法 如果不合法则返回错误
        if (lunarMonth != -2) {
            if ( lunarMonth < 1 || lunarMonth > 12 ) {
                logger.error("月份参数不合法,应输入1到12或-2，闰月为负，即闰2月=-2");
                return new LunarResult(false, null, "月份参数不合法,应输入1到12或-2，闰月为负，即闰2月=-2");
            }
        }

        //校验日是否合法
        if (lunarDay < 1 || lunarDay > 30) {
            logger.error("日参数不合法");
            return new LunarResult(false, null, "日参数不合法");
        }

            try{
                Lunar lunar = Lunar.fromYmd(lunarYear, lunarMonth, lunarDay);
                Solar solar = lunar.getSolar();
                return new LunarResult(true, solar.toString(), "农历日期转阳历成功");
            } catch (Exception e) {
            logger.error("未知异常导致农历日期转阳历失败", e);
            return new LunarResult(false, null, "未知异常导致农历日期转阳历失败");
        }
    }

    /**
     * 获取阴历月份信息
     *
     * @param solarDateString 阳历日期字符串（格式：yyyy-MM-dd）
     * @return 阴历月份信息
     */
    @NaslLogic
    public static LunarResult getChineseMonthInfo(String solarDateString) {
        if (solarDateString == null) {
            logger.error("输入的阳历日期字符串不能为空");
            return new LunarResult(false, null, "输入的阳历日期字符串不能为空");
        }
        try {
            LocalDate solarDate = LocalDate.parse(solarDateString, formatter);

            // 日期字符串符合格式，可以进行后续处理
            ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate(solarDateString));

            // 农历日期
            int lunarYear = chineseDate.getChineseYear();
            int lunarYearMonth = chineseDate.getMonth();

            // 获取农历月份的名称
            String monthName = chineseDate.getChineseMonthName();
            String leapMonth;
            String bigMonth;
            // 判断是否是闰月
            if (chineseDate.isLeapMonth()) {
                leapMonth = "闰月";
            } else {
                leapMonth = "非闰月";
            }
            // 获取农历月份的天数
            int monthLength = DateUtils.monthDays(lunarYear, lunarYearMonth);
            // 判断是大月还是小月
            if (monthLength == 30) {
                bigMonth = "大月";
            } else {
                bigMonth = "小月";
            }
            return new LunarResult(true, monthName + " " + leapMonth + " " + bigMonth, "获取阴历月份信息成功");
        } catch (DateTimeParseException e) {
            logger.error("输入的阳历日期字符串格式不正确，请使用'yyyy-MM-dd'格式");
            return new LunarResult(false, null, "输入的阳历日期字符串格式不正确，请使用'yyyy-MM-dd'格式");
        }
    }

    /**
     * 根据阳历日期获取节气
     * @param solarDateString 阳历日期字符串（格式：yyyy-MM-dd）
     * @return 获取节气
     */
    @NaslLogic
    public static LunarResult getTermInfo(String solarDateString) {
        if (solarDateString == null) {
            logger.error("输入的阳历日期字符串不能为空");
            return new LunarResult(false, null, "输入的阳历日期字符串不能为空");
        }
        try {
            // 日期字符串符合格式进行后续处理,不符合直接抛出DateTimeParseException
            LocalDate solarDate = LocalDate.parse(solarDateString, formatter);
            ChineseDate chineseDate = new ChineseDate(DateUtil.parseDate(solarDateString));

            String term = chineseDate.getTerm();
            //term为空则说明此日期非节气
            if (StringUtils.isEmpty(term)) {
                return new LunarResult(true,  null,"此日期非节气");
            }
            // 将公历日期转换为农历日期
            return new LunarResult(true,  term,"获取节气成功");
        } catch (DateTimeParseException e) {
            logger.error("输入的阳历日期字符串格式不正确，请使用'yyyy-MM-dd'格式");
            return new LunarResult(false, null, "输入的阳历日期字符串格式不正确，请使用'yyyy-MM-dd'格式");
        }
    }

    /**
     * 获取阴历年份的闰月信息，以及闰月的大小
     *
     * @param lunarYear 阴历年份
     * @return 闰月
     */
    @NaslLogic
    public static LunarResult getLeapMonthInfo(Integer lunarYear) {
        //判空
        if (lunarYear == null) {
            logger.error("输入参数阴历年份不得为空");
            return new LunarResult(false, null, "输入参数阴历年份不得为空");
        }

        //判断是否在1900~2099之外
        if (lunarYear < LunarCalendar.MIN_YEAR || lunarYear > LunarCalendar.MAX_YEAR) {
            logger.error("输入阴历年份不合法,应在1900年到2099年之间（包括边界）");
            return new LunarResult(false, null, "输入阴历年份不合法,应在1900年到2099年之间（包括边界）");
        }
        String bigMonth;
        int month = LunarCalendar.leapMonth(lunarYear);
        int days = LunarCalendar.daysInLunarMonth(lunarYear,month);
        if (days == 30) {
            bigMonth = "大月";
        } else {
            bigMonth = "小月";
        }
        if (month != 0) {
            return new LunarResult(true,  lunarYear + "年闰" + month + "月,这个月有"+days+"天,是" + bigMonth,"获取阴历年份的闰月信息成功");
        } else {
            return new LunarResult(true,  lunarYear + "年没有闰月","获取阴历年份的闰月信息成功");
        }
    }

    /**
     * 获取阴历年份的生肖信息
     *
     * @param lunarYear 阴历年份
     * @return 生肖
     */
    @NaslLogic
    public static LunarResult getChineseZodiac(Integer lunarYear) {
        if (lunarYear == null) {
            logger.error("输入参数阴历年份不能为空");
            return new LunarResult(false, null, "输入参数阴历年份不能为空");
        }
        return new LunarResult(true,  DateUtils.animalsYear(lunarYear), "获取阴历年份的生肖信息成功");
    }

    /**
     * 判断特定阴历日期是否为传统节日，如春节、中秋节等
     *
     * @param lunarYear  年（农历）
     * @param lunarMonth 月（农历），1到12，闰月为负，即闰2月=-2
     * @param lunarDay   日（农历），1到30
     * @return 节日
     */
    @NaslLogic
    public static LunarResult getFestivals(Integer lunarYear, Integer lunarMonth, Integer lunarDay) {

        if (lunarYear == null || lunarMonth == null || lunarDay == null) {
            logger.error("年(农历)、月(农历)、日(农历)不能为空");
            return new LunarResult(false, null, "年(农历)、月(农历)、日(农历)不能为空");
        }

        //不等于-2 则不是闰月 然后判断是否合法 如果不合法则返回错误
        if (lunarMonth != -2) {
            if ( lunarMonth < 1 || lunarMonth > 12 ) {
                logger.error("月份参数不合法,应输入1到12或-2，闰月为负，即闰2月=-2");
                return new LunarResult(false, null, "月份参数不合法,应输入1到12或-2，闰月为负，即闰2月=-2");
            }
        }

        //校验日是否合法
        if (lunarDay < 1 || lunarDay > 30) {
            logger.error("日参数不合法");
            return new LunarResult(false, null, "日参数不合法,应输入1~30范围内");
        }
        Lunar lunar = Lunar.fromYmd(lunarYear, lunarMonth, lunarDay);
        String festival = lunar.getFestivals().toString();
        festival = festival.substring(1, festival.length() - 1);
        String otherFestival = lunar.getOtherFestivals().toString();
        otherFestival = otherFestival.substring(1, otherFestival.length() - 1);
        if (StringUtils.isEmpty(festival) && StringUtils.isEmpty(otherFestival)) {
            return new LunarResult(true, "非传统节日", "判断特定阴历日期是否为传统节日成功");
        } else {
            return new LunarResult(true, festival + " " + otherFestival, "判断特定阴历日期是否为传统节日成功");
        }
    }



    /**
     * 计算两个阴历日期之间的天数差
     *
     * @param lunarYear1  第一个阴历年
     * @param lunarMonth1 第一个阴历月,范围：1到12，闰月为负，即闰2月=-2
     * @param lunarDay1   第一个阴历日,范围：1到31
     * @param lunarYear2 第二个阴历年
     * @param lunarMonth2 第二个阴历月,范围：1到12，闰月为负，即闰2月=-2
     * @param lunarDay2 第二个阴历日,范围：1到30
     * @return 天数差
     */
    @NaslLogic
    public static LunarResult getLunarDaysDifference(Integer lunarYear1, Integer lunarMonth1, Integer lunarDay1, Integer lunarYear2, Integer lunarMonth2, Integer lunarDay2) {

        if (lunarYear1 == null || lunarMonth1 == null || lunarDay1 == null) {
            logger.error("第一个年(农历)、月(农历)、日(农历)不能为空");
            return new LunarResult(false, null, "第一个年(农历)、月(农历)、日(农历)不能为空");
        }

        if (lunarYear2 == null || lunarMonth2 == null || lunarDay2 == null) {
            logger.error("第二个年(农历)、月(农历)、日(农历)不能为空");
            return new LunarResult(false, null, "第二个年(农历)、月(农历)、日(农历)不能为空");
        }

        if (lunarMonth1 != -2) {
            if ( lunarMonth1 < 1 || lunarMonth1 > 12 ) {
                logger.error("第一个月份参数不合法,应输入1到12或-2，闰月为负，即闰2月=-2");
                return new LunarResult(false, null, "第一个月份参数不合法,应输入1到12或-2，闰月为负，即闰2月=-2");
            }
        }

        if (lunarMonth2 != -2) {
            if ( lunarMonth2 < 1 || lunarMonth2 > 12 ) {
                logger.error("第二个月份参数不合法,应输入1到12或-2，闰月为负，即闰2月=-2");
                return new LunarResult(false, null, "第二个月份参数不合法,应输入1到12或-2，闰月为负，即闰2月=-2");
            }
        }

        //校验日是否合法
        if (lunarDay1 < 1 || lunarDay1 > 30) {
            logger.error("第一个日参数不合法，应输入1~30范围内");
            return new LunarResult(false, null, "第一个日参数不合法,应输入1~30范围内");
        }

        //校验日是否合法
        if (lunarDay2 < 1 || lunarDay2 > 30) {
            logger.error("第二个日参数不合法，应输入1~30范围内");
            return new LunarResult(false, null, "第二个日参数不合法,应输入1~30范围内");
        }

        //此处需要优化 需要入参获取有多少天 然后判断是否再有效范围
        Lunar lunar1 = Lunar.fromYmd(lunarYear1, lunarMonth1, lunarDay1);
        Lunar lunar2 = Lunar.fromYmd(lunarYear2, lunarMonth2, lunarDay2);
        Solar solar1 = lunar1.getSolar();
        Solar solar2 = lunar2.getSolar();
        LocalDate date1 = LocalDate.parse(solar1.toString());
        LocalDate date2 = LocalDate.parse(solar2.toString());
        long between = ChronoUnit.DAYS.between(date1, date2);

        return new LunarResult(true, String.valueOf(between), "计算两个阴历日期之间的天数差成功");
    }

}

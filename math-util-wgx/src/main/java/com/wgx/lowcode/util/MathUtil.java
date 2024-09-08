package com.wgx.lowcode.util;

import com.netease.lowcode.core.annotation.NaslLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @version: 1.0
 * 数学算数工具类
 */
public class MathUtil {

    private static final Logger logger = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    private static final long negativeZeroDoubleBits = Double.doubleToRawLongBits(-0.0d);

    /**
     * 返回整数的绝对值
     * @param a
     * @return
     */
    @NaslLogic
    public static Long absInteger(Long a) {
        // 检查参数是否为null，如果为null则抛出nullPointException异常
        if (a == null) {
            logger.error("参数不能为null");
            throw new NullPointerException("参数不能为null");
        }
        return (a < 0) ? -a : a;
    }

    /**
     * 返回小数的绝对值
     * @param a
     * @return
     */
    @NaslLogic
    public static Double absDecimal(Double a) {
        // 检查参数是否为null，如果为null则抛出NullPointerException
        if (a == null) {
            logger.error("参数不能为null");
            throw new NullPointerException("参数不能为null");
        }
        return (a <= 0.0D) ? 0.0D - a : a;
    }

    /**
     * 返回小于等于给定参数的最大整数
     * @param a
     * @return
     */
    @NaslLogic
    public static Double floor(Double a) {
        if (Double.isNaN(a) || a == Double.POSITIVE_INFINITY || a == Double.NEGATIVE_INFINITY) {
            logger.error("参数不能为null、非法数字、正无穷大或负无穷小，这些值无法进行取整操作");
            throw new IllegalArgumentException("参数不能为null、非法数字、正无穷大或负无穷小，这些值无法进行取整操作");
        }
        return Math.floor(a);
    }

    /**
     * 返回大于等于给定参数的最小整数
     * @param a
     * @return
     */
    @NaslLogic
    public static Double ceil(Double a) {
        //校验是否为非法参数
        if (Double.isNaN(a) || a == Double.POSITIVE_INFINITY || a == Double.NEGATIVE_INFINITY) {
            logger.error("参数不能为null、非法数字、正无穷大或负无穷小，这些值无法进行取整操作");
            throw new IllegalArgumentException("参数不能为null、非法数字、正无穷大或负无穷小，这些值无法进行取整操作");
        }
        return Math.ceil(a);
    }

    /**
     * 返回两个整数中较小的数
     * @param a 第一个整数
     * @param b 第二个整数
     * @return
     */
    @NaslLogic
    public static Long minInteger(Long a, Long b) {
        // 检查参数是否为null，如果为null则抛出nullPointException异常
        if (a == null || b == null) {
            logger.error("参数 a 或 b 不能为 null");
            throw new NullPointerException("参数 a 或 b 不能为 null");
        }
        return Math.min(a, b);
    }

    /**
     * 返回两个整数中较大的数
     * @param a 第一个整数
     * @param b 第二个整数
     * @return
     */
    @NaslLogic
    public static Long maxInteger(Long a, Long b) {
        // 检查参数是否为null，如果为null则抛出nullPointException异常
        if (a == null || b == null) {
            logger.error("参数 a 或 b 不能为 null");
            throw new NullPointerException("参数 a 或 b 不能为 null");
        }
        return Math.max(a, b);
    }
    /**
     * 返回两个小数中较小的数
     * @param a 第一个小数
     * @param b 第二个小数
     * @return
     */
    @NaslLogic
    public static Double minDecimal(Double a, Double b) {
        // 检查参数是否为null，如果为null则抛出nullPointException异常
        if (a == null || b == null) {
            logger.error("参数 a 或 b 不能为 null");
            throw new NullPointerException("参数 a 或 b 不能为 null");
        }
        if (a != a)
            return a;   // a is NaN
        if ((a == 0.0d) &&
                (b == 0.0d) &&
                (Double.doubleToRawLongBits(b) == negativeZeroDoubleBits)) {
            // Raw conversion ok since NaN can't map to -0.0.
            return b;
        }
        return (a <= b) ? a : b;
    }

    /**
     * 返回两个小数中较大的数
     * @param a 第一个小数
     * @param b 第二个小数
     * @return
     */
    @NaslLogic
    public static Double maxDecimal(Double a, Double b) {
        // 如果a或b为null，则抛出NullPointerException异常
        if (a == null || b == null) {
            logger.error("参数 a 或 b 不能为 null");
            throw new NullPointerException("参数 a 或 b 不能为 null");
        }
        if (a != a)
            return a;   // a is NaN
        if ((a == 0.0d) &&
                (b == 0.0d) &&
                (Double.doubleToRawLongBits(a) == negativeZeroDoubleBits)) {
            // Raw conversion ok since NaN can't map to -0.0.
            return b;
        }
        return (a >= b) ? a : b;
    }


    /**
     * 将小数转化为小数点后指定位数的字符串并进行四舍五入 例如传入：a=123.456，digits=2，则返回：123.46
     * @param a 待格式化的小数
     * @param digits 小数点后的位数。
     * @return 格式化后的字符串。
     */
    @NaslLogic
    public static String toFixed(Double a, Integer digits) {
        // 检查参数是否为null，如果为null则抛出nullPointException异常
        if (a == null || digits == null) {
            logger.error("参数a(待格式化的小数)或digit(小数点后的位数)不能为null");
            throw new NullPointerException("参数a(待格式化的小数)或digit(小数点后的位数)不能为null");
        }

        // 检查 digits 是否为非负整数
        if (digits < 0 || digits > 15) {
            logger.error("小数位数必须是0到15之间的整数，否则格式化结果可能不正确");
            throw new IllegalArgumentException("小数位数必须是0到15之间的整数，否则格式化结果可能不正确");
        }

        // 检查参数是否为非法数字
        if (Double.isNaN(a) || a == Double.POSITIVE_INFINITY || a == Double.NEGATIVE_INFINITY) {
            logger.error("参数不能为null、非法数字、正无穷大或负无穷小，这些值无法进行格式化操作");
            throw new IllegalArgumentException("参数不能为null、非法数字、正无穷大或负无穷小，这些值无法进行格式化操作");
        }

        System.out.println("a = " + a);
        // 使用 String.format 进行格式化
        return new BigDecimal(a).setScale(digits, RoundingMode.HALF_DOWN).toString();
    }

    /**
     * 计算两数之和
     *
     * @param a 第一个加数
     * @param b 第二个加数
     * @return 两数之和
     */
    @NaslLogic
    public static Double add(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("加数不能为null");
            throw new IllegalArgumentException("加数不能为null");
        }
        return new BigDecimal(a.toString()).add(new BigDecimal(b.toString())).doubleValue();
    }

    /**
     * 计算两数之和,保留两位小数,四舍五入
     *
     * @param a 第一个加数
     * @param b 第二个加数
     * @return 两数之和(四舍五入)
     */
    @NaslLogic
    public static Double addRounded(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("加数不能为null");
            throw new IllegalArgumentException("加数不能为null");
        }
        return new BigDecimal(a.toString()).add(new BigDecimal(b.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 计算两数之和,返回向上取整后的小数
     *
     * @param a 第一个加数
     * @param b 第二个加数
     * @return 两数之和 (向上取整)
     */
    @NaslLogic
    public static Double addCeiling(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("加数不能为null");
            throw new IllegalArgumentException("加数不能为null");
        }
        return new BigDecimal(a).add(new BigDecimal(b))
                .setScale(0, RoundingMode.CEILING).doubleValue();
    }

    /**
     * 计算两数之和,返回向下取整后的小数
     *
     * @param a 第一个加数
     * @param b 第二个加数
     * @return 两数之和 (向下取整)
     */
    @NaslLogic
    public static Double addFloor(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("加数不能为null");
            throw new IllegalArgumentException("加数不能为null");
        }
        return new BigDecimal(a.toString()).add(new BigDecimal(b.toString())).setScale(0, RoundingMode.FLOOR).doubleValue();
    }

    /**
     * 计算两数之和,自定义保留小数位数和取舍方式
     *
     * @param a         第一个加数
     * @param b         第二个加数
     * @param scale     结果的精度（小数点后的位数），必须是非负整数
     * @param roundingType 取舍方式：0 - 不进行取舍，1 - 四舍五入，2 - 向上取整，3 - 向下取整
     * @return          两数之和(自定义保留小数位数和取舍方式)
     */
    @NaslLogic
    public static Double addCustomScale(Double a, Double b, Integer scale, Integer roundingType) {

        if (a == null || b == null || scale == null || scale < 0 || roundingType == null) {
            logger.error("加数、精度或取舍方式不能为null，精度必须为非负整数，取舍方式应为0（不进行取舍）、1（四舍五入）、2（向上取整）或3（向下取整）");
            throw new IllegalArgumentException("加数、精度或取舍方式不能为null，精度必须为非负整数，取舍方式应为0（不进行取舍）、1（四舍五入）、2（向上取整）或3（向下取整）");
        }

        //根据给定的取舍方式枚举值返回对应的RoundingMode对象。
        RoundingMode roundingMode = getRoundingMode(roundingType);
        
        BigDecimal result = new BigDecimal(a.toString()).add(new BigDecimal(b.toString()));

        //如果取舍方式不为0，则对结果进行取舍
        if (roundingType != 0) {
            result = result.setScale(scale, roundingMode);
        }

        return result.doubleValue();
    }

    /**
     * 计算两数之差
     *
     * @param a 被减数
     * @param b 减数
     * @return 两数之差
     */
    @NaslLogic
    public static Double subtract(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("被减数或减数不能为null");
            throw new IllegalArgumentException("被减数或减数不能为null");
        }
        return new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString())).doubleValue();
    }

    /**
     * 计算两数之差，保留两位小数，四舍五入
     *
     * @param a 被减数
     * @param b 减数
     * @return 两数之差(四舍五入)
     */
    @NaslLogic
    public static Double subtractRounded(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("被减数或减数不能为null");
            throw new IllegalArgumentException("被减数或减数不能为null");
        }
        return new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 计算两数之差，返回向上取整后的小数
     *
     * @param a 被减数
     * @param b 减数
     * @return 两数之差(向上取整)
     */
    @NaslLogic
    public static Double subtractCeiling(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("被减数或减数不能为null");
            throw new IllegalArgumentException("被减数或减数不能为null");
        }
        return new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString())).setScale(0, RoundingMode.CEILING).doubleValue();
    }

    /**
     * 计算两数之差，返回向下取整后的小数
     *
     * @param a 被减数
     * @param b 减数
     * @return 两数之差(向下取整)
     */
    @NaslLogic
    public static Double subtractFloor(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("被减数或减数不能为null");
            throw new IllegalArgumentException("被减数或减数不能为null");
        }
        return new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString())).setScale(0, RoundingMode.FLOOR).doubleValue();
    }

    /**
     * 计算两数之差，自定义保留小数位数和取舍方式
     *
     * @param a         被减数
     * @param b         减数
     * @param scale     结果的精度（小数点后的位数），必须是非负整数
     * @param roundingType 取舍方式：0 - 不进行取舍，1 - 四舍五入，2 - 向上取整，3 - 向下取整
     * @return          两数之差
     */
    @NaslLogic
    public static Double subtractCustomScale(Double a, Double b, Integer scale, Integer roundingType) {

        if (a == null || b == null || scale == null || scale < 0 || roundingType == null) {
            logger.error("被减数、减数、精度或取舍方式不能为null，精度必须为非负整数，取舍方式应为0（不进行取舍）、1（四舍五入）、2（向上取整）或3（向下取整）");
            throw new IllegalArgumentException("被减数、减数、精度或取舍方式不能为null，精度必须为非负整数，取舍方式应为0（不进行取舍）、1（四舍五入）、2（向上取整）或3（向下取整）");
        }

        //根据给定的取舍方式枚举值返回对应的RoundingMode对象。
        RoundingMode roundingMode = getRoundingMode(roundingType);

        BigDecimal result = new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString()));

        //如果取舍方式不为0，则对结果进行取舍
        if (roundingType != 0) {
            result = result.setScale(scale, roundingMode);
        }

        return result.doubleValue();
    }

    /**
     * 计算两数之商
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 两数之商
     */
    @NaslLogic
    public static Double divide(Double dividend, Double divisor) {
        if (dividend == null || divisor == null) {
            logger.error("被除数或除数不能为null");
            throw new IllegalArgumentException("被除数或除数不能为null");
        }
        return new BigDecimal(dividend.toString()).divide(new BigDecimal(divisor.toString()), MathContext.DECIMAL128).doubleValue();
    }

    /**
     * 计算两数之商，保留两位小数，四舍五入
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 两数之商（四舍五入）
     */
    @NaslLogic
    public static Double divideRounded(Double dividend, Double divisor) {
        if (dividend == null || divisor == null) {
            logger.error("被除数或除数不能为null");
            throw new IllegalArgumentException("被除数或除数不能为null");
        }
        return new BigDecimal(dividend.toString()).divide(new BigDecimal(divisor.toString()), 2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 计算两数之商，返回向上取整后的小数
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 两数之商（向上取整）
     */
    @NaslLogic
    public static Double divideCeiling(Double dividend, Double divisor) {
        if (dividend == null || divisor == null) {
            logger.error("被除数或除数不能为null");
            throw new IllegalArgumentException("被除数或除数不能为null");
        }
        return new BigDecimal(dividend.toString()).divide(new BigDecimal(divisor.toString()), 0, RoundingMode.CEILING).doubleValue();
    }

    /**
     * 计算两数之商，返回向下取整后的小数
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 两数之商（向下取整）
     */
    @NaslLogic
    public static Double divideFloor(Double dividend, Double divisor) {
        if (dividend == null || divisor == null) {
            logger.error("被除数或除数不能为null");
            throw new IllegalArgumentException("被除数或除数不能为null");
        }
        return new BigDecimal(dividend.toString()).divide(new BigDecimal(divisor.toString()), 0, RoundingMode.FLOOR).doubleValue();
    }

    /**
     * 计算两数之商，自定义保留小数位数和取舍方式
     *
     * @param dividend      被除数
     * @param divisor       除数
     * @param scale         结果的精度（小数点后的位数），必须是非负整数
     * @param roundingType  取舍方式：0（不进行取舍） 1 - 四舍五入，2 - 向上取整，3 - 向下取整
     * @return              两数之商
     */
    @NaslLogic
    public static Double divideCustomScale(Double dividend, Double divisor, Integer scale, Integer roundingType) {

        if (dividend == null || divisor == null || scale == null || scale < 0 || roundingType == null) {
            logger.error("被除数、除数、精度或取舍方式不能为null，精度必须为非负整数，取舍方式应为0（不进行取舍）、1（四舍五入）、2（向上取整）或3（向下取整）");
            throw new IllegalArgumentException("被除数、除数、精度或取舍方式不能为null，精度必须为非负整数，取舍方式应为0（不进行取舍）、1（四舍五入）、2（向上取整）或3（向下取整）");
        }

        if (divisor == 0.0) {
            // 抛出异常或者返回一个特定的值，比如 null 或者 Double.POSITIVE_INFINITY
            logger.error("除数不能为0");
            throw new ArithmeticException("除数不能为0");
        }

        // 根据给定的取舍方式枚举值返回对应的RoundingMode对象。
        RoundingMode roundingMode = getRoundingMode(roundingType);
        BigDecimal result = null;

        try{
            result = new BigDecimal(dividend.toString())
                    .divide(new BigDecimal(divisor.toString()), MathContext.DECIMAL128);
        }catch (ArithmeticException e){
            logger.error("算数异常: ",e);
            throw e;
        }

        //如果取舍方式不为0，则对结果进行取舍
        if (roundingType != 0) {
            result = result.setScale(scale, roundingMode);
        }

        return result.doubleValue();
    }

    /**
     * 计算两数之积
     *
     * @param a 第一个乘数
     * @param b 第二个乘数
     * @return 两数之积
     */
    @NaslLogic
    public static Double multiply(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("乘数不能为null");
            throw new IllegalArgumentException("乘数不能为null");
        }
        return new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString())).doubleValue();
    }

    /**
     * 计算两数之积，保留两位小数，四舍五入
     *
     * @param a 第一个乘数
     * @param b 第二个乘数
     * @return 两数之积（四舍五入）
     */
    @NaslLogic
    public static Double multiplyRounded(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("乘数不能为null");
            throw new IllegalArgumentException("乘数不能为null");
        }
        return new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 计算两数之积，返回向上取整后的小数
     *
     * @param a 第一个乘数
     * @param b 第二个乘数
     * @return 两数之积（向上取整）
     */
    @NaslLogic
    public static Double multiplyCeiling(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("乘数不能为null");
            throw new IllegalArgumentException("乘数不能为null");
        }
        return new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString())).setScale(0, RoundingMode.CEILING).doubleValue();
    }

    /**
     * 计算两数之积，返回向下取整后的小数
     *
     * @param a 第一个乘数
     * @param b 第二个乘数
     * @return 两数之积（向下取整）
     */
    @NaslLogic
    public static Double multiplyFloor(Double a, Double b) {
        if (a == null || b == null) {
            logger.error("乘数不能为null");
            throw new IllegalArgumentException("乘数不能为null");
        }
        return new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString())).setScale(0, RoundingMode.FLOOR).doubleValue();
    }

    /**
     * 计算两数之积，自定义保留小数位数和取舍方式
     *
     * @param a          第一个乘数
     * @param b          第二个乘数
     * @param scale      结果的精度（小数点后的位数），必须是非负整数
     * @param roundingType 取舍方式：0 - 不进行取舍，1 - 四舍五入，2 - 向上取整，3 - 向下取整
     * @return            两数之积
     */
    @NaslLogic
    public static Double multiplyCustomScale(Double a, Double b, Integer scale, Integer roundingType) {

        if (a == null || b == null || scale == null || scale < 0 || roundingType == null) {
            logger.error("乘数、精度或取舍方式不能为null，精度必须为非负整数，取舍方式应为0（不进行取舍）、1（四舍五入）、2（向上取整）或3（向下取整）");
            throw new IllegalArgumentException("乘数、精度或取舍方式不能为null，精度必须为非负整数，取舍方式应为0（不进行取舍）、1（四舍五入）、2（向上取整）或3（向下取整）");
        }

        // 根据给定的取舍方式枚举值返回对应的RoundingMode对象。
        RoundingMode roundingMode = getRoundingMode(roundingType);

        BigDecimal result = new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString()));

        //如果取舍方式不为0，则对结果进行取舍
        if (roundingType != 0) {
            result = result.setScale(scale, roundingMode);
        }

        return result.doubleValue();
    }

    /**
     * 根据给定的取舍方式枚举值返回对应的RoundingMode对象。
     *
     * @param roundingType 取舍方式：0 - 不进行取舍，1 - 四舍五入，2 - 向上取整，3 - 向下取整
     * @return 对应的RoundingMode对象
     * @throws IllegalArgumentException 如果输入的取舍方式无效
     */
    private static RoundingMode getRoundingMode(Integer roundingType) throws IllegalArgumentException {
        switch (roundingType) {
            case 0:
                return RoundingMode.UNNECESSARY; // 不进行取舍
            case 1:
                return RoundingMode.HALF_UP; // 四舍五入
            case 2:
                return RoundingMode.CEILING; // 向上取整
            case 3:
                return RoundingMode.FLOOR; // 向下取整
            default:
                throw new IllegalArgumentException("无效的取舍方式，应为0（不进行取舍）、1（四舍五入）、2（向上取整）或3（向下取整）");
        }
    }
}

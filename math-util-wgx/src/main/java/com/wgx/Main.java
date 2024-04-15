package com.wgx;

import com.wgx.lowcode.util.MathUtil;

/**
 * @Date: ${DATE} - ${MONTH} - ${DAY} - ${TIME}
 * @Description: com.netease
 * @version: 1.0
 */
public class Main {
    public static void main(String[] args) {
        // 绝对值测试
        System.out.println("absInteger(10L) = " + MathUtil.absInteger(10L));

        // 小数绝对值测试
        System.out.println("absDecimal(-5.5) = " + MathUtil.absDecimal(-5.5));

        // 取整测试
        System.out.println("floor(5.7) = " + MathUtil.floor(5.7));
        System.out.println("ceil(5.7) = " + MathUtil.ceil(5.7));
        System.out.println("floor(-5.7) = " + MathUtil.floor(-5.7));
        System.out.println("ceil(-5.7) = " + MathUtil.ceil(-5.7));

        // 最大值和最小值测试
        System.out.println("minInteger(10L, 20L) = " + MathUtil.minInteger(10L, 20L));
        System.out.println("maxInteger(10L, 20L) = " + MathUtil.maxInteger(10L, 20L));
        System.out.println("min(5.5, 4.3) = " + MathUtil.minDecimal(5.5, 4.3));
        System.out.println("max(5.5, 4.3) = " + MathUtil.maxDecimal(5.5, 4.3));

        // 浮点数格式化测试
        System.out.println("toFixed(123.456, 2) = " + MathUtil.toFixed(123.456, 2));

        // 两数之和测试
        System.out.println("add(3.14, 2.718) = " + MathUtil.add(3.14, 2.718));
        System.out.println("addRounded(3.14, 2.718) = " + MathUtil.addRounded(3.14, 2.718));
        System.out.println("addCeiling(3.14, 2.718) = " + MathUtil.addCeiling(3.14, 2.718));
        System.out.println("addFloor(3.14, 2.718) = " + MathUtil.addFloor(3.14, 2.718));
        System.out.println("addCustomScale(3.14, 2.718, 2, 1) = " + MathUtil.addCustomScale(3.14, 2.718, 2, 1));

        // 两数之差测试
        System.out.println("subtract(5.5, 2.3) = " + MathUtil.subtract(5.5, 2.3));
        System.out.println("subtractRounded(5.5, 2.3) = " + MathUtil.subtractRounded(5.5, 2.3));
        System.out.println("subtractCeiling(5.5, 2.3) = " + MathUtil.subtractCeiling(5.5, 2.3));
        System.out.println("subtractFloor(5.5, 2.3) = " + MathUtil.subtractFloor(5.5, 2.3));
        System.out.println("subtractCustomScale(5.5, 2.3, 2, 1) = " + MathUtil.subtractCustomScale(5.5, 2.3, 2, 1));

        // 两数之商测试
        System.out.println("divide(10.0, 2.0) = " + MathUtil.divide(10.0, 2.0));
        System.out.println("divideRounded(10.0, 2.0) = " + MathUtil.divideRounded(10.0, 2.0));
        System.out.println("divideCeiling(10.0, 2.0) = " + MathUtil.divideCeiling(10.0, 2.0));
        System.out.println("divideFloor(10.0, 2.0) = " + MathUtil.divideFloor(10.0, 2.0));
        System.out.println("divideCustomScale(10.0, 2.0, 2, 1) = " + MathUtil.divideCustomScale(10.0, 2.0, 2, 1));

        // 两数之积测试
        System.out.println("multiply(3.14, 2.718) = " + MathUtil.multiply(3.14, 2.718));
        System.out.println("multiplyRounded(3.14, 2.718) = " + MathUtil.multiplyRounded(3.14, 2.718));
        System.out.println("multiplyCeiling(3.14, 2.718) = " + MathUtil.multiplyCeiling(3.14, 2.718));
        System.out.println("multiplyFloor(3.14, 2.718) = " + MathUtil.multiplyFloor(3.14, 2.718));
        System.out.println("multiplyCustomScale(3.14, 2.718, 2, 1) = " + MathUtil.multiplyCustomScale(3.14, 2.718, 2, 1));

        // 异常测试
        try {
            System.out.println("absInteger(null) = " + MathUtil.absInteger(null));
        } catch (Exception e) {
            System.out.println("absInteger(null) = " + e.getMessage());
        }

        try {
            System.out.println("absDouble(null) = " + MathUtil.absDecimal(null));
        } catch (Exception e) {
            System.out.println("absDouble(null) = " + e.getMessage());
        }

        try {
            System.out.println("floor(NaN) = " + MathUtil.floor(Double.NaN));
        } catch (Exception e) {
            System.out.println("floor(NaN) =  " + e.getMessage());
        }

        try {
            System.out.println("ceil(NaN) = " + MathUtil.ceil(Double.NaN));
        } catch (Exception e) {
            System.out.println("ceil(NaN) = " + e.getMessage());
        }

        try {
            System.out.println("minInteger(null, 20L) = " + MathUtil.minInteger(null, 20L));
        } catch (Exception e) {
            System.out.println("minInteger(null, 20L) = " + e.getMessage());
        }

        try {
            System.out.println("maxInteger(10L, null) = " + MathUtil.maxInteger(10L, null));
        } catch (Exception e) {
            System.out.println("maxInteger(10L, null) = " + e.getMessage());
        }

        try {
            System.out.println("min(null, 4.3) = " + MathUtil.minDecimal(null, 4.3));
        } catch (Exception e) {
            System.out.println("min(null, 4.3) = " + e.getMessage());
        }

        try {
            System.out.println("max(null, 4.3) = " + MathUtil.maxDecimal(null, 4.3));
        } catch (Exception e) {
            System.out.println("max(null, 4.3) = " + e.getMessage());
        }

        try {
            System.out.println("toFixed(5.0,null) = " + MathUtil.toFixed(5.0, null));
        } catch (Exception e) {
            System.out.println("toFixed(5.0, null) = " + e.getMessage());
        }

    }
}
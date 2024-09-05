package com.netease.lowcode.extension.excel;

import org.apache.commons.lang3.StringUtils;

public final class ExcelCommonUtils {
    private static final int INFINITE_INDEX = -1;
    /**
     * 将行列值转换成excel的坐标值
     * @param rowNum 行下标，从0开始
     * @param columnNum 列下标，从0开始
     * @return
     */
    public static String indexToCellLocate(int rowNum, int columnNum) {
        return indexToCellColumn(columnNum) + (rowNum + 1);
    }

    /**
     * 将下标值转换为excel的列数(A、B、AA等)
     * @param index 列下标，从0开始
     * @return
     */
    public static String indexToCellColumn(int index) {
        int x = index / 26;
        int y = index % 26;

        StringBuilder result = new StringBuilder("");

        if (x == 0) {
            result.append((char) ('A' + y));
        } else if (x < 27) {
            result.append((char) ('A' + x - 1));
            result.append((char) ('A' + y));
        } else {
            result.append(indexToCellColumn(x)).append((char) ('A' + y));
        }

        return result.toString();
    }

    public static int[] cellLocateToIndex(String cellLocate) {
        int[] result = new int[]{INFINITE_INDEX, INFINITE_INDEX};
        if (StringUtils.isBlank(cellLocate)) {
            return result;
        }

        cellLocate = cellLocate.toUpperCase();
        char[] chars = cellLocate.toCharArray();
        int rowNumArrayIndex = chars.length;
        for (int i = chars.length - 1; i >= 0; i--) {
            char c = chars[i];
            if (c >= 'A' && c <= 'Z') {
                //属于字母则表示的是列数
                if (result[1] == INFINITE_INDEX) {
                    // 没有指定行数
                    result[1] = (c - 'A');
                } else {
                    result[1] += (c - 'A' + 1) * (rowNumArrayIndex - 1 - i) * 26;
                }
            } else {
                rowNumArrayIndex = i;
            }
        }

        if (rowNumArrayIndex >= 0 && rowNumArrayIndex < chars.length) {
            try {
                result[0] = Integer.parseInt(cellLocate.substring(rowNumArrayIndex)) - 1;
            } catch (NumberFormatException e) {
                throw new ExcelParseException(String.format("非法的cell坐标错误%s", cellLocate));
            }
        }

        return result;
    }

    public static boolean isInRect(int index, int startCellIndex, int endCellIndex) {
        if (startCellIndex != INFINITE_INDEX && startCellIndex > index) {
            return false;
        }
        if (endCellIndex != INFINITE_INDEX && endCellIndex < index) {
            return false;
        }

        return true;
    }
}

package com.netease.lowcode.extensions.easyexcel.poi;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.extensions.response.ExportBigDataResponse;
import java.util.*;

public class POIExcelCreate {


    /**
     * 只能后端调用，不支持前端直接调用
     *
     * @param request
     * @return
     */
    @NaslLogic
    public static ExportBigDataResponse poiCreateXls(POIExcelCreateDTO request) {
        // 脚手架打包后，修改 nasl-metadata 文件，增加泛型参数
        /**
         *       "typeParams":[
         *         {
         *             "concept":"TypeParam",
         *             "name":"T"
         *         }
         *       ],
         *       "params": [{
         *       ...
         *       }]
         */
        // 第二部注释 打包插件，注释该方法，重新打jar包替换
        throw new RuntimeException("打包方式不对，请重新打包");
    }

    public static <T> ExportBigDataResponse poiCreateXls(POIExcelCreateDTO request, Class<T> clazz) {
        // 由于结构的Label标签IDE中输入字符不能超过63 因此提供覆写的地方，传入。
        request.validate();

        ExcelData excelData = new ExcelData();
        // 设置属性与表头的映射
        excelData.setName(request.getExportFileName());
        // 一个逻辑目前仅支持一个sheet
        SheetData sheetData = new SheetData();

        // 1. 当列很多时，需要手动繁琐的add，最好通过 解析 T 泛型
        // 2. 解析Structure属性的@Label注解参数，获取表头，列的属性：列宽、列的order、列值的条件
        // 4. 合并单元格的情况（暂不支持，涉及到计算）
        // 5. 导出动态列 如何解决。（在structure中使用Map表示动态的列，属性如何设置？）
        // 6. 导出日期，时间格式、时区的问题。 其他类型映射到excel的问题
        // 7. 小数点处理的问题，保留小数的问题；
        // 8. 列值条件，引入groovy语法
        // 9. 导出多个sheet?（暂不支持）

        CommonHandler.parseTitle(clazz, sheetData, request.getLabels());
        // 添加表头
        CommonHandler.addTitle(sheetData);
        CommonHandler.addData(request, sheetData);

        excelData.setSheetList(Collections.singletonList(sheetData));

        return CommonHandler.createXls(excelData);
    }

    @NaslLogic
    public static String poiCreateXlsx() {
        return null;
    }


    public static String poiCreateXlsAsync() {
        return null;
    }

    public static String poiCreateXlsxAsync() {
        return null;
    }

}

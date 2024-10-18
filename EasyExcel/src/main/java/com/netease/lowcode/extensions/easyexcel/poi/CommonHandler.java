package com.netease.lowcode.extensions.easyexcel.poi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.netease.lowcode.extensions.FileUtils;
import com.netease.lowcode.extensions.JsonUtil;
import com.netease.lowcode.extensions.UploadResponseDTO;
import com.netease.lowcode.extensions.response.ExportBigDataResponse;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

@Component("libraryEasyExcelCommonHandler")
public class CommonHandler {
    private static FileUtils fileUtils;

    public static void parseTitle(Class<?> clazz,SheetData sheetData,Map<String,String> labels) {
        // 解析表头数据：复杂表头：在label中 使用 主表头1-子表头2 会自动合并相同且相邻的主表头
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {

            // 字段名
            String name = declaredField.getName();
            Class<?> type = declaredField.getType();
            String title = "";

            // 判断是否覆写label
            String overrideLabel = "";
            if (MapUtils.isNotEmpty(labels) && labels.containsKey(name)) {
                overrideLabel = labels.get(name);
                try {
                    title = parseLabel(sheetData, name, overrideLabel);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            // 获取注解表头
            Annotation[] annotations = declaredField.getAnnotations();
            if (Objects.nonNull(annotations) && StringUtils.isBlank(overrideLabel)) {
                for (Annotation annotation : annotations) {
                    String simpleName = annotation.annotationType().getSimpleName();
                    // 如果没有Label注解就不解析 表头样式，该列不导出到excel
                    if(StringUtils.equals("Label",simpleName)){
                        //
                        try {
                            String value = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                            // 如果是以 @Style=json 开头就解析json内容作为样式
                            // 否则直接获取value作为表头

                            title = parseLabel(sheetData, name, value);

                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                                 JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            sheetData.putColHeadEntry(name, title);
            sheetData.putColJavaTypeEntry(name, type);
        }
    }

    private static String parseLabel(SheetData sheetData, String name, String value) throws JsonProcessingException {
        String title;
        if(StringUtils.startsWith(value,"@Style=")) {
            // 解析样式
            String substring = value.substring(7);

            CellStyle style = JsonUtil.fromJson(substring, CellStyle.class);
            sheetData.putColStyleEntry(name, style);
            if(Objects.nonNull(style.getIndex())){
                sheetData.putColHeadIndexEntry(name, style.getIndex());
            }
            title = style.getTitle();
        } else {
            title = value;
        }
        return title;
    }

    public static void addTitle(SheetData sheetData) {

        // todo: 目前仅支持解析一级表头，后续可通过style配置表头的parent节点，从而获取合并区域

        RowData rowData = new RowData();

        List<Boolean> indexFlag = new ArrayList<>(sheetData.getColHeadMap().size());
        for (int i = 0; i < sheetData.getColHeadMap().size(); i++) {
            indexFlag.add(true);
        }
        for (Integer value : sheetData.getColHeadIndexMap().values()) {
            // 可能超界

            indexFlag.set(value, false);
        }


        int tmpIndex = 0;
        for (Map.Entry<String, String> entry : sheetData.getColHeadMap().entrySet()) {
            Integer colHeadIndex = sheetData.getColHeadIndex(entry.getKey());
            if (Objects.nonNull(colHeadIndex) && colHeadIndex >= 0) {
                // 已经存在不重复添加
                continue;
            }

            while (tmpIndex < indexFlag.size()) {

                if (indexFlag.get(tmpIndex)) {
                    indexFlag.set(tmpIndex, false);
                    sheetData.putColHeadIndexEntry(entry.getKey(), tmpIndex++);
                    break;
                }

                tmpIndex++;
            }
        }


        // 如果在style中未指定列顺序，则指定默认顺序
        for (Map.Entry<String, String> entry : sheetData.getColHeadMap().entrySet()) {
            CellData cellData = new CellData();
            cellData.setIndex(sheetData.getColHeadIndex(entry.getKey()));
            cellData.setData(entry.getValue());
            rowData.addCell(cellData);
        }
        sheetData.addRow(rowData);
    }

    public static void addData(POIExcelCreateDTO request, SheetData sheetData) {
        try {
            JsonNode arrayNode = JsonUtil.fromJson(request.getJsonData());
            if (!arrayNode.isArray()) {
                throw new RuntimeException("不是数组");
            }
            for (int i = 0; i < arrayNode.size(); i++) {

                JsonNode node = arrayNode.get(i);
                RowData rowData = new RowData();
                sheetData.addRow(rowData);


                for (String colName : sheetData.getColNames()) {

                    // 默认不指定顺序，后续看情况支持通过cell索引指定列的顺序
                    CellData cellData = new CellData();
                    // 处理类型映射
                    // * NASL类型和Java类型匹配关系：
                    // * NASL：Boolean，  Java：java.lang.Boolean
                    // * NASL：Integer，  Java：java.lang.Long
                    // * NASL：String，   Java：java.lang.String
                    // * NASL：Time，     Java：java.time.LocalTime
                    // * NASL：Date，     Java：java.time.LocalDate
                    // * NASL：DateTime， Java：java.time.ZonedDateTime
                    // * NASL：Decimal，  Java：java.math.BigDecimal
                    // * NASL：List，     Java：java.util.List
                    // * NASL：Map，      Java：java.util.Map
                    if (Boolean.class.equals(sheetData.getColJavaType(colName))) {
                        JsonNode colValue = node.get(colName);
                        cellData.setData(colValue.asBoolean());
                    } else if (Long.class.equals(sheetData.getColJavaType(colName))) {
                        JsonNode colValue = node.get(colName);
                        cellData.setData(colValue.asDouble());
                    } else if (String.class.equals(sheetData.getColJavaType(colName))) {
                        JsonNode colValue = node.get(colName);
                        cellData.setData(colValue.asText());
                    } else if (LocalTime.class.equals(sheetData.getColJavaType(colName))) {
                        JsonNode colValue = node.get(colName);
                        cellData.setData(colValue.asText());
                    } else if (LocalDate.class.equals(sheetData.getColJavaType(colName))) {
                        JsonNode colValue = node.get(colName);
                        cellData.setData(colValue.asText());
                    } else if (ZonedDateTime.class.equals(sheetData.getColJavaType(colName))) {
                        JsonNode colValue = node.get(colName);
                        cellData.setData(colValue.asText());
                    } else if (BigDecimal.class.equals(sheetData.getColJavaType(colName))) {
                        JsonNode colValue = node.get(colName);
                        cellData.setData(colValue.asText());
                    } else if (List.class.equals(sheetData.getColJavaType(colName))) {
                        JsonNode colValue = node.get(colName);
                        cellData.setData(colValue.asText());
                    } else if (Map.class.equals(sheetData.getColJavaType(colName))) {
                        JsonNode colValue = node.get(colName);
                        cellData.setData(colValue.asText());
                    } else {
                        JsonNode colValue = node.get(colName);
                        cellData.setData(colValue.asText());
                    }
                    // 设置列索引，与表头一致
                    cellData.setIndex(sheetData.getColHeadIndex(colName));
                    // 读取样式
                    CellStyle colStyle = sheetData.getColStyle(colName);
                    cellData.setCellStyle(Objects.isNull(colStyle) ? null : colStyle.clone());
                    rowData.addCell(cellData);
                }

            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createXlsx(ExcelData excelData) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
    }

    public static ExportBigDataResponse createXls(ExcelData excelData) {
        long start = System.currentTimeMillis();
        HSSFWorkbook wb = new HSSFWorkbook();
        for (SheetData sheetData : excelData.getSheetList()) {
            HSSFSheet sheet = wb.createSheet();

            // 设置合并区域
            for (MergedRegion mergedRegion : sheetData.getMergedRegionList()) {
                sheet.addMergedRegion(new CellRangeAddress(mergedRegion.getFirstRow(),
                        mergedRegion.getLastRow(), mergedRegion.getFirstCol(), mergedRegion.getLastCol()));
            }

            // 设置样式前预处理
            preSetCellStyle(sheetData);


            for (int i = 0; i < sheetData.getRowDataList().size(); i++) {

                // 创建一行数据
                HSSFRow row = sheet.createRow(i);
                RowData rowData = sheetData.getRow(i);

                for (int j = 0; j < rowData.getCellDataList().size(); j++) {

                    HSSFCell cell = row.createCell(j);
                    CellData cellData = rowData.getCell(j);

                    if (cellData.getData() instanceof String) {
                        cell.setCellValue((String) cellData.getData());
                    } else if (cellData.getData() instanceof LocalDate) {
                        cell.setCellValue((LocalDate) cellData.getData());
                    } else if (cellData.getData() instanceof Boolean) {
                        cell.setCellValue((Boolean) cellData.getData());
                    } else if (cellData.getData() instanceof Double) {
                        cell.setCellValue((Double) cellData.getData());
                    } else if (cellData.getData() instanceof Date) {
                        cell.setCellValue((Date) cellData.getData());
                    } else if (cellData.getData() instanceof LocalDateTime) {
                        cell.setCellValue((LocalDateTime) cellData.getData());
                    } else if (cellData.getData() instanceof Calendar) {
                        cell.setCellValue((Calendar) cellData.getData());
                    } else if (cellData.getData() instanceof RichTextString) {
                        cell.setCellValue((RichTextString) cellData.getData());
                    } else {
                        throw new RuntimeException("不支持的类型");
                    }

                    // 设置单元格样式
                    setCellStyle(cellData, cell, wb);

                }
            }
        }

        File exportFile = null;
        try {
            // 创建目录
            String path = String.join("/", "data", String.valueOf(System.currentTimeMillis()));
            File dir = new File(path);
            if (!dir.mkdirs()) {
                throw new RuntimeException("创建目录失败");
            }
            exportFile = new File(path,excelData.getName());
            FileOutputStream fos = new FileOutputStream(exportFile);

            //wb.write(Files.newOutputStream(Paths.get(excelData.getName())));
            wb.write(fos);
            fos.flush();
            fos.close();

            UploadResponseDTO uploadResponseDTO = fileUtils.uploadFileV2(exportFile);
            return ExportBigDataResponse.OK(uploadResponseDTO.getFilePath(), uploadResponseDTO.getResult(), (double) (System.currentTimeMillis() - start) / 1000, (double) exportFile.length());
        } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            // 删除文件
            if (Objects.nonNull(exportFile)) {
                FileUtils.delete(exportFile.getParentFile());
            }
        }
    }

    private static void preSetCellStyle(SheetData sheetData) {
        // 对于整行样式，单独做预处理
        for (RowData rowData : sheetData.getRowDataList()) {

            // 这一行最终的背景色
            String background = null;

            // 遍历行，是否有整行样式
            for (CellData cellData : rowData.getCellDataList()) {
                // 检查样式
                CellStyle cellStyle = cellData.getCellStyle();
                if(Objects.isNull(cellStyle)){
                    continue;
                }
                // 仅生效匹配到的第一个整行样式
                if(StringUtils.isNotBlank(cellStyle.getRowBackgroundCondition())){
                    // 需要符合规范 GREEN<20:RED<BLACK 且必须是long
                    if(cellData.getData() instanceof Double){
                        String[] split = cellStyle.getRowBackgroundCondition().split("<");

                        if ((Double) cellData.getData() < Double.valueOf(split[1].split(":")[0])) {
                            background = split[0];
                        } else if ((Double) cellData.getData() > Double.valueOf(split[1].split(":")[0])) {
                            background = split[2];
                        } else {
                            background = split[1].split(":")[1];
                        }
                    }
                    break;
                }
            }

            // 对整行进行样式填充
            if(StringUtils.isNotBlank(background)){
                for (CellData cellData : rowData.getCellDataList()) {
                    CellStyle cellStyle = cellData.getCellStyle();
                    if (Objects.isNull(cellStyle)) {
                        cellData.setCellStyle(new CellStyle());
                        cellStyle = cellData.getCellStyle();
                    }
                    // 行样式优先级高于单元格，会覆盖单元格样式
                    cellStyle.setBackground(background);
                }
            }
        }
    }

    private static void setCellStyle(CellData cellData, HSSFCell cell, HSSFWorkbook wb) {
        CellStyle cellStyle = cellData.getCellStyle();
        if (Objects.isNull(cellStyle)) {
            return;
        }

        HSSFCellStyle hssfCellStyle = wb.createCellStyle();


        if(StringUtils.isNotBlank(cellStyle.getBackgroundCondition())){
            // 需要符合规范 GREEN<20:RED<BLACK 且必须是long
            if(cellData.getData() instanceof Double){
                String[] split = cellStyle.getBackgroundCondition().split("<");

                if ((Double) cellData.getData() < Double.valueOf(split[1].split(":")[0])) {
                    cellStyle.setBackground(split[0]);
                } else if ((Double) cellData.getData() > Double.valueOf(split[1].split(":")[0])) {
                    cellStyle.setBackground(split[2]);
                } else {
                    cellStyle.setBackground(split[1].split(":")[1]);
                }
                hssfCellStyle.setFillForegroundColor(cellStyle.getBackground());
                hssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
        }

        // 直接指定单元格背景色
        if(StringUtils.isNotBlank(cellStyle.getBackgroundStr())){
            hssfCellStyle.setFillForegroundColor(cellStyle.getBackground());
            hssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        // 设置列宽
        if (Objects.nonNull(cellStyle.getColWidth())) {
            wb.getSheetAt(0).setColumnWidth(cellData.getIndex(), cellStyle.getColWidth() * 256);
        }

        cell.setCellStyle(hssfCellStyle);
    }

    private static void setCellValue() {

    }

    @Autowired
    @Qualifier("easyExcelFileUtils")
    public void setFileUtils(FileUtils fileUtils) {
        CommonHandler.fileUtils = fileUtils;
    }
}

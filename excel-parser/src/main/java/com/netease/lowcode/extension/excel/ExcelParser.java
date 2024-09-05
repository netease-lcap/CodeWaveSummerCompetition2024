package com.netease.lowcode.extension.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.netease.lowcode.extension.excel.dto.ExcelParseError;
import com.netease.lowcode.extension.excel.dto.ExcelParseRect;
import com.netease.lowcode.extension.excel.dto.ExcelParseResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 扩展模块静态方法
 */
@Slf4j
public class ExcelParser {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    /**
     * 解析excel
     *
     * @param url            excel文件地址
     * @param rect
     * @param columnFieldMap 列映射:key表示列的索引,value表示映射的字段名称
     * @param row            表头的行数
     * @param clazz          映射类
     * @return
     * @param <T>
     */
    public static <T> ExcelParseResult<T> parseAllSheet(String url, ExcelParseRect rect, Map<String, String> columnFieldMap, Long row, Class<T> clazz) {
        return parseBySheetName(url, null, rect, columnFieldMap, row, clazz);
    }

    public static <T> ExcelParseResult<T> parseBySheetName(String url, List<String> sheetNames, ExcelParseRect rect,
                                                           Map<String, String> columnFieldMap, Long row, Class<T> clazz) {
        if (StringUtils.isBlank(url)) {
            ExcelParseResult errResult = new ExcelParseResult();
            ExcelParseError error = new ExcelParseError();
            error.setSheetName("");
            error.setErrorMsg("");
            error.setErrorMsg("非法的文件下载路径: " + url);
            errResult.setErrors(Arrays.asList(error));
            return errResult;
        }

        long start = System.currentTimeMillis();
        try (InputStream inputStream = openUrlStream(url)) {
            log.info("download file cost {}ms", System.currentTimeMillis() - start);
            return parseStreamBySheetName(inputStream, sheetNames, rect, clazz, columnFieldMap, row);
        } catch (IOException e) {
            throw new ExcelParseException("下载excel文件发生异常", e);
        }
    }

    public static <T> ExcelParseResult<T> parseBySheetIndex(String url, List<Integer> sheetIndexes, ExcelParseRect rect,
                                                            Map<String, String> columnFieldMap, Long row, Class<T> clazz) {
        if (StringUtils.isBlank(url)) {
            ExcelParseResult errResult = new ExcelParseResult();
            ExcelParseError error = new ExcelParseError();
            error.setSheetName("");
            error.setErrorMsg("");
            error.setErrorMsg("非法的文件下载路径: " + url);
            errResult.setErrors(Arrays.asList(error));
            return errResult;
        }

        long start = System.currentTimeMillis();
        try (InputStream inputStream = openUrlStream(url)) {
            log.info("download file cost {}ms", System.currentTimeMillis() - start);
            return parseStreamBySheetIndex(inputStream, sheetIndexes, rect, clazz, columnFieldMap, row);
        } catch (IOException e) {
            throw new ExcelParseException("下载excel文件发生异常", e);
        }
    }

    public static <T> ExcelParseResult<T> parseStreamBySheetName(InputStream inputStream, List<String> sheetNames, ExcelParseRect rect,
                                                                 Class<T> clazz, Map<String, String> columnFieldMap, Long row) {
        ExcelReaderBuilder excelReaderBuilder = EasyExcel.read(inputStream).extraRead(CellExtraTypeEnum.MERGE);
        DefaultReaderListener<T> easyExcelReaderListener = new DefaultReaderListener(clazz, rect, columnFieldMap);
        excelReaderBuilder.registerReadListener(easyExcelReaderListener);
        ExcelReader excelReader = excelReaderBuilder.headRowNumber((row == null || row <= 0) ? 1 : Integer.parseInt(String.valueOf(row))).build();

        List<ReadSheet> readSheets = excelReader.excelExecutor().sheetList();
        for (ReadSheet readSheet : readSheets) {
            // 不指定sheet的时候全部读取
            if (null == sheetNames || sheetNames.contains(readSheet.getSheetName())) {
                excelReader.read(readSheet);
            }
        }

        ExcelParseResult parseResult = new ExcelParseResult();
        parseResult.setData(easyExcelReaderListener.getData());
        parseResult.setErrors(easyExcelReaderListener.getParseErrors());
        parseResult.setSuccess(CollectionUtils.isEmpty(parseResult.getErrors()));
        parseResult.setUnParsedData(easyExcelReaderListener.getUnParsedData());
        return parseResult;
    }

    public static <T> ExcelParseResult<T> parseStreamBySheetIndex(InputStream inputStream, List<Integer> sheetIndexes,
                                                                  ExcelParseRect rect, Class<T> clazz, Map<String, String> columnFieldMap, Long row) {
        ExcelReaderBuilder excelReaderBuilder = EasyExcel.read(inputStream).extraRead(CellExtraTypeEnum.MERGE);
        DefaultReaderListener<T> easyExcelReaderListener = new DefaultReaderListener(clazz, rect, columnFieldMap);
        excelReaderBuilder.registerReadListener(easyExcelReaderListener);
        ExcelReader excelReader = excelReaderBuilder.headRowNumber((row == null || row <= 0) ? 1 : Integer.parseInt(String.valueOf(row))).build();

        List<ReadSheet> readSheets = excelReader.excelExecutor().sheetList();
        for (int i = 0; i < readSheets.size(); i++) {
            // sheetIndex从1开始
            if (null == sheetIndexes || sheetIndexes.contains(i + 1)) {
                excelReader.read(readSheets.get(i));
            }
        }

        ExcelParseResult parseResult = new ExcelParseResult();
        parseResult.setData(easyExcelReaderListener.getData());
        parseResult.setErrors(easyExcelReaderListener.getParseErrors());
        parseResult.setUnParsedData(easyExcelReaderListener.getUnParsedData());
        return parseResult;
    }

    /**
     * 下载文件
     *
     * @param url
     * @return
     */
    private static InputStream openUrlStream(String url) {
        try {
            Request request = new Request.Builder().url(getFileUrl(url)).get().build();
            Response response = okHttpClient.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new ExcelParseException("下载文件发生异常, code: " + response.code() + ", message: " + response.message());
            }

            return response.body().byteStream();
        } catch (IOException e) {
            throw new ExcelParseException("下载文件发生异常", e);
        }
    }

    /**
     * 获取文件下载路径，因为是请求本服务所以替换域名为localhost避免出现一些hostname unknown的情况
     * @param url
     * @return
     */
    private static String getFileUrl(String url) {
        int index = url.indexOf("/upload/");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int port = request.getLocalPort();

        if (index >= 0) {
            //制品中上传文件后的url
            url = "http://localhost:" + port + "/upload/" + url.substring(index + "/upload/".length());
        }

        log.info("文件下载路径{}", url);
        return url;
    }
}

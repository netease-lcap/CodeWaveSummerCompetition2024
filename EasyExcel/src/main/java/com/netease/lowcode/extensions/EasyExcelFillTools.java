package com.netease.lowcode.extensions;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson2.JSON;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.extensions.model.ComplexFillRequest;
import com.netease.lowcode.extensions.response.ExportBigDataResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

import static com.netease.lowcode.extensions.EasyExcelTools.openUrlStream;

@Component("libraryEasyExcelFillTools")
public class EasyExcelFillTools {
    private static final Logger log = LoggerFactory.getLogger(EasyExcelFillTools.class);

    private static FileUtils fileUtils;

    /**
     * 纵向单列表复杂填充(适合少量数据)
     *
     * @param request
     * @return
     */
    @NaslLogic
    public static ExportBigDataResponse complexFill(ComplexFillRequest request) {
        if (Objects.isNull(request)) {
            return ExportBigDataResponse.FAIL("请求参数为空");
        }
        if (StringUtils.isBlank(request.templateUrl)) {
            return ExportBigDataResponse.FAIL("模板url不能为空");
        }
        File exportFile = null;

        try {
            long start = System.currentTimeMillis();

            // 创建目录
            String path = String.join("/", "data", String.valueOf(System.currentTimeMillis()));
            File dir = new File(path);
            if (!dir.mkdirs()) {
                return ExportBigDataResponse.FAIL(String.format("创建目录失败:%s", path));
            }

            exportFile = new File(path, request.fileName);
            FileOutputStream fos = new FileOutputStream(exportFile);
            // 模板url下载
            InputStream inputStream = openUrlStream(request.templateUrl);
//            InputStream inputStream = new FileInputStream(request.templateUrl);
            ExcelWriter excelWriter = EasyExcel.write(fos).withTemplate(inputStream).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            // 填充简单数据
            if (StringUtils.isNotBlank(request.jsonData)) {
                // 数据解析为map
                Map<String, Object> map = JSON.parseObject(request.jsonData, Map.class);
                excelWriter.fill(map, writeSheet);
            }
            // 填充列表数据
            HashMap<String, List<String>> listJsonData = new HashMap<String, List<String>>();
            listJsonData.put("", request.listJsonData);
            fillListData(listJsonData, WriteDirectionEnum.VERTICAL, excelWriter, writeSheet);

            excelWriter.close();
            fos.flush();
            fos.close();

            // 文件上传
            UploadResponseDTO uploadResponseDTO = fileUtils.uploadFileV2(exportFile);

            return ExportBigDataResponse.OK(uploadResponseDTO.getFilePath(), uploadResponseDTO.getResult(), (double) (System.currentTimeMillis() - start) / 1000, (double) exportFile.length());
        } catch (Throwable throwable) {
            log.error("数据填充失败", throwable);
            return ExportBigDataResponse.FAIL(String.format("数据填充失败,msg=%s", throwable.getMessage()), Arrays.toString(throwable.getStackTrace()));
        } finally {
            // 删除文件
            if (Objects.nonNull(exportFile)) {
                FileUtils.delete(exportFile.getParentFile());
            }
        }

    }

    private static void fillListData(Map<String, List<String>> listJsonData, WriteDirectionEnum directionEnum, ExcelWriter excelWriter, WriteSheet writeSheet) {

        if (MapUtils.isEmpty(listJsonData)) {
            return;
        }

        /* 横向填充列表必须是最后一个数据：
           因为横向填充无法实现 .forceNewRow(Boolean.TRUE) 效果，只能靠设计模板来避免
        *  比如所有横向列表末尾的数据都拼接到第二次填充的数据中 */
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).direction(directionEnum).build();

        for (Map.Entry<String, List<String>> entry : listJsonData.entrySet()) {
            if (CollectionUtils.isEmpty(entry.getValue())) {
                continue;
            }

            // 数据转map
            List<Map<String, Object>> data = new ArrayList<>();
            for (String jsonData : entry.getValue()) {
                // 数据解析为map
                Map<String, Object> map = JSON.parseObject(jsonData, Map.class);
                data.add(map);
            }

            if (StringUtils.isBlank(entry.getKey())) {
                excelWriter.fill(data, fillConfig, writeSheet);
            } else {
                excelWriter.fill(new FillWrapper(entry.getKey(), data), fillConfig, writeSheet);
            }
        }
    }


    @Autowired
    @Qualifier("easyExcelFileUtils")
    public void setFileUtils(FileUtils fileUtils) {
        EasyExcelFillTools.fileUtils = fileUtils;
    }

}


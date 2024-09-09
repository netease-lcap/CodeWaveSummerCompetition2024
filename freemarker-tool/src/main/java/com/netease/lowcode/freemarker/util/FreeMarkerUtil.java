package com.netease.lowcode.freemarker.util;

import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.freemarker.dto.CreateDocxRequest;
import com.netease.lowcode.freemarker.dto.CreateRequest;
import com.netease.lowcode.freemarker.dto.DownloadResponseDTO;
import com.netease.lowcode.freemarker.dto.UploadResponseDTO;
import com.netease.lowcode.freemarker.validators.CreateDocxRequestValidator;
import com.netease.lowcode.freemarker.validators.CreateRequestValidator;
import com.spire.xls.FileFormat;
import com.spire.xls.Workbook;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FreeMarkerUtil {

    /**
     * 根据模板和数据创建指定后缀文件，并下载
     *
     * @return
     */
    @NaslLogic
    public static DownloadResponseDTO createFile(CreateRequest request) {


        try {
            CreateRequestValidator.validate(request);

            //2. 模板替换
            ByteArrayInputStream byteArrayInputStream = getFreemarkerContentInputStreamV2(request.jsonData, FileUtil.getTrueUrl(request.templateUrl));

            //3. 上传文件
            UploadResponseDTO outUrl = FileUtil.uploadStream(byteArrayInputStream, request.outFileName);

            if (Objects.nonNull(outUrl)) {

                return DownloadResponseDTO.OK(outUrl.getResult(), outUrl.getFilePath());
            } else {

                return DownloadResponseDTO.FAIL("文件上传失败","");
            }

        } catch (Throwable e) {

            return DownloadResponseDTO.FAIL("执行异常:"+e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * 根据模板创建excel
     *
     * @param request
     * @return
     */
    @NaslLogic
    public static DownloadResponseDTO createNewXlsx(CreateRequest request) {
        try {
            CreateRequestValidator.validate(request);

            //2. 模板替换
            ByteArrayInputStream byteArrayInputStream = getFreemarkerContentInputStreamV2(request.jsonData, FileUtil.getTrueUrl(request.templateUrl));

            //3. 上传文件
            //4. xml to excel
            Workbook workbook = new Workbook();
            workbook.loadFromXml(byteArrayInputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.saveToStream(outputStream, FileFormat.Version2016);

            //
            org.apache.poi.ss.usermodel.Workbook poiWorkbook = WorkbookFactory.create(new ByteArrayInputStream(outputStream.toByteArray()));
            poiWorkbook.removeSheetAt(poiWorkbook.getSheetIndex("Evaluation Warning"));
            ByteArrayOutputStream tmpOutputStream = new ByteArrayOutputStream();
            poiWorkbook.write(tmpOutputStream);

            UploadResponseDTO outUrl = FileUtil.uploadStream(new ByteArrayInputStream(tmpOutputStream.toByteArray()), request.outFileName);

            if (Objects.nonNull(outUrl)) {

                return DownloadResponseDTO.OK(outUrl.getResult(), outUrl.getFilePath());
            } else {

                return DownloadResponseDTO.FAIL("文件上传失败","");
            }

        } catch (Throwable e) {

            return DownloadResponseDTO.FAIL("执行异常:"+e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * 根据模板创建docx
     *
     * @param request
     * @return
     */
    @NaslLogic
    public static DownloadResponseDTO createNewDocxFile(CreateDocxRequest request) {

        Map<String,String> templateFileMap = new HashMap<>();
        try {
            CreateDocxRequestValidator.validate(request);

            // 图片转Base64
            Map<String, String> picMap = new HashMap<>();
            if (Objects.nonNull(request.imageMap) && !request.base64) {
                BASE64Encoder encoder = new BASE64Encoder();
                for (Map.Entry<String, String> entry : request.imageMap.entrySet()) {

                    InputStream inputStream = FileUtil.getFileInputStream(entry.getValue());
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        bos.write(buffer, 0, read);
                    }

                    picMap.put(entry.getKey(), encoder.encodeBuffer(bos.toByteArray()));
                    inputStream.close();
                    bos.close();
                }
            } else if (Objects.nonNull(request.imageMap)) {
                picMap.putAll(request.imageMap);
            }

            //1. 下载文件
            InputStream docxInputStream = FileUtil.getFileInputStream(request.templateDocxFileUrl);

            // 下载模板文件
            if (request.templateUrl != null) {
                for (Map.Entry<String, String> entry : request.templateUrl.entrySet()) {
                    templateFileMap.put(entry.getKey(),FileUtil.getTrueUrl(entry.getValue()));
                }
            }

            ByteArrayOutputStream outputStream = createDocx(request.jsonData,
                    picMap,
                    docxInputStream,
                    templateFileMap);

            // 上传文件
            UploadResponseDTO outUrl = FileUtil.uploadStream(new ByteArrayInputStream(outputStream.toByteArray()),request.outFileName);
            outputStream.close();
            if (Objects.nonNull(outUrl)) {

                return DownloadResponseDTO.OK(outUrl.getResult(), outUrl.getFilePath());
            } else {

                return DownloadResponseDTO.FAIL("文件上传失败","");
            }
        } catch (Throwable e) {
            return DownloadResponseDTO.FAIL("执行异常:" + e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    public static ByteArrayOutputStream createDocx(String jsonData,
                                                   Map<String, String> picMap,
                                                   InputStream docxInputSteam,
                                                   Map<String,String> templateFileMap) throws Exception {

        ZipOutputStream zipOut = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {

            Map<String, ByteArrayInputStream> templateFileStreamMap = new HashMap<>();
            for (Map.Entry<String, String> entry : templateFileMap.entrySet()) {
                templateFileStreamMap.put(entry.getKey(),getFreemarkerContentInputStreamV2(jsonData,entry.getValue()));
            }

            //最初设计的模板
            zipOut = new ZipOutputStream(outputStream);
            //开始覆盖文档, 不要删除原图片，存在无需替换的情况，因此全部保留
            writeZipFileV2(docxInputSteam,zipOut,templateFileStreamMap);
            //写入图片 ，可能存在用户命名与保留文件重复，客户图片名称前加特定标识，由用户控制
            writePicture(picMap, zipOut);

            return outputStream;
        } finally {
            if (zipOut != null){
                zipOut.close();
            }
        }
    }

    private static ByteArrayInputStream getFreemarkerContentInputStreamV2(String jsonData, String url) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setTemplateLoader(new URLTemplateLoader() {
            @Override
            protected URL getURL(String name) {
                try {
                    return new URL(url);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // 获取模板
        Template template = cfg.getTemplate("test");
        StringWriter swriter = new StringWriter();
        Object jsonNode = JsonUtil.fromJson(jsonData, Object.class);
        template.process(jsonNode, swriter);

        return new ByteArrayInputStream(swriter.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static void writeZipFileV2(InputStream zipInputStream,
                                       ZipOutputStream zipOut,Map<String, ByteArrayInputStream> templateFileStreamMap) throws IOException {

        int len;
        byte[] buffer = new byte[1024];

        ZipInputStream zis = new ZipInputStream(zipInputStream);
        ZipEntry next;
        while ((next = zis.getNextEntry()) != null) {

            zipOut.putNextEntry(new ZipEntry(next.getName()));

            if (templateFileStreamMap.containsKey(next.getName())) {
                ByteArrayInputStream byteArrayInputStream = templateFileStreamMap.get(next.getName());
                if (byteArrayInputStream != null) {
                    while ((len = byteArrayInputStream.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, len);
                    }
                    byteArrayInputStream.close();
                }
                continue;
            }
            while ((len = zis.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
            }
        }
        zipInputStream.close();

    }

    private static void writePicture(Map<String, String> picMap, ZipOutputStream zipout) throws IOException {
        int len;
        byte[] buffer = new byte[1024];
        BASE64Decoder decoder = new BASE64Decoder();
        for (Map.Entry<String, String> entry : picMap.entrySet()) {
            // 用户输入的图片名称可能会与原文件重复，这里交由使用方控制，图片名称前加个标识区分，防止覆盖
            ZipEntry next = new ZipEntry("word/media/"+entry.getKey());
            zipout.putNextEntry(new ZipEntry(next.toString()));
            byte[] bytes = decoder.decodeBuffer(entry.getValue());
            InputStream in  = new ByteArrayInputStream(bytes);
            while ((len = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, len);
            }
            in.close();
        }
    }
}

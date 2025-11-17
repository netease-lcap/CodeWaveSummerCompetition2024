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
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FreeMarkerUtil {

    private static final Pattern IMG_OPEN_TAG_PATTERN = Pattern.compile("(?i)<img\\b[^>]*?>");
    private static final Pattern IMG_CLOSE_TAG_PATTERN = Pattern.compile("(?i)</img>");
    private static final String CONTENT_TYPES_FILE = "[Content_Types].xml";
    private static final String CONTENT_TYPES_NS = "http://schemas.openxmlformats.org/package/2006/content-types";
    private static final Pattern AMPERSAND_PATTERN =
            Pattern.compile("&(?!(?:[A-Za-z]+|#[0-9]+|#x[0-9A-Fa-f]+);)");

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

            // 预处理 json，仅对 img 标签的尖括号转义，避免 Freemarker 把它视作真实节点
            String sanitizedJsonData = escapeImgTagBrackets(request.jsonData, request.processImgTag);

            // 图片转Base64
            Map<String, String> picMap = new HashMap<>();
            if (Objects.nonNull(request.imageMap) && !request.base64) {
                Base64.Encoder encoder = Base64.getEncoder();
                for (Map.Entry<String, String> entry : request.imageMap.entrySet()) {

                    InputStream inputStream = FileUtil.getFileInputStream(entry.getValue());
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        bos.write(buffer, 0, read);
                    }

                    picMap.put(entry.getKey(), encoder.encodeToString(bos.toByteArray()));
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

            // 是否开启富文本 img 转图片的增强逻辑，由调用方显式控制
            boolean enableRichTextImage = Boolean.TRUE.equals(request.processImgTag);
            ByteArrayOutputStream outputStream = createDocx(sanitizedJsonData,
                    picMap,
                    docxInputStream,
                    templateFileMap,
                    enableRichTextImage);
            // 将内容写入到文件
//            try (FileOutputStream fileOutputStream = new FileOutputStream("/data/aa.docx")) {
//                outputStream.writeTo(fileOutputStream);
//                System.out.println("文件写入成功");
//            }
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
                                                   Map<String,String> templateFileMap,
                                                   boolean enableRichTextImage) throws Exception {

        ZipOutputStream zipOut = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {

            byte[] templateDocBytes = toByteArray(docxInputSteam);
            if (docxInputSteam != null) {
                docxInputSteam.close();
            }
            // 根据开关确定模板渲染方式：开启时在渲染结果中查找 img 标签并做额外处理，关闭时保持原逻辑
            Map<String, byte[]> originalEntries = enableRichTextImage
                    ? extractZipEntries(templateDocBytes)
                    : Collections.emptyMap();
            Map<String, byte[]> processedTemplateBytes = enableRichTextImage
                    ? prepareTemplateFiles(jsonData, templateFileMap, picMap, originalEntries)
                    : Collections.emptyMap();
            Map<String, ByteArrayInputStream> templateFileStreamMap = enableRichTextImage
                    ? buildTemplateStreamMap(processedTemplateBytes)
                    : buildOriginalTemplateStreamMap(jsonData, templateFileMap);

            if (enableRichTextImage) {
                Set<String> imageExtensions = collectImageExtensions(picMap);
                if (!imageExtensions.isEmpty()) {
                    byte[] baseContentTypes = processedTemplateBytes.getOrDefault(CONTENT_TYPES_FILE,
                            originalEntries.get(CONTENT_TYPES_FILE));
                    byte[] updatedContentTypes = ensureImageContentTypes(baseContentTypes, imageExtensions);
                    if (updatedContentTypes != null) {
                        templateFileStreamMap.put(CONTENT_TYPES_FILE, new ByteArrayInputStream(updatedContentTypes));
                    }
                }
            }

            //最初设计的模板
            zipOut = new ZipOutputStream(outputStream);
            //开始覆盖文档, 不要删除原图片，存在无需替换的情况，因此全部保留
            writeZipFileV2(new ByteArrayInputStream(templateDocBytes),zipOut,templateFileStreamMap);
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
        Base64.Decoder decoder = Base64.getDecoder();
        for (Map.Entry<String, String> entry : picMap.entrySet()) {
            // 用户输入的图片名称可能会与原文件重复，这里交由使用方控制，图片名称前加个标识区分，防止覆盖
            ZipEntry next = new ZipEntry("word/media/"+entry.getKey());
            zipout.putNextEntry(new ZipEntry(next.toString()));
            byte[] bytes = decoder.decode(entry.getValue());
            InputStream in  = new ByteArrayInputStream(bytes);
            while ((len = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, len);
            }
            in.close();
        }
    }

    private static Map<String, ByteArrayInputStream> buildTemplateStreamMap(Map<String, byte[]> templateBytes) {
        Map<String, ByteArrayInputStream> result = new HashMap<>();
        for (Map.Entry<String, byte[]> entry : templateBytes.entrySet()) {
            result.put(entry.getKey(), new ByteArrayInputStream(entry.getValue()));
        }
        return result;
    }

    /**
     * 当未开启富文本增强时，仅负责把模板从远端下载并渲染为输入流即可。
     */
    private static Map<String, ByteArrayInputStream> buildOriginalTemplateStreamMap(String jsonData,
                                                                                    Map<String, String> templateFileMap) throws IOException, TemplateException {
        Map<String, ByteArrayInputStream> result = new HashMap<>();
        if (templateFileMap == null || templateFileMap.isEmpty()) {
            return result;
        }
        for (Map.Entry<String, String> entry : templateFileMap.entrySet()) {
            result.put(entry.getKey(), getFreemarkerContentInputStreamV2(jsonData, entry.getValue()));
        }
        return result;
    }

    /**
     * 渲染模板并扫描富文本 img，补齐 document.xml 与 rels 的引用关系。
     */
    private static Map<String, byte[]> prepareTemplateFiles(String jsonData,
                                                            Map<String, String> templateFileMap,
                                                            Map<String, String> picMap,
                                                            Map<String, byte[]> originalEntries) throws Exception {
        Map<String, byte[]> templateBytesMap = new HashMap<>();
        if (templateFileMap == null || templateFileMap.isEmpty()) {
            return templateBytesMap;
        }
        AtomicInteger imageSequence = new AtomicInteger(1);
        AtomicInteger docPrSequence = new AtomicInteger(1000);
        Map<String, List<RichTextImageHandler.RelationshipInfo>> relationshipsByPart = new HashMap<>();

        for (Map.Entry<String, String> entry : templateFileMap.entrySet()) {
            ByteArrayInputStream templateInputStream = getFreemarkerContentInputStreamV2(jsonData, entry.getValue());
            byte[] templateBytes = toByteArray(templateInputStream);
            templateInputStream.close();

            if (RichTextImageHandler.shouldProcess(entry.getKey())) {
                RichTextImageHandler.Result result = RichTextImageHandler.processPart(entry.getKey(),
                        new String(templateBytes, StandardCharsets.UTF_8),
                        picMap,
                        imageSequence,
                        docPrSequence);
                templateBytes = result.getXml().getBytes(StandardCharsets.UTF_8);
                if (!result.getRelationships().isEmpty()) {
                    relationshipsByPart.put(entry.getKey(), result.getRelationships());
                }
            }

            templateBytesMap.put(entry.getKey(), templateBytes);
        }

        for (Map.Entry<String, List<RichTextImageHandler.RelationshipInfo>> entry : relationshipsByPart.entrySet()) {
            String relationshipPath = RichTextImageHandler.toRelationshipPath(entry.getKey());
            byte[] relBytes = templateBytesMap.containsKey(relationshipPath)
                    ? templateBytesMap.get(relationshipPath)
                    : originalEntries.get(relationshipPath);
            String updated = RichTextImageHandler.appendRelationships(relBytes, entry.getValue());
            templateBytesMap.put(relationshipPath, updated.getBytes(StandardCharsets.UTF_8));
        }
        return templateBytesMap;
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return new byte[0];
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        return bos.toByteArray();
    }

    private static Map<String, byte[]> extractZipEntries(byte[] zipBytes) throws IOException {
        Map<String, byte[]> entries = new HashMap<>();
        if (zipBytes == null || zipBytes.length == 0) {
            return entries;
        }
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int len;
                while ((len = zis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                entries.put(entry.getName(), bos.toByteArray());
            }
        }
        return entries;
    }

    private static String escapeImgTagBrackets(String jsonData, Boolean processImgTag) {
        if (!Boolean.TRUE.equals(processImgTag) || StringUtils.isBlank(jsonData)) {
            return jsonData;
        }
        String ampersandEscaped = escapeBareAmpersands(jsonData);
        String interim = escapeByPattern(ampersandEscaped, IMG_OPEN_TAG_PATTERN);
        return escapeByPattern(interim, IMG_CLOSE_TAG_PATTERN);
    }

    private static String escapeByPattern(String source, Pattern pattern) {
        Matcher matcher = pattern.matcher(source);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String escaped = matcher.group().replace("<", "&lt;").replace(">", "&gt;");
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(escaped));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static String escapeBareAmpersands(String source) {
        if (StringUtils.isBlank(source)) {
            return source;
        }
        Matcher matcher = AMPERSAND_PATTERN.matcher(source);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, "&amp;");
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private static Set<String> collectImageExtensions(Map<String, String> picMap) {
        Set<String> extensions = new HashSet<>();
        if (picMap == null || picMap.isEmpty()) {
            return extensions;
        }
        for (String name : picMap.keySet()) {
            if (StringUtils.isBlank(name)) {
                continue;
            }
            int idx = name.lastIndexOf('.');
            if (idx == -1 || idx == name.length() - 1) {
                continue;
            }
            String ext = name.substring(idx + 1).toLowerCase(Locale.ROOT);
            if (ext.matches("[a-z0-9]+")) {
                extensions.add(ext);
            }
        }
        return extensions;
    }

    private static byte[] ensureImageContentTypes(byte[] existingContentTypes,
                                                  Set<String> imageExtensions) throws Exception {
        if (imageExtensions == null || imageExtensions.isEmpty()) {
            return null;
        }

        Document document = parseContentTypesDocument(existingContentTypes);
        Element root = document.getDocumentElement();
        if (root == null || !"Types".equals(root.getLocalName())) {
            document = createEmptyContentTypesDocument();
            root = document.getDocumentElement();
        }

        Set<String> existingExtensions = new HashSet<>();
        NodeList defaults = root.getElementsByTagNameNS(CONTENT_TYPES_NS, "Default");
        for (int i = 0; i < defaults.getLength(); i++) {
            Element element = (Element) defaults.item(i);
            existingExtensions.add(element.getAttribute("Extension").toLowerCase(Locale.ROOT));
        }

        boolean modified = false;
        for (String extension : imageExtensions) {
            if (existingExtensions.contains(extension)) {
                continue;
            }
            Element defaultElement = document.createElementNS(CONTENT_TYPES_NS, "Default");
            defaultElement.setAttribute("Extension", extension);
            defaultElement.setAttribute("ContentType", guessImageContentType(extension));
            root.appendChild(defaultElement);
            existingExtensions.add(extension);
            modified = true;
        }

        return modified ? transformDocumentToBytes(document) : null;
    }

    private static Document parseContentTypesDocument(byte[] content) throws Exception {
        if (content == null || content.length == 0) {
            return createEmptyContentTypesDocument();
        }
        DocumentBuilder builder = buildSecureDocumentBuilder();
        try (InputStream inputStream = new ByteArrayInputStream(content)) {
            return builder.parse(inputStream);
        }
    }

    private static Document createEmptyContentTypesDocument() throws ParserConfigurationException {
        DocumentBuilder builder = buildSecureDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElementNS(CONTENT_TYPES_NS, "Types");
        document.appendChild(root);
        return document;
    }

    private static DocumentBuilder buildSecureDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        return factory.newDocumentBuilder();
    }

    private static byte[] transformDocumentToBytes(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(document), new StreamResult(outputStream));
        return outputStream.toByteArray();
    }

    private static String guessImageContentType(String extension) {
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "tif":
            case "tiff":
                return "image/tiff";
            case "webp":
                return "image/webp";
            default:
                return "image/" + extension;
        }
    }
}

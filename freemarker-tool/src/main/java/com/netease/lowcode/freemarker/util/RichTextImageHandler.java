package com.netease.lowcode.freemarker.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.imageio.ImageIO;
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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 将富文本中的 &lt;img&gt; 标签转换为 Word 文档中的图片节点，并维护关系文件。
 */
public class RichTextImageHandler {

    private static final Pattern IMG_PATTERN = Pattern.compile("(?i)<img\\b[^>]*>");
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("(?i)([A-Za-z_:][\\w:.-]*)\\s*=\\s*(\"([^\"]*)\"|'([^']*)')");
    private static final double PT_TO_PX = 96D / 72D;
    private static final long EMUS_PER_PIXEL = 9525L;

    private static final String WORD_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";
    private static final String WP_NS = "http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing";
    private static final String PIC_NS = "http://schemas.openxmlformats.org/drawingml/2006/picture";
    private static final String A_NS = "http://schemas.openxmlformats.org/drawingml/2006/main";
    private static final String R_NS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";
    private static final String REL_NS = "http://schemas.openxmlformats.org/package/2006/relationships";
    private static final String REL_IMAGE_TYPE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image";
    private static final String XML_NS = "http://www.w3.org/XML/1998/namespace";
    private static final long DEFAULT_MAX_IMAGE_WIDTH_EMU = 5_943_600L; // ≈6.5in usable page width

    private RichTextImageHandler() {
    }

    public static boolean shouldProcess(String entryName) {
        if (StringUtils.isBlank(entryName)) {
            return false;
        }
        return entryName.startsWith("word/")
                && entryName.endsWith(".xml")
                && !entryName.contains("/_rels/");
    }

    public static String toRelationshipPath(String partName) {
        if (StringUtils.isBlank(partName)) {
            return "";
        }
        int idx = partName.lastIndexOf('/');
        String dir = idx == -1 ? "" : partName.substring(0, idx);
        String file = idx == -1 ? partName : partName.substring(idx + 1);
        String relDir = dir.isEmpty() ? "_rels" : dir + "/_rels";
        return relDir + "/" + file + ".rels";
    }

    public static Result processPart(String partName,
                                     String xmlContent,
                                     Map<String, String> picMap,
                                     AtomicInteger imageSequence,
                                     AtomicInteger docPrSequence) throws Exception {

        Objects.requireNonNull(picMap, "picMap must not be null");

        if (StringUtils.isBlank(xmlContent)
                || (!xmlContent.contains("<img") && !xmlContent.contains("&lt;img"))) {
            return new Result(xmlContent, new ArrayList<>(), false);
        }

        Document document = parseXml(xmlContent);
        List<Element> textNodes = collectTextNodes(document);
        List<RelationshipInfo> relationships = new ArrayList<>();
        boolean modified = false;

        for (Element textElement : textNodes) {
            String text = textElement.getTextContent();
            if (StringUtils.isBlank(text) || !containsImage(text)) {
                continue;
            }

            Element runElement = (Element) textElement.getParentNode();
            if (runElement == null) {
                continue;
            }
            Node container = runElement.getParentNode();
            if (container == null) {
                continue;
            }

            // 将同一个文本节点拆成文字片段和图片片段，后续逐个构造 w:r
            List<Segment> segments = splitSegments(text, picMap, imageSequence, docPrSequence, relationships);
            if (segments.isEmpty()) {
                continue;
            }

            for (Node newRun : buildRuns(document, runElement, segments)) {
                container.insertBefore(newRun, runElement);
            }
            container.removeChild(runElement);
            modified = true;
        }

        if (!modified) {
            return new Result(xmlContent, new ArrayList<>(), false);
        }

        return new Result(transformToString(document), relationships, true);
    }

    public static String appendRelationships(byte[] relContent,
                                             List<RelationshipInfo> relationships) throws Exception {
        if (relationships == null || relationships.isEmpty()) {
            return relContent == null ? "" : new String(relContent, StandardCharsets.UTF_8);
        }

        Document document;
        if (relContent == null || relContent.length == 0) {
            document = createEmptyRelationshipsDocument();
        } else {
            document = parseXml(new String(relContent, StandardCharsets.UTF_8));
        }

        Element root = document.getDocumentElement();
        if (root == null || !"Relationships".equals(root.getLocalName())) {
            document = createEmptyRelationshipsDocument();
            root = document.getDocumentElement();
        }

        Set<String> existingIds = collectRelationshipIds(root);
        for (RelationshipInfo info : relationships) {
            if (existingIds.contains(info.getId())) {
                // Relationship id already exists, skip to avoid inconsistencies.
                continue;
            }
            Element relationship = document.createElementNS(REL_NS, "Relationship");
            relationship.setAttribute("Id", info.getId());
            relationship.setAttribute("Type", info.getType());
            relationship.setAttribute("Target", info.getTarget());
            root.appendChild(relationship);
        }
        return transformToString(document);
    }

    private static List<Element> collectTextNodes(Document document) {
        List<Element> result = new ArrayList<>();
        NodeList nodeList = document.getElementsByTagNameNS(WORD_NS, "t");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                result.add((Element) node);
            }
        }
        return result;
    }

    private static boolean containsImage(String text) {
        return IMG_PATTERN.matcher(text).find();
    }

    private static List<Segment> splitSegments(String text,
                                               Map<String, String> picMap,
                                               AtomicInteger imageSequence,
                                               AtomicInteger docPrSequence,
                                               List<RelationshipInfo> relationships) {
        List<Segment> segments = new ArrayList<>();
        Matcher matcher = IMG_PATTERN.matcher(text);
        int lastEnd = 0;
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                segments.add(Segment.text(text.substring(lastEnd, matcher.start())));
            }
            String tag = matcher.group();
            ImageInfo imageInfo = buildImageInfo(tag, picMap, imageSequence, docPrSequence);
            if (imageInfo == null) {
                segments.add(Segment.text(tag));
            } else {
                segments.add(Segment.image(imageInfo));
                relationships.add(new RelationshipInfo(imageInfo.relationshipId, REL_IMAGE_TYPE, "media/" + imageInfo.fileName));
            }
            lastEnd = matcher.end();
        }
        if (lastEnd < text.length()) {
            segments.add(Segment.text(text.substring(lastEnd)));
        }
        return segments;
    }

    private static List<Node> buildRuns(Document document, Element templateRun, List<Segment> segments) {
        List<Node> runs = new ArrayList<>();
        for (Segment segment : segments) {

            if (segment.type == SegmentType.TEXT) {
                if (StringUtils.isEmpty(segment.text)) {
                    continue;
                }
                runs.add(createTextRun(document, templateRun, segment.text));
                continue;
            }

            if (segment.imageInfo != null) {
                Node imageRun = createImageRun(document, templateRun, segment.imageInfo);
                if (imageRun != null) {
                    runs.add(imageRun);
                }
            }
        }
        return runs;
    }

    private static Element createTextRun(Document document, Element templateRun, String value) {
        Element run = (Element) templateRun.cloneNode(false);
        copyRunProperties(document, templateRun, run);
        Element textElement = document.createElementNS(WORD_NS, "w:t");
        if (needsPreserve(value)) {
            textElement.setAttributeNS(XML_NS, "xml:space", "preserve");
        }
        textElement.setTextContent(value);
        run.appendChild(textElement);
        return run;
    }

    private static boolean needsPreserve(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return Character.isWhitespace(value.charAt(0)) || Character.isWhitespace(value.charAt(value.length() - 1));
    }

    private static Node createImageRun(Document document, Element templateRun, ImageInfo imageInfo) {
        Element run = (Element) templateRun.cloneNode(false);
        copyRunProperties(document, templateRun, run);

        Element drawing = document.createElementNS(WORD_NS, "w:drawing");
        Element inline = document.createElementNS(WP_NS, "wp:inline");
        inline.setAttribute("distT", "0");
        inline.setAttribute("distB", "0");
        inline.setAttribute("distL", "0");
        inline.setAttribute("distR", "0");

        Element extent = document.createElementNS(WP_NS, "wp:extent");
        extent.setAttribute("cx", String.valueOf(imageInfo.cx));
        extent.setAttribute("cy", String.valueOf(imageInfo.cy));
        inline.appendChild(extent);

        Element effectExtent = document.createElementNS(WP_NS, "wp:effectExtent");
        effectExtent.setAttribute("l", "0");
        effectExtent.setAttribute("t", "0");
        effectExtent.setAttribute("r", "0");
        effectExtent.setAttribute("b", "0");
        inline.appendChild(effectExtent);

        Element docPr = document.createElementNS(WP_NS, "wp:docPr");
        docPr.setAttribute("id", String.valueOf(imageInfo.docPrId));
        docPr.setAttribute("name", "Picture " + imageInfo.docPrId);
        if (StringUtils.isNotBlank(imageInfo.alt)) {
            docPr.setAttribute("descr", imageInfo.alt);
        }
        inline.appendChild(docPr);

        Element cNvGraphicFramePr = document.createElementNS(WP_NS, "wp:cNvGraphicFramePr");
        Element graphicFrameLocks = document.createElementNS(A_NS, "a:graphicFrameLocks");
        graphicFrameLocks.setAttribute("noChangeAspect", "1");
        cNvGraphicFramePr.appendChild(graphicFrameLocks);
        inline.appendChild(cNvGraphicFramePr);

        Element graphic = document.createElementNS(A_NS, "a:graphic");
        Element graphicData = document.createElementNS(A_NS, "a:graphicData");
        graphicData.setAttribute("uri", "http://schemas.openxmlformats.org/drawingml/2006/picture");

        Element pic = document.createElementNS(PIC_NS, "pic:pic");
        Element nvPicPr = document.createElementNS(PIC_NS, "pic:nvPicPr");
        Element cNvPr = document.createElementNS(PIC_NS, "pic:cNvPr");
        cNvPr.setAttribute("id", String.valueOf(imageInfo.docPrId));
        cNvPr.setAttribute("name", imageInfo.fileName);
        nvPicPr.appendChild(cNvPr);
        nvPicPr.appendChild(document.createElementNS(PIC_NS, "pic:cNvPicPr"));
        pic.appendChild(nvPicPr);

        Element blipFill = document.createElementNS(PIC_NS, "pic:blipFill");
        Element blip = document.createElementNS(A_NS, "a:blip");
        blip.setAttributeNS(R_NS, "r:embed", imageInfo.relationshipId);
        blipFill.appendChild(blip);
        Element stretch = document.createElementNS(A_NS, "a:stretch");
        stretch.appendChild(document.createElementNS(A_NS, "a:fillRect"));
        blipFill.appendChild(stretch);
        pic.appendChild(blipFill);

        Element spPr = document.createElementNS(PIC_NS, "pic:spPr");
        Element xfrm = document.createElementNS(A_NS, "a:xfrm");
        Element off = document.createElementNS(A_NS, "a:off");
        off.setAttribute("x", "0");
        off.setAttribute("y", "0");
        xfrm.appendChild(off);
        Element ext = document.createElementNS(A_NS, "a:ext");
        ext.setAttribute("cx", String.valueOf(imageInfo.cx));
        ext.setAttribute("cy", String.valueOf(imageInfo.cy));
        xfrm.appendChild(ext);
        spPr.appendChild(xfrm);
        Element prstGeom = document.createElementNS(A_NS, "a:prstGeom");
        prstGeom.setAttribute("prst", "rect");
        prstGeom.appendChild(document.createElementNS(A_NS, "a:avLst"));
        spPr.appendChild(prstGeom);
        pic.appendChild(spPr);

        graphicData.appendChild(pic);
        graphic.appendChild(graphicData);
        inline.appendChild(graphic);
        drawing.appendChild(inline);
        run.appendChild(drawing);
        return run;
    }

    private static void copyRunProperties(Document document, Element source, Element target) {
        NodeList childNodes = source.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof Element) {
                Element childElement = (Element) child;
                if ("rPr".equals(childElement.getLocalName())) {
                    target.appendChild(childElement.cloneNode(true));
                }
            }
        }
    }

    private static ImageInfo buildImageInfo(String tag,
                                            Map<String, String> picMap,
                                            AtomicInteger imageSequence,
                                            AtomicInteger docPrSequence) {

        Map<String, String> attributes = parseAttributes(tag);
        String src = attributes.get("src");
        if (StringUtils.isBlank(src)) {
            return null;
        }

        ImageBinary imageBinary = readImage(src.trim());
        if (imageBinary == null || imageBinary.bytes == null || imageBinary.bytes.length == 0) {
            return null;
        }

        Dimension dimension = resolveDimension(attributes, imageBinary);
        if (dimension == null) {
            return null;
        }

        String extension = determineExtension(imageBinary, src);
        String fileName = generateUniqueFileName(picMap, extension, imageSequence);
        // 将下载/解析后的图片重新塞回 picMap，这样主流程会把它写入 word/media
        picMap.put(fileName, Base64.getEncoder().encodeToString(imageBinary.bytes));

        ImageInfo imageInfo = new ImageInfo();
        imageInfo.fileName = fileName;
        imageInfo.relationshipId = "rIdRichText" + UUID.randomUUID().toString().replace("-", "");
        imageInfo.cx = dimension.widthEmu;
        imageInfo.cy = dimension.heightEmu;
        imageInfo.docPrId = docPrSequence.getAndIncrement();
        imageInfo.alt = attributes.getOrDefault("alt", "");
        return imageInfo;
    }

    private static String generateUniqueFileName(Map<String, String> picMap,
                                                 String extension,
                                                 AtomicInteger imageSequence) {
        String ext = StringUtils.isBlank(extension) ? "png" : extension;
        String fileName;
        do {
            fileName = "rich_text_inline_" + imageSequence.getAndIncrement() + "_"
                    + UUID.randomUUID().toString().replace("-", "") + "." + ext;
        } while (picMap.containsKey(fileName));
        return fileName;
    }

    private static Dimension resolveDimension(Map<String, String> attributes, ImageBinary imageBinary) {
        Integer width = extractDimension(attributes, "width");
        Integer height = extractDimension(attributes, "height");

        if (imageBinary.widthPx > 0 && imageBinary.heightPx > 0) {
            if (width != null && height == null) {
                height = Math.max(1, (int) Math.round((double) width / imageBinary.widthPx * imageBinary.heightPx));
            } else if (height != null && width == null) {
                width = Math.max(1, (int) Math.round((double) height / imageBinary.heightPx * imageBinary.widthPx));
            } else if (width == null) {
                width = imageBinary.widthPx;
                height = imageBinary.heightPx;
            }
        }

        if (width == null || height == null) {
            return null;
        }

        Dimension dimension = new Dimension();
        dimension.widthEmu = pixelsToEmu(width);
        dimension.heightEmu = pixelsToEmu(height);
        enforceMaxWidth(dimension);
        return dimension;
    }

    private static Integer extractDimension(Map<String, String> attributes, String key) {
        String direct = attributes.get(key);
        Integer value = parsePixels(direct);
        if (value != null) {
            return value;
        }
        String style = attributes.get("style");
        if (StringUtils.isBlank(style)) {
            return null;
        }
        for (String token : style.split(";")) {
            String[] pair = token.split(":");
            if (pair.length != 2) {
                continue;
            }
            String styleKey = pair[0].trim();
            String styleValue = pair[1].trim();
            if (key.equalsIgnoreCase(styleKey)) {
                Integer parsed = parsePixels(styleValue);
                if (parsed != null) {
                    return parsed;
                }
            }
        }
        return null;
    }

    private static Integer parsePixels(String raw) {
        if (StringUtils.isBlank(raw)) {
            return null;
        }
        String value = raw.trim().toLowerCase(Locale.ROOT);
        try {
            if (value.endsWith("px")) {
                value = value.substring(0, value.length() - 2);
                return (int) Math.round(Double.parseDouble(value));
            }
            if (value.endsWith("pt")) {
                double pt = Double.parseDouble(value.substring(0, value.length() - 2));
                return (int) Math.round(pt * PT_TO_PX);
            }
            if (!Character.isDigit(value.charAt(value.length() - 1))) {
                return null;
            }
            return (int) Math.round(Double.parseDouble(value));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static ImageBinary readImage(String src) {
        try {
            if (src.startsWith("data:") && src.contains("base64,")) {
                String base64 = src.substring(src.indexOf("base64,") + 7);
                byte[] bytes = Base64.getDecoder().decode(base64);
                return buildImageBinary(bytes, extractMimeFromDataUrl(src));
            }
            try (InputStream inputStream = FileUtil.getFileInputStream(src)) {
                byte[] bytes = readAllBytes(inputStream);
                return buildImageBinary(bytes, null);
            }
        } catch (IOException ex) {
//            LOGGER.warn("Failed to load image for rich text: {}", src, ex);
            return null;
        }
    }

    private static ImageBinary buildImageBinary(byte[] bytes, String mime) throws IOException {
        ImageBinary binary = new ImageBinary();
        binary.bytes = bytes;
        binary.mimeType = mime;
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
            BufferedImage image = ImageIO.read(bais);
            if (image != null) {
                binary.widthPx = image.getWidth();
                binary.heightPx = image.getHeight();
            }
        }
        return binary;
    }

    private static String extractMimeFromDataUrl(String src) {
        int start = src.indexOf(':');
        int end = src.indexOf(';');
        if (start >= 0 && end > start) {
            return src.substring(start + 1, end);
        }
        return null;
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        return bos.toByteArray();
    }

    private static long pixelsToEmu(int pixels) {
        return Math.max(1, Math.round(pixels * EMUS_PER_PIXEL));
    }

    private static String determineExtension(ImageBinary imageBinary, String src) {
        if (imageBinary != null && StringUtils.isNotBlank(imageBinary.mimeType)) {
            String mime = imageBinary.mimeType.toLowerCase(Locale.ROOT);
            if (mime.contains("png")) {
                return "png";
            }
            if (mime.contains("jpeg") || mime.contains("jpg")) {
                return "jpg";
            }
            if (mime.contains("gif")) {
                return "gif";
            }
        }
        if (StringUtils.isNotBlank(src)) {
            String sanitized = src.split("\\?")[0];
            int idx = sanitized.lastIndexOf('.');
            if (idx != -1 && idx < sanitized.length() - 1) {
                String ext = sanitized.substring(idx + 1).toLowerCase(Locale.ROOT);
                if (ext.matches("[a-z]{2,4}")) {
                    return ext;
                }
            }
        }
        return "png";
    }

    private static void enforceMaxWidth(Dimension dimension) {
        if (dimension == null || dimension.widthEmu <= DEFAULT_MAX_IMAGE_WIDTH_EMU) {
            return;
        }
        double scale = (double) DEFAULT_MAX_IMAGE_WIDTH_EMU / (double) dimension.widthEmu;
        dimension.widthEmu = DEFAULT_MAX_IMAGE_WIDTH_EMU;
        dimension.heightEmu = Math.max(1L, Math.round(dimension.heightEmu * scale));
    }

    private static Map<String, String> parseAttributes(String tag) {
        Map<String, String> attributes = new HashMap<>();
        Matcher matcher = ATTRIBUTE_PATTERN.matcher(tag);
        while (matcher.find()) {
            String name = matcher.group(1).toLowerCase(Locale.ROOT);
            String value = matcher.group(3);
            if (value == null) {
                value = matcher.group(4);
            }
            if (value != null) {
                attributes.put(name, value);
            }
        }
        return attributes;
    }

    private static Document parseXml(String xml) throws Exception {
        DocumentBuilder builder = newDocumentBuilder();
        try (StringReader reader = new StringReader(xml)) {
            InputSource source = new InputSource(reader);
            return builder.parse(source);
        }
    }

    private static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        disableExternalEntities(factory);
        return factory.newDocumentBuilder();
    }

    private static void disableExternalEntities(DocumentBuilderFactory factory) throws ParserConfigurationException {
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
    }

    private static Document createEmptyRelationshipsDocument() throws ParserConfigurationException {
        DocumentBuilder builder = newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElementNS(REL_NS, "Relationships");
        document.appendChild(root);
        return document;
    }

    private static Set<String> collectRelationshipIds(Element root) {
        Set<String> ids = new HashSet<>();
        NodeList nodes = root.getElementsByTagNameNS(REL_NS, "Relationship");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                ids.add(element.getAttribute("Id"));
            }
        }
        return ids;
    }

    private static String transformToString(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }

    public static class RelationshipInfo {
        private final String id;
        private final String type;
        private final String target;

        public RelationshipInfo(String id, String type, String target) {
            this.id = id;
            this.type = type;
            this.target = target;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getTarget() {
            return target;
        }
    }

    public static class Result {
        private final String xml;
        private final List<RelationshipInfo> relationships;
        private final boolean modified;

        public Result(String xml, List<RelationshipInfo> relationships, boolean modified) {
            this.xml = xml;
            this.relationships = relationships;
            this.modified = modified;
        }

        public String getXml() {
            return xml;
        }

        public List<RelationshipInfo> getRelationships() {
            return relationships;
        }

        public boolean isModified() {
            return modified;
        }
    }

    private enum SegmentType {
        TEXT, IMAGE
    }

    private static class Segment {
        SegmentType type;
        String text;
        ImageInfo imageInfo;

        static Segment text(String value) {
            Segment segment = new Segment();
            segment.type = SegmentType.TEXT;
            segment.text = value;
            return segment;
        }

        static Segment image(ImageInfo info) {
            Segment segment = new Segment();
            segment.type = SegmentType.IMAGE;
            segment.imageInfo = info;
            return segment;
        }
    }

    private static class ImageInfo {
        String relationshipId;
        String fileName;
        long cx;
        long cy;
        int docPrId;
        String alt;
    }

    private static class ImageBinary {
        byte[] bytes;
        String mimeType;
        int widthPx;
        int heightPx;
    }

    private static class Dimension {
        long widthEmu;
        long heightEmu;
    }
}

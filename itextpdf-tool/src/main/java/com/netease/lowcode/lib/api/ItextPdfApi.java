package com.netease.lowcode.lib.api;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.netease.lowcode.core.annotation.NaslLogic;
import com.netease.lowcode.lib.file.FileConnectorUtils;
import com.netease.lowcode.lib.file.UploadResponseDTO;
import com.netease.lowcode.lib.itextpdf.BaseFontBuilder;
import com.netease.lowcode.lib.itextpdf.PdfBuilderUtil;
import com.netease.lowcode.lib.itextpdf.PdfDocumentBuilder;
import com.netease.lowcode.lib.structure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItextPdfApi {

    //参数使用LCAP_EXTENSION_LOGGER后日志会显示在平台日志功能中
    private static final Logger log = LoggerFactory.getLogger("LCAP_EXTENSION_LOGGER");

    private String tempDirectory = "./itextpdf";
    private Map<String, PdfDocumentBuilder> pdfDocumentBuilderMap = new HashMap<>();
    private Map<String, Object> elementMap = new HashMap<>();
    @Resource
    private FileConnectorUtils fileConnectorUtils;

    /**
     * 合并pdf，并上传到nos
     *
     * @param inputFiles              原文件地址集合
     * @param targetFileName          目标文件名，文件名称不可重复
     * @param isDeleteOriginLocalFile 是否删除本地原文件
     * @return nos文件访问信息
     */
    @NaslLogic
    public UploadResponseDTO mergePdfToNosFileURl(List<String> inputFiles, String targetFileName, Boolean isDeleteOriginLocalFile) {
        String localFilePath = mergePdf(inputFiles, targetFileName, isDeleteOriginLocalFile);
        String[] path = localFilePath.split(File.separator);
        String fileName = path[path.length - 1];
        try {
            FileInputStream fis = new FileInputStream(localFilePath);
            UploadResponseDTO res = fileConnectorUtils.fileUploadV2(fis, fileName, new HashMap<>());
            return res;
        } catch (Exception e) {
            log.error("close document error", e);
            throw new IllegalArgumentException("close document error");
        } finally {
            try {
                //删除本地文件
                Files.delete(Paths.get(localFilePath));
                String directoryPath = localFilePath.replace(fileName, "");
                Files.delete(Paths.get(directoryPath));
            } catch (IOException e) {
                log.error("删除失败", e);
            }
        }
    }

    /**
     * 获取inputFiles的pdf文件，并下载到本地
     *
     * @param inputFiles 原文件地址集合
     * @return inputFiles的pdf文件
     */
    private List<String> getInputFilesLocal(List<String> inputFiles) throws IOException {
        //inputFiles去重
        inputFiles = inputFiles.stream().distinct().collect(Collectors.toList());
        List<String> inputFilesFinal = new ArrayList<>();
        String key = "document-" + UUID.randomUUID();
        //创建目录
        String tempDirectoryFinal = tempDirectory + File.separator + key + File.separator;
        File file = new File(tempDirectoryFinal);
        file.mkdirs();
        for (String input : inputFiles) {
            String targetFileName = "tomergepdf-" + UUID.randomUUID() + ".pdf";
            String targetFileNamePath = tempDirectoryFinal + targetFileName;
            if (input.startsWith("http://") || input.startsWith("https://")) {
                InputStream inputStream = fileConnectorUtils.fileDownloadV2(input);
                // 使用try-with-resources确保流关闭
                try (OutputStream outputStream = new FileOutputStream(targetFileNamePath)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                inputFilesFinal.add(targetFileNamePath);
            } else {
                inputFilesFinal.add(input);
            }
        }
        return inputFilesFinal;
    }

    /**
     * 合并pdf
     *
     * @param inputFiles              原文件地址集合
     * @param targetFileName          目标文件名
     * @param isDeleteOriginLocalFile 是否删除本地原文件
     * @return 目标文件本地路径
     */
    @NaslLogic
    public String mergePdf(List<String> inputFiles, String targetFileName, Boolean isDeleteOriginLocalFile) {
        String key = "merge-document-" + UUID.randomUUID();
        //创建目录
        String tempDirectoryFinal = tempDirectory + File.separator + key + File.separator;
        String targetFileNamePath = tempDirectoryFinal + targetFileName;
        File file = new File(tempDirectoryFinal);
        file.mkdirs();
        Document document = new Document();
        PdfCopy copy;
        List<PdfReader> readers = new ArrayList<>();
        List<String> inputFilesFinal = new ArrayList<>();
        try {
            inputFilesFinal = getInputFilesLocal(inputFiles);
            copy = new PdfSmartCopy(document, Files.newOutputStream(Paths.get(targetFileNamePath)));
            document.open();
            for (String input : inputFilesFinal) {
                PdfReader reader = new PdfReader(new RandomAccessFileOrArray(input), null);
                readers.add(reader);
                copy.addDocument(reader);
            }
            return targetFileNamePath;
        } catch (Exception e) {
            log.error("合并失败: ", e);
            throw new IllegalArgumentException("合并失败");
        } finally {
            try {
                Set<String> directorys = new HashSet<>();
                if (isDeleteOriginLocalFile) {
                    //删除本地合并前原文件
                    for (String input : inputFilesFinal) {
                        Files.delete(Paths.get(input));
                        String directory = input.replace(input.substring(input.lastIndexOf(File.separator)), "");
                        directorys.add(directory);
                    }
                    for (String directory : directorys) {
                        Files.delete(Paths.get(directory));
                    }
                }
                if (document.isOpen()) {
                    document.close();
                }
                for (PdfReader r : readers) {
                    if (r != null) {
                        r.close();
                    }
                }
            } catch (Exception e) {
                log.error(" ", e);
            }
        }
    }

    /**
     * 创建pdf文档
     *
     * @param fileName 文件名称（带扩展名）
     * @return 文档key
     * @throws IOException
     * @throws DocumentException
     */
    @NaslLogic
    public String createDocument(String fileName) {
        try {
            String key = "document-" + UUID.randomUUID();
            //创建目录
            String tempDirectoryFinal = tempDirectory + File.separator + key + File.separator;
            File file = new File(tempDirectoryFinal);
            file.mkdirs();
            //将pdf档写入响应输出流
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(tempDirectoryFinal + fileName)));
            document.open();
            //信息放入内存
            PdfDocumentBuilder pdfDocumentBuilder = new PdfDocumentBuilder(document, tempDirectoryFinal, fileName, pdfWriter);
            pdfDocumentBuilderMap.put(key, pdfDocumentBuilder);
            return key;
        } catch (Exception e) {
            log.error("create document error", e);
            throw new IllegalArgumentException("create document error");
        }
    }

    /**
     * 关闭文档
     *
     * @param documentKey
     * @return 文件本地路径
     */
    @NaslLogic
    public String closeDocument(String documentKey) {
        try {
            PdfDocumentBuilder pdfDocumentBuilder = pdfDocumentBuilderMap.get(documentKey);
            if (pdfDocumentBuilder == null) {
                return null;
            }
            pdfDocumentBuilder.getDocument().close();
            String path = pdfDocumentBuilder.getTempDirectoryFinal() + pdfDocumentBuilder.getFileName();
            pdfDocumentBuilderMap.remove(documentKey);
            return path;
        } catch (Exception e) {
            log.error("close document error", e);
            throw new IllegalArgumentException("close document error");
        }
    }

    /**
     * 关闭文档并且获取文件地址
     *
     * @param documentKey
     * @return 文件访问信息
     */
    @NaslLogic
    public UploadResponseDTO closeDocumentGetFileURl(String documentKey) {
        PdfDocumentBuilder pdfDocumentBuilder = pdfDocumentBuilderMap.get(documentKey);
        if (pdfDocumentBuilder == null) {
            return null;
        }
        try {
            pdfDocumentBuilder.getDocument().close();
            FileInputStream fis = new FileInputStream(pdfDocumentBuilder.getTempDirectoryFinal() + File.separator + pdfDocumentBuilder.getFileName());
            UploadResponseDTO res = fileConnectorUtils.fileUploadV2(fis, pdfDocumentBuilder.getFileName(), new HashMap<>());
            pdfDocumentBuilderMap.remove(documentKey);
            return res;
        } catch (Exception e) {
            log.error("close document error", e);
            throw new IllegalArgumentException("close document error");
        } finally {
            try {
                Files.delete(Paths.get(pdfDocumentBuilder.getTempDirectoryFinal() + File.separator + pdfDocumentBuilder.getFileName()));
                Files.delete(Paths.get(pdfDocumentBuilder.getTempDirectoryFinal()));
            } catch (IOException e) {
                log.error("删除失败", e);
            }
        }
    }

    /**
     * 构造段落
     *
     * @param paragraphStr            段落文本。可空，空时创建空段落
     * @param iTextParagraphStructure 段落配置
     * @param iTextFontStructure      段落文本格式。paragraphStr为空时可空
     * @return 段落key
     * @throws DocumentException
     * @throws IOException
     */
    @NaslLogic
    public String buildParagraph(String paragraphStr, ITextParagraphStructure iTextParagraphStructure, ITextFontStructure iTextFontStructure) {
        try {
            Paragraph paragraph = new Paragraph();
            if (!StringUtils.isEmpty(paragraphStr)) {
                Font font = BaseFontBuilder.setFont(iTextFontStructure);
                paragraph = new Paragraph(paragraphStr, font);
            }
            PdfBuilderUtil.fillParagraph(paragraph, iTextParagraphStructure);
            String paragraphKey = "paragraph-" + UUID.randomUUID();
            elementMap.put(paragraphKey, paragraph);
            return paragraphKey;
        } catch (Exception e) {
            log.error("build paragraph error", e);
            throw new IllegalArgumentException("build paragraph error");
        }
    }

    /**
     * 构造表格
     *
     * @param iTextTableStructure 表格配置
     * @return
     * @throws DocumentException
     */
    @NaslLogic
    public String buildTable(ITextTableStructure iTextTableStructure) {
        try {
            PdfPTable table = new PdfPTable(iTextTableStructure.getNumColumns());
            PdfBuilderUtil.fillTable(table, iTextTableStructure);
            String tableKey = "table-" + UUID.randomUUID();
            elementMap.put(tableKey, table);
            return tableKey;
        } catch (Exception e) {
            log.error("build table error", e);
            throw new IllegalArgumentException("build table error");
        }
    }

    /**
     * @param cellStr            单元格文本。可空，空时创建空段落
     * @param iTextCellStructure 单元格配置
     * @param iTextFontStructure 单元格文本格式。paragraphStr为空时可空
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    @NaslLogic
    public String buildCell(String cellStr, ITextCellStructure iTextCellStructure, ITextFontStructure iTextFontStructure) {
        try {
            PdfPCell cell = new PdfPCell();
            if (!StringUtils.isEmpty(cellStr)) {
                Font font = BaseFontBuilder.setFont(iTextFontStructure);
                cell = new PdfPCell(new Paragraph(cellStr, font));
            }
            PdfBuilderUtil.fillCell(cell, iTextCellStructure);
            String cellKey = "cell-" + UUID.randomUUID();
            elementMap.put(cellKey, cell);
            return cellKey;
        } catch (Exception e) {
            log.error("build cell error", e);
            throw new IllegalArgumentException("build cell error");
        }
    }

    /**
     * 构造chunk
     *
     * @param chunkStr            文本。可空，空时创建空段落
     * @param iTextChunkStructure 配置
     * @param iTextFontStructure  文本格式
     * @return
     */
    @NaslLogic
    public String buildChunk(String chunkStr, ITextChunkStructure iTextChunkStructure, ITextFontStructure iTextFontStructure) {
        try {
            Chunk chunk;
            if (iTextFontStructure == null) {
                chunk = new Chunk(chunkStr);
            } else {
                Font font = BaseFontBuilder.setFont(iTextFontStructure);
                chunk = new Chunk(chunkStr, font);
                PdfBuilderUtil.fillChunk(chunk, iTextChunkStructure);
            }
            String chunkKey = "chunk-" + UUID.randomUUID();
            elementMap.put(chunkKey, chunk);
            return chunkKey;
        } catch (Exception e) {
            log.error("build chunk error", e);
            throw new IllegalArgumentException("build chunk error");
        }
    }

    /**
     * 构造短语
     *
     * @param phraseStr            文本。可空，空时创建空段落
     * @param iTextFontStructure   文本格式
     * @param iTextPhraseStructure 短语配置
     * @return
     */
    @NaslLogic
    public String buildPhrase(String phraseStr, ITextFontStructure iTextFontStructure, ITextPhraseStructure iTextPhraseStructure) {
        try {
            Phrase phrase = new Phrase();
            if (!StringUtils.isEmpty(phraseStr)) {
                phrase = new Phrase(phraseStr, BaseFontBuilder.setFont(iTextFontStructure));
            }
            PdfBuilderUtil.fillPhrase(phrase, iTextPhraseStructure);
            String phraseKey = "phrase-" + UUID.randomUUID();
            elementMap.put(phraseKey, phrase);
            return phraseKey;
        } catch (Exception e) {
            log.error("build phrase error", e);
            throw new IllegalArgumentException("build phrase error");
        }
    }

    /**
     * 新增图片canvas，置于顶层
     *
     * @param documentKey
     * @param imageKey
     * @return
     */
    @NaslLogic
    public Boolean addCanvasImage(String documentKey, String imageKey) {
        try {
            PdfDocumentBuilder pdfDocumentBuilder = pdfDocumentBuilderMap.get(documentKey);
            if (pdfDocumentBuilder == null) {
                throw new IllegalArgumentException("documentKey is not exist");
            }
            Image image = (Image) elementMap.get(imageKey);
            if (image == null) {
                throw new IllegalArgumentException("imageKey is not exist");
            }
            PdfWriter pdfWriter = pdfDocumentBuilder.getPdfWriter();
            PdfContentByte canvas = pdfWriter.getDirectContent();
            canvas.addImage(image);
            return true;
        } catch (Exception e) {
            log.error("add canvasImage error", e);
            throw new IllegalArgumentException("add canvasImage error");
        }
    }

    /**
     * 构造列表
     *
     * @param listItemStr        列表项文本。可空，空时创建空列表
     * @param iTextFontStructure 列表项文本格式
     * @param iTextListStructure 列表配置
     * @return
     */
    @NaslLogic
    public String buildListItem(String listItemStr, ITextFontStructure iTextFontStructure, ITextListStructure iTextListStructure) {
        try {
            ListItem listItem = new ListItem();
            if (!StringUtils.isEmpty(listItemStr)) {
                listItem = new ListItem(listItemStr, BaseFontBuilder.setFont(iTextFontStructure));
            }
            PdfBuilderUtil.filListItem(listItem, iTextListStructure);
            String listItemKey = "listItem-" + UUID.randomUUID();
            elementMap.put(listItemKey, listItem);
            return listItemKey;
        } catch (Exception e) {
            log.error("build listItem error", e);
            throw new IllegalArgumentException("build listItem error");
        }
    }

    /**
     * 构造图片元素
     *
     * @param imageUrl            图片url
     * @param iTextImageStructure 图片配置
     * @return
     */
    @NaslLogic
    public String buildImage(String imageUrl, ITextImageStructure iTextImageStructure) {
        try {
            Image image = Image.getInstance(new URL(imageUrl));
            PdfBuilderUtil.fillImage(image, iTextImageStructure);
            String imageKey = "image-" + UUID.randomUUID();
            elementMap.put(imageKey, image);
            return imageKey;
        } catch (Exception e) {
            log.error("build image error", e);
            throw new IllegalArgumentException("build image error");
        }
    }

    /**
     * 使用base64字符串构造图片
     *
     * @param base64ImageString   图片base64字符串
     * @param iTextImageStructure 图片配置
     * @return
     */
    @NaslLogic
    public String buildImageBase64(String base64ImageString, ITextImageStructure iTextImageStructure) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64ImageString);
            Image image = Image.getInstance(imageBytes);
            PdfBuilderUtil.fillImage(image, iTextImageStructure);
            String imageKey = "image-" + UUID.randomUUID();
            elementMap.put(imageKey, image);
            return imageKey;
        } catch (Exception e) {
            log.error("build image error", e);
            throw new IllegalArgumentException("build image error");
        }
    }

    /**
     * 组装元素
     *
     * @param addedElementKey 子元素key
     * @param addElementKey   父元素key
     * @return
     * @throws DocumentException
     */
    @NaslLogic
    public Boolean addDocumentElement(String addedElementKey, String addElementKey) {
        boolean flag = true;
        try {
            PdfDocumentBuilder pdfDocumentBuilder = pdfDocumentBuilderMap.get(addElementKey);
            Object elementSon = elementMap.get(addedElementKey);
            if (pdfDocumentBuilder != null) {
                if (elementSon instanceof Paragraph) {
                    Paragraph paragraph = (Paragraph) elementSon;
                    Document document = pdfDocumentBuilder.getDocument();
                    document.add(paragraph);
                    pdfDocumentBuilder.setDocument(document);
                } else if (elementSon instanceof Phrase) {
                    Phrase phrase = (Phrase) elementSon;
                    Document document = pdfDocumentBuilder.getDocument();
                    document.add(phrase);
                    pdfDocumentBuilder.setDocument(document);
                } else if (elementSon instanceof PdfPTable) {
                    PdfPTable pdfPTable = (PdfPTable) elementSon;
                    Document document = pdfDocumentBuilder.getDocument();
                    document.add(pdfPTable);
                    pdfDocumentBuilder.setDocument(document);
                } else if (elementSon instanceof Image) {
                    Image image = (Image) elementSon;
                    Document document = pdfDocumentBuilder.getDocument();
                    document.add(image);
                    pdfDocumentBuilder.setDocument(document);
                } else if (elementSon instanceof ListItem) {
                    ListItem listItem = (ListItem) elementSon;
                    Document document = pdfDocumentBuilder.getDocument();
                    document.add(listItem);
                    pdfDocumentBuilder.setDocument(document);
                } else if (elementSon instanceof Chunk) {
                    Chunk chunk = (Chunk) elementSon;
                    Document document = pdfDocumentBuilder.getDocument();
                    document.add(chunk);
                    pdfDocumentBuilder.setDocument(document);
                } else {
                    log.error("addedElementKey获取的elementSon类型错误");
                    flag = false;
                }
            } else {
                Object elementParent = elementMap.get(addElementKey);
                if (elementParent instanceof Paragraph) {
                    Paragraph paragraph = (Paragraph) elementParent;
                    if (elementSon instanceof Paragraph) {
                        Paragraph paragraphSon = (Paragraph) elementSon;
                        paragraph.add(paragraphSon);
                        elementMap.put(addElementKey, paragraph);
                    } else if (elementSon instanceof Phrase) {
                        Phrase phrase = (Phrase) elementSon;
                        paragraph.add(phrase);
                        elementMap.put(addElementKey, paragraph);
                    } else if (elementSon instanceof PdfPTable) {
                        PdfPTable pdfPTable = (PdfPTable) elementSon;
                        paragraph.add(pdfPTable);
                        elementMap.put(addElementKey, paragraph);
                    } else if (elementSon instanceof Image) {
                        Image image = (Image) elementSon;
                        paragraph.add(image);
                        elementMap.put(addElementKey, paragraph);
                    } else if (elementSon instanceof ListItem) {
                        ListItem listItem = (ListItem) elementSon;
                        paragraph.add(listItem);
                        elementMap.put(addElementKey, paragraph);
                    } else if (elementSon instanceof Chunk) {
                        Chunk chunk = (Chunk) elementSon;
                        paragraph.add(chunk);
                        elementMap.put(addElementKey, paragraph);
                    } else {
                        flag = false;
                        log.error("addedElementKey获取的elementSon类型错误");
                    }
                } else if (elementParent instanceof PdfPTable) {
                    PdfPTable pdfPTable = (PdfPTable) elementParent;
                    if (elementSon instanceof Paragraph) {
                        Paragraph paragraphSon = (Paragraph) elementSon;
                        pdfPTable.addCell(paragraphSon);
                        elementMap.put(addElementKey, pdfPTable);
                    } else if (elementSon instanceof Phrase) {
                        Phrase phrase = (Phrase) elementSon;
                        pdfPTable.addCell(phrase);
                        elementMap.put(addElementKey, pdfPTable);
                    } else if (elementSon instanceof PdfPTable) {
                        PdfPTable pdfPTableSon = (PdfPTable) elementSon;
                        pdfPTable.addCell(pdfPTableSon);
                        elementMap.put(addElementKey, pdfPTable);
                    } else if (elementSon instanceof Image) {
                        Image image = (Image) elementSon;
                        pdfPTable.addCell(image);
                        elementMap.put(addElementKey, pdfPTable);
                    } else if (elementSon instanceof ListItem) {
                        ListItem listItem = (ListItem) elementSon;
                        pdfPTable.addCell(listItem);
                        elementMap.put(addElementKey, pdfPTable);
                    } else if (elementSon instanceof PdfPCell) {
                        PdfPCell cell = (PdfPCell) elementSon;
                        pdfPTable.addCell(cell);
                        elementMap.put(addElementKey, pdfPTable);
                    } else {
                        flag = false;
                        log.error("addedElementKey获取的elementSon类型错误");
                    }
                } else if (elementParent instanceof PdfPCell) {
                    PdfPCell pdfPCell = (PdfPCell) elementParent;
                    if (elementSon instanceof Paragraph) {
                        Paragraph paragraphSon = (Paragraph) elementSon;
                        pdfPCell.addElement(paragraphSon);
                        elementMap.put(addElementKey, pdfPCell);
                    } else if (elementSon instanceof Phrase) {
                        Phrase phrase = (Phrase) elementSon;
                        pdfPCell.addElement(phrase);
                        elementMap.put(addElementKey, pdfPCell);
                    } else if (elementSon instanceof PdfPTable) {
                        PdfPTable pdfPTable = (PdfPTable) elementSon;
                        pdfPCell.addElement(pdfPTable);
                        elementMap.put(addElementKey, pdfPCell);
                    } else if (elementSon instanceof Image) {
                        Image image = (Image) elementSon;
                        pdfPCell.addElement(image);
                        elementMap.put(addElementKey, pdfPCell);
                    } else if (elementSon instanceof ListItem) {
                        ListItem listItem = (ListItem) elementSon;
                        pdfPCell.addElement(listItem);
                        elementMap.put(addElementKey, pdfPCell);
                    } else if (elementSon instanceof Chunk) {
                        Chunk chunk = (Chunk) elementSon;
                        pdfPCell.addElement(chunk);
                        elementMap.put(addElementKey, pdfPCell);
                    } else {
                        flag = false;
                        log.error("addedElementKey获取的elementSon类型错误");
                    }
                } else if (elementParent instanceof ListItem) {
                    ListItem listItem = (ListItem) elementParent;
                    if (elementSon instanceof Paragraph) {
                        Paragraph paragraphSon = (Paragraph) elementSon;
                        listItem.add(paragraphSon);
                        elementMap.put(addElementKey, listItem);
                    } else if (elementSon instanceof Phrase) {
                        Phrase phrase = (Phrase) elementSon;
                        listItem.add(phrase);
                        elementMap.put(addElementKey, listItem);
                    } else if (elementSon instanceof PdfPTable) {
                        PdfPTable pdfPTable = (PdfPTable) elementSon;
                        listItem.add(pdfPTable);
                        elementMap.put(addElementKey, listItem);
                    } else if (elementSon instanceof Image) {
                        Image image = (Image) elementSon;
                        listItem.add(image);
                        elementMap.put(addElementKey, listItem);
                    } else if (elementSon instanceof ListItem) {
                        ListItem listItemSon = (ListItem) elementSon;
                        listItem.add(listItemSon);
                        elementMap.put(addElementKey, listItem);
                    } else if (elementSon instanceof Chunk) {
                        Chunk chunk = (Chunk) elementSon;
                        listItem.add(chunk);
                        elementMap.put(addElementKey, listItem);
                    } else {
                        flag = false;
                        log.error("addedElementKey获取的elementSon类型错误");
                    }
                } else {
                    flag = false;
                    log.error("addElementKey获取的elementParent类型错误");
                }
            }
        } catch (Exception e) {
            log.error("build addElement error", e);
            throw new IllegalArgumentException("build addElement error");
        }
        if (flag) {
            elementMap.remove(addedElementKey);
        }
        return flag;
    }

}

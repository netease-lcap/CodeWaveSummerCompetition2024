package com.netease.lowcode.lib.itextpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfDocumentBuilder {


    private Document document;
    private String tempDirectoryFinal;
    private String fileName;
    private PdfWriter pdfWriter;

    public PdfDocumentBuilder(Document document, String tempDirectoryFinal, String fileName, PdfWriter pdfWriter) {
        this.document = document;
        this.tempDirectoryFinal = tempDirectoryFinal;
        this.fileName = fileName;
        this.pdfWriter = pdfWriter;
    }

    public PdfWriter getPdfWriter() {
        return pdfWriter;
    }

    public void setPdfWriter(PdfWriter pdfWriter) {
        this.pdfWriter = pdfWriter;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTempDirectoryFinal() {
        return tempDirectoryFinal;
    }

    public void setTempDirectoryFinal(String tempDirectoryFinal) {
        this.tempDirectoryFinal = tempDirectoryFinal;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}

package com.netease.lowcode.lib.itextpdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.netease.lowcode.lib.enums.BaseColorEnum;
import com.netease.lowcode.lib.enums.ElementAlignEnum;
import com.netease.lowcode.lib.enums.RectangleEnum;
import com.netease.lowcode.lib.structure.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PdfBuilderUtil {

    public static void fillParagraph(Paragraph paragraph, ITextParagraphStructure iTextParagraphStructure) {
        if (iTextParagraphStructure == null) {
            return;
        }
        if (iTextParagraphStructure.getAlignText() != null) {
            paragraph.setAlignment(ElementAlignEnum.getAlignElement(iTextParagraphStructure.getAlignText()));
        }
        if (iTextParagraphStructure.getIndentationLeft() != null) {
            paragraph.setIndentationLeft(Float.parseFloat(iTextParagraphStructure.getIndentationLeft() + ""));
        }
        if (iTextParagraphStructure.getIndentationRight() != null) {
            paragraph.setIndentationRight(Float.parseFloat(iTextParagraphStructure.getIndentationRight() + ""));
        }
        if (iTextParagraphStructure.getFirstLineIndent() != null) {
            paragraph.setFirstLineIndent(Float.parseFloat(iTextParagraphStructure.getFirstLineIndent() + ""));
        }
        if (iTextParagraphStructure.getSpacingBefore() != null) {
            paragraph.setSpacingBefore(Float.parseFloat(iTextParagraphStructure.getSpacingBefore() + ""));
        }
        if (iTextParagraphStructure.getSpacingAfter() != null) {
            paragraph.setSpacingAfter(Float.parseFloat(iTextParagraphStructure.getSpacingAfter() + ""));
        }
        if (iTextParagraphStructure.getFixedLeading() != null) {
            paragraph.setLeading(Float.parseFloat(iTextParagraphStructure.getFixedLeading() + ""));
        }
        if (iTextParagraphStructure.getMultipliedLeading() != null) {
            paragraph.setMultipliedLeading(Float.parseFloat(iTextParagraphStructure.getMultipliedLeading() + ""));
        }
        if (iTextParagraphStructure.getKeepTogether() != null) {
            paragraph.setKeepTogether(iTextParagraphStructure.getKeepTogether());
        }
    }

    public static void fillTable(PdfPTable table, ITextTableStructure iTextTableStructure) throws DocumentException {
        if (iTextTableStructure == null) {
            return;
        }
        if (iTextTableStructure.getWidthPercentage() != null) {
            table.setWidthPercentage(Float.parseFloat(iTextTableStructure.getWidthPercentage() + ""));
        }
        if (iTextTableStructure.getTotalWidth() != null) {
            table.setTotalWidth(Float.parseFloat(iTextTableStructure.getTotalWidth() + ""));
        }
        if (iTextTableStructure.getLockedWidth() != null) {
            table.setLockedWidth(iTextTableStructure.getLockedWidth());
        }
        if (iTextTableStructure.getHorizontalAlignmentText() != null) {
            table.setHorizontalAlignment(ElementAlignEnum.getAlignElement(iTextTableStructure.getHorizontalAlignmentText()));
        }
        if (iTextTableStructure.getSpacingBefore() != null) {
            table.setSpacingBefore(Float.parseFloat(iTextTableStructure.getSpacingBefore() + ""));
        }
        if (iTextTableStructure.getSpacingAfter() != null) {
            table.setSpacingAfter(Float.parseFloat(iTextTableStructure.getSpacingAfter() + ""));
        }
        if (iTextTableStructure.getWidths() != null && !iTextTableStructure.getWidths().isEmpty()) {
            float[] widths = new float[iTextTableStructure.getWidths().size()];
            for (int i = 0; i < iTextTableStructure.getWidths().size(); i++) {
                widths[i] = Float.parseFloat(iTextTableStructure.getWidths().get(i) + "");
            }
            table.setWidths(widths);
        }
        if (iTextTableStructure.getHeaderRows() != null) {
            table.setHeaderRows(iTextTableStructure.getHeaderRows());
        }
        if (iTextTableStructure.getFooterRows() != null) {
            table.setFooterRows(iTextTableStructure.getFooterRows());
        }
        if (iTextTableStructure.getSplitLate() != null) {
            table.setSplitLate(iTextTableStructure.getSplitLate());
        }
        if (iTextTableStructure.getSkipFirstHeader() != null) {
            table.setSkipFirstHeader(iTextTableStructure.getSkipFirstHeader());
        }
    }

    public static void fillCell(PdfPCell cell, ITextCellStructure iTextCellStructure) {
        if (iTextCellStructure == null) {
            return;
        }
        if (iTextCellStructure.getBorder() != null) {
            cell.setBorder(RectangleEnum.getRectangle(iTextCellStructure.getBorder()));
        }
        if (iTextCellStructure.getBorderWidth() != null) {
            cell.setBorderWidth(Float.parseFloat(iTextCellStructure.getBorderWidth() + ""));
        }
        if (iTextCellStructure.getPadding() != null) {
            cell.setPadding(Float.parseFloat(iTextCellStructure.getPadding() + ""));
        }
        if (iTextCellStructure.getBorderColor() != null) {
            cell.setBorderColor(BaseColorEnum.getBaseColor(iTextCellStructure.getBorderColor()));
        }
        if (iTextCellStructure.getPaddingLeft() != null) {
            cell.setPaddingLeft(Float.parseFloat(iTextCellStructure.getPaddingLeft() + ""));
        }
        if (iTextCellStructure.getPaddingRight() != null) {
            cell.setPaddingRight(Float.parseFloat(iTextCellStructure.getPaddingRight() + ""));
        }
        if (iTextCellStructure.getPaddingTop() != null) {
            cell.setPaddingTop(Float.parseFloat(iTextCellStructure.getPaddingTop() + ""));
        }
        if (iTextCellStructure.getPaddingBottom() != null) {
            cell.setPaddingBottom(Float.parseFloat(iTextCellStructure.getPaddingBottom() + ""));
        }
        if (iTextCellStructure.getHorizontalAlignmentText() != null) {
            cell.setHorizontalAlignment(ElementAlignEnum.getAlignElement(iTextCellStructure.getHorizontalAlignmentText()));
        }
        if (iTextCellStructure.getVerticalAlignmentText() != null) {
            cell.setVerticalAlignment(ElementAlignEnum.getAlignElement(iTextCellStructure.getVerticalAlignmentText()));
        }
        if (iTextCellStructure.getFixedHeight() != null) {
            cell.setFixedHeight(Float.parseFloat(iTextCellStructure.getFixedHeight() + ""));
        }
        if (iTextCellStructure.getMinimumHeight() != null) {
            cell.setMinimumHeight(Float.parseFloat(iTextCellStructure.getMinimumHeight() + ""));
        }
        if (iTextCellStructure.getFixedLeading() == null) {
            iTextCellStructure.setFixedLeading(0d);
        }
        if (iTextCellStructure.getMultipliedLeading() == null) {
            iTextCellStructure.setMultipliedLeading(0d);
        }
        cell.setLeading(Float.parseFloat(iTextCellStructure.getFixedLeading() + ""), Float.parseFloat(iTextCellStructure.getMultipliedLeading() + ""));

        if (iTextCellStructure.getBackgroundColor() != null) {
            cell.setBackgroundColor(BaseColorEnum.getBaseColor(iTextCellStructure.getBackgroundColor()));
        }
        if (iTextCellStructure.getColSpan() != null) {
            cell.setColspan(iTextCellStructure.getColSpan());
        }
        if (iTextCellStructure.getRowSpan() != null) {
            cell.setRowspan(iTextCellStructure.getRowSpan());
        }
    }


    public static void fillChunk(Chunk chunk, ITextChunkStructure iTextChunkStructure) {
        if (iTextChunkStructure == null) {
            return;
        }
        if (iTextChunkStructure.getCharacterSpacing() != null) {
            chunk.setCharacterSpacing(Float.parseFloat(iTextChunkStructure.getCharacterSpacing() + ""));
        }
        if (iTextChunkStructure.getWordSpacing() != null) {
            chunk.setWordSpacing(Float.parseFloat(iTextChunkStructure.getWordSpacing() + ""));
        }
        if (iTextChunkStructure.getAnchor() != null) {
            chunk.setAnchor(iTextChunkStructure.getAnchor());
        }
        if (iTextChunkStructure.getBackgroundColor() != null) {
            chunk.setBackground(BaseColorEnum.getBaseColor(iTextChunkStructure.getBackgroundColor()));
        }
    }

    public static void fillPhrase(Phrase phrase, ITextPhraseStructure iTextPhraseStructure) {
        if (iTextPhraseStructure == null) {
            return;
        }
        if (iTextPhraseStructure.getFixedLeading() != null) {
            phrase.setLeading(Float.parseFloat(iTextPhraseStructure.getFixedLeading() + ""));
        }
        if (iTextPhraseStructure.getMultipliedLeading() != null) {
            phrase.setMultipliedLeading(Float.parseFloat(iTextPhraseStructure.getMultipliedLeading() + ""));
        }
    }

    public static void filListItem(ListItem listItem, ITextListStructure iTextListStructure) {
        if (iTextListStructure == null) {
            return;
        }
        if (iTextListStructure.getAlignText() != null) {
            listItem.setAlignment(ElementAlignEnum.getAlignElement(iTextListStructure.getAlignText()));
        }
        if (iTextListStructure.getIndentationLeft() != null) {
            listItem.setIndentationLeft(Float.parseFloat(iTextListStructure.getIndentationLeft() + ""));
        }
        if (iTextListStructure.getIndentationRight() != null) {
            listItem.setIndentationRight(Float.parseFloat(iTextListStructure.getIndentationRight() + ""));
        }
        if (iTextListStructure.getFirstLineIndent() != null) {
            listItem.setFirstLineIndent(Float.parseFloat(iTextListStructure.getFirstLineIndent() + ""));
        }
        if (iTextListStructure.getKeepTogether() != null) {
            listItem.setKeepTogether(iTextListStructure.getKeepTogether());
        }
        if (iTextListStructure.getSpacingBefore() != null) {
            listItem.setSpacingBefore(Float.parseFloat(iTextListStructure.getSpacingBefore() + ""));
        }
        if (iTextListStructure.getSpacingAfter() != null) {
            listItem.setSpacingAfter(Float.parseFloat(iTextListStructure.getSpacingAfter() + ""));
        }
        if (iTextListStructure.getPaddingTop() != null) {
            listItem.setPaddingTop(Float.parseFloat(iTextListStructure.getPaddingTop() + ""));
        }
        if (iTextListStructure.getFixedLeading() != null) {
            listItem.setLeading(Float.parseFloat(iTextListStructure.getFixedLeading() + ""));
        }
        if (iTextListStructure.getMultipliedLeading() != null) {
            listItem.setMultipliedLeading(Float.parseFloat(iTextListStructure.getMultipliedLeading() + ""));
        }
    }

    public static void fillImage(Image image, ITextImageStructure iTextImageStructure) throws MalformedURLException {
        if (iTextImageStructure == null) {
            return;
        }
        if (iTextImageStructure.getScaleToFitHeight() != null && iTextImageStructure.getScaleToFitWidth() != null) {
            image.scaleToFit(Float.parseFloat(iTextImageStructure.getScaleToFitWidth() + ""), Float.parseFloat(iTextImageStructure.getScaleToFitHeight() + ""));
        }

        if (iTextImageStructure.getScaleAbsoluteWidth() != null && iTextImageStructure.getScaleAbsoluteHeight() == null) {
            image.scaleAbsolute(Float.parseFloat(iTextImageStructure.getScaleAbsoluteWidth() + ""), Float.parseFloat(iTextImageStructure.getScaleAbsoluteHeight() + ""));
        }
        if (iTextImageStructure.getScalePercent() != null) {
            image.scalePercent(Float.parseFloat(iTextImageStructure.getScalePercent() + ""));
        }
        if (iTextImageStructure.getAlignText() != null) {
            image.setAlignment(ElementAlignEnum.getAlignElement(iTextImageStructure.getAlignText()));
        }
        if (iTextImageStructure.getAbsoluteX() != null && iTextImageStructure.getAbsoluteY() != null) {
            image.setAbsolutePosition(Float.parseFloat(iTextImageStructure.getAbsoluteX() + ""), Float.parseFloat(iTextImageStructure.getAbsoluteY() + ""));
        }
        if (iTextImageStructure.getIndentationLeft() != null) {
            image.setIndentationLeft(Float.parseFloat(iTextImageStructure.getIndentationLeft() + ""));
        }
        if (iTextImageStructure.getBorder() != null) {
            image.setBorder(RectangleEnum.getRectangle(iTextImageStructure.getBorder()));
        }
        if (iTextImageStructure.getBorderWidth() != null) {
            image.setBorderWidth(Float.parseFloat(iTextImageStructure.getBorderWidth() + ""));
        }
        if (iTextImageStructure.getBorderColor() != null) {
            image.setBorderColor(BaseColorEnum.getBaseColor(iTextImageStructure.getBorderColor()));
        }
        if (iTextImageStructure.getSpacingBefore() != null) {
            image.setSpacingBefore(Float.parseFloat(iTextImageStructure.getSpacingBefore() + ""));
        }
        if (iTextImageStructure.getSpacingAfter() != null) {
            image.setSpacingAfter(Float.parseFloat(iTextImageStructure.getSpacingAfter() + ""));
        }
        if (iTextImageStructure.getTransparency() != null && !iTextImageStructure.getTransparency().isEmpty()) {
            List<Integer> transparencyList = iTextImageStructure.getTransparency();
            int[] transparency = new int[transparencyList.size()];
            for (int i = 0; i < transparencyList.size(); i++) {
                transparency[i] = transparencyList.get(i);
            }
            image.setTransparency(transparency);
        }
        if (iTextImageStructure.getAnchor() != null) {
            image.setUrl(new URL(iTextImageStructure.getAnchor()));
        }
        if (iTextImageStructure.getCompressionLevel() != null) {
            image.setCompressionLevel(iTextImageStructure.getCompressionLevel());
        }
        if (iTextImageStructure.getDpiX() != null && iTextImageStructure.getDpiY() != null) {
            image.setDpi(iTextImageStructure.getDpiX(), iTextImageStructure.getDpiY());
        }
    }
}

package com.netease.lowcode.extension.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.netease.lowcode.extension.excel.dto.ExcelParseError;
import com.netease.lowcode.extension.excel.dto.ExcelParseRect;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反置读取excel， todo 未实现
 * 根据泛型自动反射为指定类型列表
 * @param <T>
 */
@Slf4j
public class ReverseReaderListener<T> extends AnalysisEventListener<Map<Integer, String>> {
    private Class<T> tClass;
    private ExcelParseRect rect;
    private Map<Integer, String> headMap;
    private int headRowIndex = 0;

    private List<T> data = new ArrayList<>();
    private List<Map<String, String>> unParsedData = new ArrayList<>();
    private List<ExcelParseError> parseErrors = new ArrayList<>();
    private List<CellExtra> mergeCells = new ArrayList<>();

    public ReverseReaderListener(Class<T> tClass) {
        this.tClass = tClass;
        if (!ReflectionUtils.isCollectionType(tClass)) {
            ReflectionUtils.register(tClass);
        }
    }

    public ReverseReaderListener(Class<T> tClass, ExcelParseRect rect) {
        this.tClass = tClass;
        this.rect = rect;
        if (!ReflectionUtils.isCollectionType(tClass)) {
            ReflectionUtils.register(tClass);
        }
    }

    public List<T> getData() {
        return data;
    }

    public List<ExcelParseError> getParseErrors() {
        return parseErrors;
    }

    public List<Map<String, String>> getUnParsedData() {
        return unParsedData;
    }

    @Override
    public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {
        int rowIndex = analysisContext.readRowHolder().getRowIndex();
        int[] startCellIndex = ExcelCommonUtils.cellLocateToIndex(null == rect ? null : rect.getStartCell());
        int[] endCellIndex = ExcelCommonUtils.cellLocateToIndex(null == rect ? null : rect.getEndCell());
        if (!ExcelCommonUtils.isInRect(rowIndex, startCellIndex[0], endCellIndex[0])) {
            log.info("不在指定的读取范围内，当前{}行，起始行{},结束行{}", rowIndex, startCellIndex[0], endCellIndex[0]);
            return;
        }


        if (null != headMap && !headMap.isEmpty()) {
            T t = ReflectionUtils.newInstance(tClass);
            Map<String, String> unParsed = null;

            for (Map.Entry<Integer, String> entry : integerStringMap.entrySet()) {
                try {
                    int columnIndex = entry.getKey();

                    if (!ExcelCommonUtils.isInRect(columnIndex, startCellIndex[1], endCellIndex[1])) {
                        log.info("不在指定的读取范围内，当前{}列，起始列{},结束列{}", rowIndex, startCellIndex[1], endCellIndex[1]);
                        continue;
                    }

                    String head = headMap.get(entry.getKey());
                    boolean isSetValueSuccess = ReflectionUtils.setValue(t, head, entry.getValue());
                    if (!isSetValueSuccess) {
                        unParsed = null == unParsed ? new HashMap<>(integerStringMap.size()) : unParsed;
                        unParsed.put(head, entry.getValue());
                    }
                } catch (Exception e) {
                    ExcelParseError parseError = new ExcelParseError();
                    parseError.setSheetName(analysisContext.readSheetHolder().getSheetName());
                    parseError.setCellName(ExcelCommonUtils.indexToCellLocate(rowIndex, entry.getKey()));
                    parseError.setCellValue(entry.getValue());
                    parseError.setErrorMsg(String.format("解析%s单元格%s时发生错误:%s",
                            parseError.getSheetName(), parseError.getCellName(), e.getMessage()));
                    parseErrors.add(parseError);
                    log.warn("解析单元格出错", e);
                }
            }

            if (null != t) {
                data.add(t);
            }

            if (null != unParsed && !unParsed.isEmpty()) {
                unParsedData.add(unParsed);
            }
        }
    }

    /**
     * 读取额外信息,在invoke方法执行完成后,doAfterAllAnalysed方法前执行
     * 这里主要读取合并单元格信息
     */
    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        switch (extra.getType()) {
            case MERGE: {
                mergeCells.add(extra);
                break;
            }
            default:
        }
    }


    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("当前第{}行为标题行", context.readRowHolder().getRowIndex());
        this.headRowIndex = context.readRowHolder().getRowIndex();
        this.headMap = headMap;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (!CollectionUtils.isEmpty(mergeCells)) {
            for (CellExtra extra : mergeCells) {
                // 因为可以指定区域，所以有时候不一定会全部读取到
                int maxRowIndex = Math.min(extra.getLastRowIndex(), data.size());
                // TODO 需要考虑合并单元格在读取范围之外的情况
                if (maxRowIndex < extra.getFirstRowIndex()) {
                    continue;
                }

                // 需要-1扣除标题行
                int firstDataIndex = extra.getFirstRowIndex() - 1;
                T firstRowObj = data.get(firstDataIndex);
                String firstColumnHead = headMap.get(extra.getFirstColumnIndex());
                for (int i = firstDataIndex; i < maxRowIndex; i++) {
                    T rowData = data.get(i);

                    for (int j = extra.getFirstColumnIndex(); j <= extra.getLastColumnIndex(); j++) {
                        String columnHead = headMap.get(j);
                        ReflectionUtils.copyProperty(firstRowObj, firstColumnHead, rowData, columnHead);
                    }
                }
            }
        }
    }
}

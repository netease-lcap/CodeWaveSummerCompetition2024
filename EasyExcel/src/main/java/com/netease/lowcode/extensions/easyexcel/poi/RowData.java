package com.netease.lowcode.extensions.easyexcel.poi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RowData {

    private List<CellData> cellDataList = new ArrayList<>();
    private boolean sorted = false;

    /**
     * 行样式信息 支持设置行 样式？
     */

    public void addCell(CellData cellData) {
        this.cellDataList.add(cellData);
        this.sorted = false;
    }




    public CellData getCell(int colNum) {
        return cellDataList.get(colNum);
    }

    public List<CellData> getCellDataList() {
        this.sort();
        return cellDataList;
    }

    public void setCellDataList(List<CellData> cellDataList) {
        this.cellDataList = cellDataList;
        this.sorted = false;
    }

    private void sort() {
        if (this.sorted) {
            return;
        }
        this.sorted = true;
        cellDataList.sort(Comparator.comparingInt(CellData::getIndex));
    }
}

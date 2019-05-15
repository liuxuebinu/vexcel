package org.vexcel.pojo;

import java.util.List;

public class VSheet {
    // sheet页下标
    Integer sheetIndex;
    // 该sheet下校验起始行
    Integer beginRow;
    // 该sheet最大数据行
    Integer endRow;
    // 每列的校验规则
    private List<ValidateRule> columns;
    // 唯一性约束键
    private List<UniqueKey> uniqueKeys;

    public Integer getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(Integer sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public Integer getBeginRow() {
        return beginRow;
    }

    public void setBeginRow(Integer beginRow) {
        this.beginRow = beginRow;
    }

    public Integer getEndRow() {
        return endRow;
    }

    public void setEndRow(Integer endRow) {
        this.endRow = endRow;
    }

    public List<ValidateRule> getColumns() {
        return columns;
    }

    public void setColumns(List<ValidateRule> columns) {
        this.columns = columns;
    }

    public List<UniqueKey> getUniqueKeys() {
        return uniqueKeys;
    }

    public void setUniqueKeys(List<UniqueKey> uniqueKeys) {
        this.uniqueKeys = uniqueKeys;
    }

}

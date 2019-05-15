package org.vexcel.pojo;

import java.util.List;

public class UniqueKey {
    // keyId
    private String keyName;
    // 唯一性约束键的表格列标
    private List<Integer> uniqueColumn;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public List<Integer> getUniqueColumn() {
        return uniqueColumn;
    }

    public void setUniqueColumn(List<Integer> uniqueColumn) {
        this.uniqueColumn = uniqueColumn;
    }

}

package org.vexcel.pojo;

public class ValidateRule {
    // 表头名字
    private String name;
    // excel列标
    private Integer columnIndex;
    // 数据对应的java类型
    private String classType;
    // 是否非空属性值
    private Boolean isNullAble;
    // 最大字符长度属性值
    private Integer maxLength;
    // 最小字符长度属性值
    private Integer minLength;
    // 正则表达式属性值
    private String rule;
    // 日期格式属性值
    private String dateFormat;
    // 浮点数整数部分最小长度属性值
    private Integer minIntLength;
    // 浮点数整数部分最大长度属性值
    private Integer maxIntLength;
    // 浮点数小数部分最小精度属性值
    private Integer minScaleLength;
    // 浮点数小数部分最大精度属性值
    private Integer maxScaleLength;

    public Boolean getIsNullAble() {
        return isNullAble;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinIntLength() {
        return minIntLength;
    }

    public void setMinIntLength(Integer minIntLength) {
        this.minIntLength = minIntLength;
    }

    public Integer getMaxIntLength() {
        return maxIntLength;
    }

    public void setMaxIntLength(Integer maxIntLength) {
        this.maxIntLength = maxIntLength;
    }

    public Integer getMinScaleLength() {
        return minScaleLength;
    }

    public void setMinScaleLength(Integer minScaleLength) {
        this.minScaleLength = minScaleLength;
    }

    public Integer getMaxScaleLength() {
        return maxScaleLength;
    }

    public void setMaxScaleLength(Integer maxScaleLength) {
        this.maxScaleLength = maxScaleLength;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setIsNullAble(Boolean isNullAble) {
        this.isNullAble = isNullAble;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

}

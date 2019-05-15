package org.vexcel.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.vexcel.exception.ValidateXmlException;
import org.vexcel.pojo.ExcelConfig;
import org.vexcel.pojo.UniqueKey;
import org.vexcel.pojo.VSheet;
import org.vexcel.pojo.ValidateRule;

public class XmlUtils {
    public static final String validateXmlName = "excelValidation.xml";
    public static final String xmlPathInJar = "/resources/excelValidation.xml";
    public static final String xmlPathOutJar = "/excelValidation.xml";

    public static boolean isNull(String param) {
        if (param == null)
            return true;
        else if ("".equals(param))
            return true;
        else
            return false;
    }

    @SuppressWarnings("unchecked")
    public List<VSheet> getRuleByName(String xmlPath, String validatorName) {

        InputStream inStream = this.getClass().getResourceAsStream(xmlPath);
        SAXReader reader = new SAXReader();
        Document document = null;
        List<VSheet> jsheets = new ArrayList<VSheet>();
        try {
            document = reader.read(inStream);
            Element rootE = document.getRootElement();
            List<Element> validators = rootE.elements("validator");
            Element validateElement = null;
            for (Element e1 : validators) {
                if (validatorName.equals(e1.attributeValue("id"))) {
                    validateElement = e1;
                    break;
                }
            }
            List<Element> sheets = validateElement.elements("sheet");

            if (sheets != null && !sheets.isEmpty()) {
                for (Element sheetElement : sheets) {
                    VSheet jsheet = new VSheet();
                    String sheetIndex = sheetElement.attributeValue("sheetIndex");
                    String beginRow = sheetElement.attributeValue("beginRow");
                    String endRow = sheetElement.attributeValue("endRow");
                    if (isNull(sheetIndex))
                        throw new ValidateXmlException("校验文件sheetIndex属性不能为空");
                    if (isNull(beginRow))
                        throw new ValidateXmlException("校验文件beginRow属性不能为空");
                    jsheet.setBeginRow(isNull(beginRow) ? null : new Integer(beginRow));
                    jsheet.setEndRow(isNull(endRow) ? null : new Integer(endRow));
                    jsheet.setSheetIndex(isNull(sheetIndex) ? null : new Integer(sheetIndex));
                    jsheet.setColumns(this.getCoulumnRules(sheetElement));
                    jsheet.setUniqueKeys(this.getUniqueKeys(sheetElement));
                    jsheets.add(jsheet);
                }
            }
            return jsheets;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        return jsheets;

    }

    private List<UniqueKey> getUniqueKeys(Element sheetElement) {
        Element uniqueKeys = sheetElement.element("uniqueKeys");
        List<UniqueKey> keys = new ArrayList<UniqueKey>();
        List<Element> uniqueKeyElements = null;
        if (uniqueKeys != null) {
            uniqueKeyElements = uniqueKeys.elements("uniqueKey");
        }
        // HashMap<String, Integer> allKeys = new HashMap<String, Integer>();
        if (uniqueKeyElements != null && !uniqueKeyElements.isEmpty()) {
            for (Element e1 : uniqueKeyElements) {
                if (isNull(e1.attributeValue("uniqueFields")))
                    throw new ValidateXmlException("校验文件uniqueFields属性不能为空");
                if (isNull(e1.attributeValue("keyId")))
                    throw new ValidateXmlException("校验文件keyId属性不能为空");
                String keyId = e1.attributeValue("keyId");
                String[] StringFields = e1.attributeValue("uniqueFields").split("[,]");
                List<Integer> IntegerFields = new ArrayList<Integer>();
                for (int i = 0; i < StringFields.length; i++) {
                    IntegerFields.add(new Integer(StringFields[i]));
                }
                if (!IntegerFields.isEmpty()) {
                    UniqueKey keyEntity = new UniqueKey();
                    keyEntity.setUniqueColumn(IntegerFields);
                    keyEntity.setKeyName(keyId);
                    keys.add(keyEntity);

                }

            }

        }

        return keys;
    }

    @SuppressWarnings("unchecked")
    private List<ValidateRule> getCoulumnRules(Element sheetElement) {
        List<ValidateRule> columnRules = new ArrayList<ValidateRule>();
        HashMap<String, Integer> indexCheckMap = new HashMap<String, Integer>();
        for (Element e1 : (List<Element>) sheetElement.elements("column")) {
            String isNullAble = e1.attributeValue("isNullAble");
            String maxLength = e1.attributeValue("maxLength");

            String minLength = e1.attributeValue("maxLength");
            String rule = e1.attributeValue("rule");
            String className = e1.attributeValue("classType");
            String columnIndex = e1.attributeValue("rowIndex");
            // String scale = e1.attributeValue("scale");
            String dateFormat = e1.attributeValue("dateFormat");
            String maxIntLength = e1.attributeValue("maxIntLength");
            String minIntLength = e1.attributeValue("minIntLength");
            String minScaleLength = e1.attributeValue("minScaleLength");
            String maxScaleLength = e1.attributeValue("maxScaleLength");
            String name = e1.attributeValue("name");
            ValidateRule ruleEntity = new ValidateRule();
            ruleEntity.setIsNullAble(isNull(isNullAble) ? null : (isNullAble.equals("false") ? false : true));
            ruleEntity.setMaxLength(isNull(maxLength) ? null : new Integer(maxLength));
            ruleEntity.setMinLength(isNull(minLength) ? null : new Integer(minLength));
            ruleEntity.setRule(isNull(rule) ? null : new String(rule));
            // ruleEntity.setScale(isNull(scale) ? null : scale.split("[,]"));
            ruleEntity.setDateFormat(isNull(dateFormat) ? null : new String(dateFormat));
            ruleEntity.setMinIntLength(isNull(minIntLength) ? null : new Integer(minIntLength));
            ruleEntity.setMaxIntLength(isNull(maxIntLength) ? null : new Integer(maxIntLength));
            ruleEntity.setMinScaleLength(isNull(minScaleLength) ? null : new Integer(minScaleLength));
            ruleEntity.setMaxScaleLength(isNull(maxScaleLength) ? null : new Integer(maxScaleLength));
            ruleEntity.setName(isNull(name) ? null : new String(name));
            if (isNull(className))
                throw new ValidateXmlException("校验文件className属性不能为空");
            if (isNull(columnIndex))
                throw new ValidateXmlException("校验文件rowIndex属性不能为空");
            ruleEntity.setClassType(className);
            if (!indexCheckMap.containsKey(columnIndex)) {
                indexCheckMap.put(columnIndex, 0);
            } else {
                throw new ValidateXmlException("校验文件rowIndex属性不能重复");
            }
            ruleEntity.setColumnIndex(new Integer(columnIndex));
            columnRules.add(ruleEntity);
        }
        return columnRules;
    }

    public HashMap<String, ExcelConfig> getAllValidators() {
        InputStream inStream = null;
        String[] paths = { xmlPathInJar, xmlPathOutJar };
        HashMap<String, ExcelConfig> excelConfigs = new HashMap<String, ExcelConfig>();
        for (String xmlPath : paths) {
            try {
                inStream = this.getClass().getResourceAsStream(xmlPath);
                SAXReader reader = new SAXReader();
                Document document = null;
                document = reader.read(inStream);
                Element rootE = document.getRootElement();
                List<Element> validators = rootE.elements("validator");
                for (Element e1 : validators) {
                    ExcelConfig config = new ExcelConfig();
                    config.setValidatorName(e1.attributeValue("id"));
                    config.setExcelType(e1.attributeValue("type"));
                    config.setXmlPath(xmlPath);
                    excelConfigs.put(e1.attributeValue("id"), config);
                    // validatorAndPath.put(e1.attributeValue("type"), xmlPath);
                }
            } catch (Exception e) {
                continue;
            } finally {
                if (inStream != null)
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }
        }
        return excelConfigs;

    }

}

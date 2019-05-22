package org.vexcel.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vexcel.engine.RuleEngine;
import org.vexcel.exception.ValidateRuntimeException;
import org.vexcel.pojo.Message;
import org.vexcel.pojo.UniqueKey;
import org.vexcel.pojo.VSheet;
import org.vexcel.pojo.ValidateRule;

public class ExcelUtils {
    public static Logger log = Logger.getLogger(ExcelUtils.class);

    private static String getCellText(org.apache.poi.ss.usermodel.Cell cell) {
        String celltext = "";

        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_NUMERIC:
            celltext = "" + (int) cell.getNumericCellValue();
            break;
        case HSSFCell.CELL_TYPE_STRING:
            celltext = cell.getStringCellValue();
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            celltext = "";
            break;
        case HSSFCell.CELL_TYPE_ERROR:
            celltext = "";
            break;

        default:
            celltext = "";
            break;
        }

        return celltext;

    }

    public static Boolean readExcel(String excelLocalPath, List<VSheet> rules, JTextArea fileLabel, Message message,
            String excelType) {
        if ("xls".equals(excelType)) {
            return readExcel_XLS(excelLocalPath, rules, fileLabel, message, excelType);
        } else {
            return readExcel_XLSX(excelLocalPath, rules, fileLabel, message, excelType);
        }

    }

    public static Boolean readExcel_XLS(String excelLocalPath, List<VSheet> rules, JTextArea fileLabel, Message message,
            String excelType) {
        Integer excelCounts = 0;
        int count = 0;
        boolean excelRight = true;
        for (VSheet sheet : rules) {
            List<ValidateRule> coumnRules = sheet.getColumns();
            HashMap<Integer, ValidateRule> coumnRules_Map = new HashMap<Integer, ValidateRule>();
            List<UniqueKey> uniqueKeys = sheet.getUniqueKeys();
            List<Object> rowKeys = new ArrayList();
            for (ValidateRule columnRow : coumnRules) {
                rowKeys.add(columnRow.getColumnIndex());
                coumnRules_Map.put(new Integer(columnRow.getColumnIndex()), columnRow);
            }

            InputStream is = null;

            BigDecimal b = BigDecimal.ZERO;
            b.setScale(4);
            try {
                is = new FileInputStream(excelLocalPath);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                excelRight = false;
                log.error("解析excel失败，未找到目标文件");
                log.error(e);
                throw new ValidateRuntimeException(e.toString());
            }
            HSSFWorkbook hssfworkbook = null;
            try {
                hssfworkbook = new HSSFWorkbook(is);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                excelRight = false;
                log.error("解析工作表失败");
                log.error(e);
                throw new ValidateRuntimeException(e.toString());
            }
            HSSFSheet hssfsheet = hssfworkbook.getSheetAt(sheet.getSheetIndex());
            int rows = hssfsheet.getLastRowNum();

            HashMap<String, Integer> countIdt = new HashMap(rows * uniqueKeys.size() + 10, 1F);

            int endRow = sheet.getEndRow();
            if (sheet.getEndRow() != null && hssfsheet.getLastRowNum() > endRow) {
                excelRight = false;
                log.error("解析工作表失败:表格sheet数据不能超过" + sheet.getEndRow() + "条");
            }
            excelCounts += (hssfsheet.getLastRowNum() - sheet.getBeginRow() + 1);
            try {

                for (int rowNum = sheet.getBeginRow(); rowNum <= hssfsheet.getLastRowNum(); rowNum++) {

                    HSSFRow hssfRow = hssfsheet.getRow(rowNum);
                    for (Object key : rowKeys) {
                        if (hssfRow.getCell((Integer) key) == null) {
                            hssfRow.createCell((Integer) key);
                            hssfRow.getCell((Integer) key).setCellType(Cell.CELL_TYPE_STRING);
                            hssfRow.getCell((Integer) key).setCellValue("");
                        }
                        Cell cell = hssfRow.getCell((Integer) key);
                        String cellText = getCellText(cell);

                        Message msg = RuleEngine.process(cellText, coumnRules_Map.get(key));
                        if (!msg.isSuccess()) {
                            log.error("第" + (rowNum + 1) + "行:" + msg.getMsg());
                            excelRight = false;
                        }
                        b = new BigDecimal(rowNum - 1).setScale(4, RoundingMode.HALF_UP)
                                .divide(new BigDecimal(hssfsheet.getLastRowNum() - 1), 4, RoundingMode.HALF_UP);

                        fileLabel.setText("excel校验中," + "sheet" + sheet.getSheetIndex() + ",进度:"
                                + new BigDecimal(b.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP) + "%");
                    }
                    count++;
                    for (UniqueKey uniqueRule : uniqueKeys) {
                        List<Integer> keyRows = uniqueRule.getUniqueColumn();
                        String keyString = uniqueRule.getKeyName();
                        for (Integer key : keyRows) {
                            if (hssfRow.getCell((Integer) key) == null) {
                                hssfRow.createCell((Integer) key);
                                hssfRow.getCell((Integer) key).setCellType(Cell.CELL_TYPE_STRING);
                                hssfRow.getCell((Integer) key).setCellValue("");
                            }
                            Cell cell = hssfRow.getCell((Integer) key);
                            String cellText = getCellText(cell);
                            if (CommonUtil.isNull(cellText)) {
                                keyString = "";
                                break;
                            }
                            keyString += "--" + cellText;
                        }
                        log.error(keyString);
                        if (!CommonUtil.isNull(keyString)) {
                            if (countIdt.containsKey(keyString)) {
                                log.error("第" + (rowNum + 1) + "行:" + "唯一性约束不通过，" + keyString + "表格内已存在");
                                excelRight = false;
                            } else {
                                countIdt.put(keyString, new Integer(1));
                            }
                        }

                    }

                }
            } catch (Exception e) {
                log.error(e.toString());
                e.printStackTrace();
                excelRight = false;
            } finally {
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }

        }
        log.error("RESULT:" + String.valueOf(excelRight) + " TOTAL:" + String.valueOf(excelCounts) + " SCANED:"
                + String.valueOf(count));
        if (count == excelCounts && excelRight)
            excelRight = true;
        else {
            excelRight = false;
            message.setMsg("扫描失败，请查看日志文件修改excel");
        }

        message.setSuccess(excelRight);
        return excelRight;
    }

    public static Boolean readExcel_XLSX(String excelLocalPath, List<VSheet> rules, JTextArea fileLabel,
            Message message, String excelType) {
        Integer excelCounts = 0;
        int count = 0;
        boolean excelRight = true;
        for (VSheet sheet : rules) {
            List<ValidateRule> coumnRules = sheet.getColumns();
            HashMap<Integer, ValidateRule> coumnRules_Map = new HashMap<Integer, ValidateRule>();
            List<UniqueKey> uniqueKeys = sheet.getUniqueKeys();
            List<Object> rowKeys = new ArrayList();
            for (ValidateRule columnRow : coumnRules) {
                rowKeys.add(columnRow.getColumnIndex());
                coumnRules_Map.put(new Integer(columnRow.getColumnIndex()), columnRow);
            }

            InputStream is = null;

            BigDecimal b = BigDecimal.ZERO;
            b.setScale(4);
            try {
                is = new FileInputStream(excelLocalPath);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                excelRight = false;
                log.error("解析excel失败，未找到目标文件");
                log.error(e);
                throw new ValidateRuntimeException(e.toString());
            }
            XSSFWorkbook hssfworkbook = null;
            try {
                hssfworkbook = new XSSFWorkbook(is);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                excelRight = false;
                log.error("解析工作表失败");
                log.error(e);
                throw new ValidateRuntimeException(e.toString());
            }
            XSSFSheet hssfsheet = hssfworkbook.getSheetAt(sheet.getSheetIndex());
            int rows = hssfsheet.getLastRowNum();

            HashMap<String, Integer> countIdt = new HashMap(rows * uniqueKeys.size() + 10, 1F);

            int endRow = sheet.getEndRow();
            if (sheet.getEndRow() != null && hssfsheet.getLastRowNum() > endRow) {
                excelRight = false;
                log.error("解析工作表失败:表格sheet数据不能超过" + sheet.getEndRow() + "条");
            }
            excelCounts += (hssfsheet.getLastRowNum() - sheet.getBeginRow() + 1);
            try {

                for (int rowNum = sheet.getBeginRow(); rowNum <= hssfsheet.getLastRowNum(); rowNum++) {

                    XSSFRow hssfRow = hssfsheet.getRow(rowNum);

                    for (Object key : rowKeys) {
                        if (hssfRow.getCell((Integer) key) == null) {
                            hssfRow.createCell((Integer) key);
                            hssfRow.getCell((Integer) key).setCellType(Cell.CELL_TYPE_STRING);
                            hssfRow.getCell((Integer) key).setCellValue("");
                        }
                        Cell cell = hssfRow.getCell((Integer) key);
                        String cellText = getCellText(cell);

                        Message msg = RuleEngine.process(cellText, coumnRules_Map.get(key));
                        if (!msg.isSuccess()) {
                            log.error("第" + (rowNum + 1) + "行:" + msg.getMsg());
                            excelRight = false;
                        }
                        b = new BigDecimal(rowNum - 1).setScale(4, RoundingMode.HALF_UP)
                                .divide(new BigDecimal(hssfsheet.getLastRowNum() - 1), 4, RoundingMode.HALF_UP);

                        fileLabel.setText("excel校验中," + "sheet" + sheet.getSheetIndex() + ",进度:"
                                + new BigDecimal(b.doubleValue() * 100).setScale(2, RoundingMode.HALF_UP) + "%");
                    }
                    count++;
                    for (UniqueKey uniqueRule : uniqueKeys) {
                        List<Integer> keyRows = uniqueRule.getUniqueColumn();
                        String keyString = uniqueRule.getKeyName();
                        for (Integer key : keyRows) {
                            if (hssfRow.getCell((Integer) key) == null) {
                                hssfRow.createCell((Integer) key);
                                hssfRow.getCell((Integer) key).setCellType(Cell.CELL_TYPE_STRING);
                                hssfRow.getCell((Integer) key).setCellValue("");
                            }
                            Cell cell = hssfRow.getCell((Integer) key);
                            String cellText = getCellText(cell);
                            if (CommonUtil.isNull(cellText)) {
                                keyString = "";
                                break;
                            }
                            keyString += "--" + cellText;
                        }
                        log.error(keyString);
                        if (!CommonUtil.isNull(keyString)) {
                            if (countIdt.containsKey(keyString)) {
                                log.error("第" + (rowNum + 1) + "行:" + "唯一性约束不通过，" + keyString + "表格内已存在");
                                excelRight = false;
                            } else {
                                countIdt.put(keyString, new Integer(1));
                            }
                        }

                    }

                }
            } catch (Exception e) {
                log.error(e.toString());
                e.printStackTrace();
                excelRight = false;
            } finally {
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }

        }
        log.error("RESULT:" + String.valueOf(excelRight) + " TOTAL:" + String.valueOf(excelCounts) + " SCANED:"
                + String.valueOf(count));
        if (count == excelCounts && excelRight)
            excelRight = true;
        else {
            excelRight = false;
            message.setMsg("扫描失败，请查看日志文件修改excel");
        }

        message.setSuccess(excelRight);
        return excelRight;
    }

}

package org.vexcel.engine;

import java.text.SimpleDateFormat;

import org.vexcel.factory.MessageFactory;
import org.vexcel.pojo.Message;
import org.vexcel.pojo.ValidateRule;

public class RuleMethods {

    public static <T> boolean isNull(T param) {
        if (param == null)

            return true;
        else if ("".equals(param))
            return true;
        else
            return false;
    }

    public static Message isNullRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(rule.getIsNullAble())) {
            if (!rule.getIsNullAble()) {
                if (isNull(excelCellString)) {
                    msg.setMsg("非空校验规则不通过. " + "字段：" + "[" + rule.getName() + "]" + "不能为空" + "\n");
                    msg.setSuccess(false);

                }

            }
        }
        return msg;
    }

    public static Message maxLengthRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(rule.getMaxLength())) {
            if (!isNull(excelCellString) && excelCellString.length() > rule.getMaxLength()) {
                msg.setMsg("字符长度规则不通过," + "字段：" + "[" + rule.getName() + "]" + "长度大于" + rule.getMaxLength() + "\n");
                msg.setSuccess(false);
            }
        }
        return msg;
    }

    public static Message minLengthRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(rule.getMinLength())) {
            if (isNull(excelCellString) && rule.getMinLength() > 0) {
                // log
                msg.setMsg("字符长度规则不通过," + "字段：" + "[" + rule.getName() + "]" + "长度小于" + rule.getMinLength() + "\n");
                msg.setSuccess(false);
            } else if (!isNull(excelCellString) && excelCellString.length() <= rule.getMinLength()) {
                msg.setMsg("字符长度规则不通过," + "字段：" + "[" + rule.getName() + "]" + "长度小于" + rule.getMinLength() + "\n");
                msg.setSuccess(false);
                // log
            }
        }
        return msg;
    }

    public static Message regexRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(excelCellString) && !isNull(rule.getRule())) {
            if (!excelCellString.matches(rule.getRule())) {
                msg.setMsg("字符格式规则不通过," + "字段：" + "[" + rule.getName() + "]" + "数据格式错误" + "\n");
                msg.setSuccess(false);
            }
        }
        return msg;
    }

    public static Message IntegerRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(excelCellString)) {
            if (!excelCellString.matches("^\\d+") && String.valueOf(Integer.MAX_VALUE).compareTo(excelCellString) > 0) {
                msg.setMsg("整型规则不通过," + "字段：" + "[" + rule.getName() + "]" + "数据格式错误或数字过大" + "\n");
                msg.setSuccess(false);
            }
        }
        return msg;
    }

    public static Message LongRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(excelCellString)) {
            if (!excelCellString.matches("^\\d+") && String.valueOf(Long.MAX_VALUE).compareTo(excelCellString) > 0) {
                msg.setMsg("长整型规则不通过," + "字段：" + "[" + rule.getName() + "]" + "数据格式错误或数字过大" + "\n");
                msg.setSuccess(false);
            }
        }
        return msg;
    }

    public static Message classTypeRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(excelCellString)) {

            try {
                Class.forName(rule.getClassType()).getConstructor(String.class).newInstance(excelCellString);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                msg.setMsg("字符类型规则不通过," + "字段：" + "[" + rule.getName() + "]" + "数据格式错误" + "\n");
                msg.setSuccess(false);
            }

        }
        return msg;
    }

    public static Message dateFormatRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(excelCellString) && !isNull(rule.getDateFormat())) {

            try {
                new SimpleDateFormat(rule.getDateFormat()).parse(excelCellString);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                msg.setMsg("日期格式规则不通过," + "字段：" + "[" + rule.getName() + "]" + "数据格式错误" + "\n");
                msg.setSuccess(false);
            }

        }
        return msg;
    }

    public static Message float_maxIntegerLegthRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(excelCellString) && !isNull(rule.getMaxIntLength())) {
            String max_intLength = rule.getMaxIntLength().toString();
            String floatRegex = "^\\d{0," + max_intLength + "}([.]\\d+}){0,1}$";
            if (!excelCellString.matches(floatRegex)) {
                msg.setMsg("浮点数规则不通过," + "字段：" + "[" + rule.getName() + "]" + "数据（浮点数字）整数部分长度过长" + "\n");
                msg.setSuccess(false);
            }

        }
        return msg;
    }

    public static Message float_minIntegerLegthRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(excelCellString) && !isNull(rule.getMinIntLength())) {
            String min_intLength = rule.getMinIntLength().toString();
            String floatRegex = "^\\d{" + min_intLength + ",}([.]\\d+}){0,1}$";
            if (!excelCellString.matches(floatRegex)) {
                msg.setMsg("浮点数规则不通过," + "字段：" + "[" + rule.getName() + "]" + "数据（浮点数字）整数部分长度过短" + "\n");
                msg.setSuccess(false);
            }

        }
        return msg;
    }

    public static Message float_minScaleLegthRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(excelCellString) && !isNull(rule.getMinScaleLength())) {
            String min_scaleLength = rule.getMinScaleLength().toString();
            String floatRegex = null;
            if (rule.getMinScaleLength().intValue() <= 0)
                floatRegex = "^([.]\\d{" + min_scaleLength + ",}){0,1}$";
            else
                floatRegex = "^\\d+([.]\\d{" + min_scaleLength + ",}){1}$";
            if (!excelCellString.matches(floatRegex)) {
                msg.setMsg("精度规则不通过," + "字段：" + "[" + rule.getName() + "]" + "数据（浮点数字）精度过低" + "\n");
                msg.setSuccess(false);
            }

        }
        return msg;

    }

    public static Message float_maxScaleLegthRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(excelCellString) && !isNull(rule.getMaxScaleLength())) {
            String floatRegex = null;
            if (rule.getMaxScaleLength().intValue() <= 0)
                floatRegex = "^\\d+$";
            else
                floatRegex = "^\\d+([.]\\d{0," + rule.getMaxScaleLength().toString() + "}){0,1}$";
            if (!excelCellString.matches(floatRegex)) {
                msg.setMsg("精度规则不通过," + "字段：" + "[" + rule.getName() + "]" + "数据（浮点数字）精度过高" + "\n");
                msg.setSuccess(false);
            }

        }
        return msg;
    }

    public static Message floatNumRule(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        if (!isNull(excelCellString)) {
            String floatRegex = "^\\d+([.]\\d+){0,1}$";
            if (!excelCellString.matches(floatRegex)) {
                msg.setMsg("浮点数规则不通过," + "字段：" + "[" + rule.getName() + "]" + "数据非浮点数" + "\n");
                msg.setSuccess(false);
            }

        }
        return msg;
    }

}

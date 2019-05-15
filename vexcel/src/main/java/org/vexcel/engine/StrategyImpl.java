package org.vexcel.engine;

import org.vexcel.factory.MessageFactory;
import org.vexcel.pojo.Message;
import org.vexcel.pojo.ValidateRule;

public class StrategyImpl implements RuleStrategy {

    // private static Method[] stringStrategy = {RuleMethods.class.getMethod("", String.class,Rule.class)};

    public Message matchStrategy(String excelCellString, ValidateRule rule) {
        // TODO Auto-generated method stub
        if (rule.getClassType().equals("java.lang.String")) {
            return this.stringStrategy(excelCellString, rule);
        } else if (rule.getClassType().equals("java.lang.Integer")) {
            return this.integerStrategy(excelCellString, rule);
        } else if (rule.getClassType().equals("java.lang.Long")) {
            return this.longStrategy(excelCellString, rule);
        } else if (rule.getClassType().equals("java.util.Date")) {
            return this.dateStrategy(excelCellString, rule);
        } else if (rule.getClassType().equals("java.lang.Float") || rule.getClassType().equals("java.lang.Double")
                || rule.getClassType().equals("java.math.BigDecimal")) {
            return this.floatStrategy(excelCellString, rule);
        } else {
            throw new RuntimeException("不支持的数据类型");
        }
    }

    public Message baseStrategy(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        msg = RuleMethods.isNullRule(excelCellString, rule);
        if (msg.isSuccess())
            msg = RuleMethods.minLengthRule(excelCellString, rule);
        else
            return msg;
        if (msg.isSuccess())
            msg = RuleMethods.maxLengthRule(excelCellString, rule);
        else
            return msg;
        if (msg.isSuccess())
            msg = RuleMethods.regexRule(excelCellString, rule);
        else
            return msg;

        return msg;
    }

    public Message integerStrategy(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        msg = baseStrategy(excelCellString, rule);
        if (msg.isSuccess())
            msg = RuleMethods.IntegerRule(excelCellString, rule);
        else
            return msg;
        if (msg.isSuccess())
            msg = RuleMethods.classTypeRule(excelCellString, rule);
        else
            return msg;
        return msg;
    }

    public Message longStrategy(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        msg = baseStrategy(excelCellString, rule);
        if (msg.isSuccess())
            msg = RuleMethods.LongRule(excelCellString, rule);
        else
            return msg;
        if (msg.isSuccess())
            msg = RuleMethods.classTypeRule(excelCellString, rule);
        else
            return msg;
        return msg;
    }

    public Message stringStrategy(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        msg = baseStrategy(excelCellString, rule);
        if (msg.isSuccess())
            msg = RuleMethods.classTypeRule(excelCellString, rule);
        else
            return msg;
        if (msg.isSuccess())
            msg = RuleMethods.regexRule(excelCellString, rule);
        else
            return msg;
        return msg;
    }

    public Message dateStrategy(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        msg = baseStrategy(excelCellString, rule);
        if (msg.isSuccess())
            msg = RuleMethods.dateFormatRule(excelCellString, rule);
        else
            return msg;

        return msg;
    }

    public Message floatStrategy(String excelCellString, ValidateRule rule) {
        Message msg = MessageFactory.getMessage();
        msg = RuleMethods.isNullRule(excelCellString, rule);
        if (msg.isSuccess())
            msg = RuleMethods.regexRule(excelCellString, rule);
        else
            return msg;

        if (msg.isSuccess())
            msg = RuleMethods.floatNumRule(excelCellString, rule);

        else
            return msg;
        if (msg.isSuccess())
            msg = RuleMethods.float_maxIntegerLegthRule(excelCellString, rule);

        else
            return msg;
        if (msg.isSuccess())
            msg = RuleMethods.float_maxScaleLegthRule(excelCellString, rule);

        else
            return msg;
        if (msg.isSuccess())
            msg = RuleMethods.float_minIntegerLegthRule(excelCellString, rule);

        else
            return msg;
        if (msg.isSuccess())
            msg = RuleMethods.float_minScaleLegthRule(excelCellString, rule);

        else
            return msg;

        if (msg.isSuccess())
            msg = RuleMethods.classTypeRule(excelCellString, rule);

        else
            return msg;

        return msg;
    }

}

package org.vexcel.engine;

import org.vexcel.pojo.Message;
import org.vexcel.pojo.ValidateRule;

public interface RuleStrategy {
    public Message matchStrategy(String excelCellString, ValidateRule rule);
}

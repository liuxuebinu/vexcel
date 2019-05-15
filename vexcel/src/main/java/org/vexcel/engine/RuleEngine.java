package org.vexcel.engine;

import org.vexcel.pojo.Message;
import org.vexcel.pojo.ValidateRule;

public class RuleEngine {

    public static Message process(String excelCellString, ValidateRule rule) {
        return new StrategyImpl().matchStrategy(excelCellString, rule);
    }

}

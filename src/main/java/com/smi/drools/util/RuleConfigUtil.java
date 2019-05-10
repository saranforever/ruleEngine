package com.smi.drools.util;

import com.smi.drools.model.ActionBuilder;
import com.smi.drools.model.ConditionBuilder;
import com.smi.drools.model.RuleBuilder;
import com.smi.drools.model.RuleConfig;

public class RuleConfigUtil {
	
	private static final String DROOL_PACKAGE = "package com.smi.drools;\r\n";
	
	private static final String DROOL_MODEL_PACKAGE = "import com.smi.drools.model.%s;\r\n";
	
	private static final String DROOL_RULE_NAME = "rule \"%s\"\r\n";
	
	private static final String DROOL_WHEN = "\twhen\r\n";
	
	private static final String DROOL_WHEN_BUILDER = "\t\t%s : %s(%s)\r\n";
	
	private static final String DROOL_THEN = "\tthen\r\n";
	
	private static final String DROOL_THEN_BUILDER = "\t\tSystem.out.println(%s);\r\n";
	
	private static final String DROOL_END = "end\r\n\n";
	
	/**
	 * @param ruleConfig
	 * @return
	 */
	public static String buildRule(RuleConfig ruleConfig) {
		
		String rule = DROOL_PACKAGE;
		rule += String.format(DROOL_MODEL_PACKAGE, ruleConfig.getModelType()); 
		
		for (RuleBuilder ruleBuilder : ruleConfig.getRuleBuilders()) {
			String conditions = "";
			rule += String.format(DROOL_RULE_NAME, ruleBuilder.getRuleName());
			rule += DROOL_WHEN;
			
			for (ConditionBuilder conditionBuilder : ruleBuilder.getConditionBuilders()) {
				conditions += conditionBuilder.getKey();
				
				if (conditionBuilder.getFilter().equalsIgnoreCase("equals")) {
					conditions += " == ";
				} else if (conditionBuilder.getFilter().equalsIgnoreCase("notequals")) {
					conditions += " != ";
				}
				conditions += "'"+conditionBuilder.getValue()+"'"; 
				
				if (conditionBuilder.getCondition() != null && !conditionBuilder.getCondition().isEmpty()) {
					if (conditionBuilder.getCondition().equalsIgnoreCase("and")) {
						conditions += " && ";
					}
					
					if (conditionBuilder.getCondition().equalsIgnoreCase("or")) {
						conditions += " || ";
					}
				}
			}
			
			rule += String.format(DROOL_WHEN_BUILDER, ruleConfig.getModelType().toLowerCase(), ruleConfig.getModelType(), conditions);
			rule += DROOL_THEN;
			for (ActionBuilder actionBuilder : ruleBuilder.getActionBuilders()) {
				rule += String.format(DROOL_THEN_BUILDER, actionBuilder.getValue());
			}
			rule += DROOL_END;
		}
		
		return rule;
	}
	
}

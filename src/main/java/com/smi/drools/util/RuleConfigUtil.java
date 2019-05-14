package com.smi.drools.util;

import com.smi.drools.model.RuleConfig;

public class RuleConfigUtil {

	private static final String DROOL_PACKAGE = "package com.smi.drools;\r\n";

	private static final String DROOL_MODEL_PACKAGE = "import com.smi.drools.model.%s;\r\n";

	private static final String DROOL_RULE_NAME = "rule \"%s\"\r\n";

	private static final String DROOL_WHEN = "\twhen\r\n";

	private static final String DROOL_WHEN_BUILDER = "\t\t%s : %s(%s)\r\n";

	private static final String DROOL_CLASS_OBJECT = "\t\t%s : %s();\r\n";

	private static final String DROOL_THEN = "\tthen\r\n";

	private static final String DROOL_THEN_BUILDER = "\t\t%s.%s;\r\n";

	private static final String DROOL_END = "end\r\n\n";
	
	private RuleConfigUtil() {
		
	}

	/**
	 * @param ruleConfig
	 * @return
	 */
	public static String buildRule(RuleConfig ruleConfig) {

		StringBuilder ruleStrBuilder = new StringBuilder();
		ruleStrBuilder.append(DROOL_PACKAGE);

		ruleStrBuilder.append(String.format(DROOL_MODEL_PACKAGE, ruleConfig.getModelType()));
		
		ruleConfig.getRuleBuilders().stream().forEach(ruleBuilder -> {
			StringBuilder conditionStrBuilder = new StringBuilder();
			ruleStrBuilder.append(String.format(DROOL_RULE_NAME, ruleBuilder.getRuleName()));
			ruleStrBuilder.append(DROOL_WHEN);
			ruleBuilder.getConditionBuilders().stream().forEach(conditionBuilder -> {
				conditionStrBuilder.append(conditionBuilder.getKey());
				
				if (conditionBuilder.getFilter().equalsIgnoreCase("equals")) {
					conditionStrBuilder.append(" == ");
				} else if (conditionBuilder.getFilter().equalsIgnoreCase("notequals")) {
					conditionStrBuilder.append(" != ");
				}
				conditionStrBuilder.append("'" + conditionBuilder.getValue() + "'");
				
				if (conditionBuilder.getCondition() != null && !conditionBuilder.getCondition().isEmpty()) {
					if (conditionBuilder.getCondition().equalsIgnoreCase("and")) {
						conditionStrBuilder.append(" && ");
					}

					if (conditionBuilder.getCondition().equalsIgnoreCase("or")) {
						conditionStrBuilder.append(" || ");
					}
				}
			});
			ruleStrBuilder.append(String.format(DROOL_WHEN_BUILDER, ruleConfig.getModelType().toLowerCase(),
					ruleConfig.getModelType(), conditionStrBuilder.toString()));
			ruleBuilder.getActionBuilders().stream().forEach(actionBuilder -> {
				int i = 0;
				ruleStrBuilder.append(String.format(DROOL_CLASS_OBJECT,
						actionBuilder.getKey().className().toLowerCase(), actionBuilder.getKey().packageName()));
				if (i == 0) {
					ruleStrBuilder.append(DROOL_THEN);
				}
				ruleStrBuilder
						.append(String.format(DROOL_THEN_BUILDER, actionBuilder.getKey().className().toLowerCase(),
								actionBuilder.getValue() + "(" + ruleConfig.getModelType().toLowerCase() + ")"));
				i++;
			});
			ruleStrBuilder.append(DROOL_END);
		});
		
		/*for (RuleBuilder ruleBuilder : ruleConfig.getRuleBuilders()) {
			ruleStrBuilder.append(String.format(DROOL_RULE_NAME, ruleBuilder.getRuleName()));
			ruleStrBuilder.append(DROOL_WHEN);

			for (ConditionBuilder conditionBuilder : ruleBuilder.getConditionBuilders()) {
				conditionStrBuilder.append(conditionBuilder.getKey());

				if (conditionBuilder.getFilter().equalsIgnoreCase("equals")) {
					conditionStrBuilder.append(" == ");
				} else if (conditionBuilder.getFilter().equalsIgnoreCase("notequals")) {
					conditionStrBuilder.append(" != ");
				}
				conditionStrBuilder.append("'" + conditionBuilder.getValue() + "'");

				if (conditionBuilder.getCondition() != null && !conditionBuilder.getCondition().isEmpty()) {
					if (conditionBuilder.getCondition().equalsIgnoreCase("and")) {
						conditionStrBuilder.append(" && ");
					}

					if (conditionBuilder.getCondition().equalsIgnoreCase("or")) {
						conditionStrBuilder.append(" || ");
					}
				}
			}

			ruleStrBuilder.append(String.format(DROOL_WHEN_BUILDER, ruleConfig.getModelType().toLowerCase(),
					ruleConfig.getModelType(), conditionStrBuilder.toString()));
			int i = 0;
			for (ActionBuilder actionBuilder : ruleBuilder.getActionBuilders()) {

				ruleStrBuilder.append(String.format(DROOL_CLASS_OBJECT,
						actionBuilder.getKey().className().toLowerCase(), actionBuilder.getKey().packageName()));
				if (i == 0) {
					ruleStrBuilder.append(DROOL_THEN);
				}
				ruleStrBuilder
						.append(String.format(DROOL_THEN_BUILDER, actionBuilder.getKey().className().toLowerCase(),
								actionBuilder.getValue() + "(" + ruleConfig.getModelType().toLowerCase() + ")"));
				i++;
			}
			ruleStrBuilder.append(DROOL_END);
		}*/

		return ruleStrBuilder.toString();
	}

}

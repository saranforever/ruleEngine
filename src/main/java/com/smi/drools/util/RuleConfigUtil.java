package com.smi.drools.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.smi.drools.enumutil.ConditionalEnum;
import com.smi.drools.enumutil.EnrichmentEnum;
import com.smi.drools.enumutil.FilterEnum;
import com.smi.drools.enumutil.ModelTypeEnum;
import com.smi.drools.model.RuleConfig;

public class RuleConfigUtil {

	private static final String DROOL_PACKAGE = "package com.smi.drools;\r\n";

	/*private static final String DROOL_MODEL_PACKAGE = "%s;\r\n";*/

	private static final String DROOL_RULE_NAME = "rule \"%s\"\r\n";
	
	private static final String DROOL_GROUP_NAME = "\t\tagenda-group \"%s\"\r\n";
	
	private static final String DROOL_WHEN = "\twhen\r\n";

	private static final String DROOL_FACT_INITIALIZER = "\t\t%s : %s()\r\n";
	
	private static final String DROOL_WHEN_BUILDER = "\t\t%s\r\n";

	private static final String DROOL_CLASS_OBJECT = "\t\t%s : %s();\r\n";

	private static final String DROOL_THEN = "\tthen\r\n";

	private static final String DROOL_THEN_BUILDER = "\t\t%s.%s;\r\n";
	
	private static final String DROOL_RULE_FOCUS = "\t\tdrools.setFocus(\"%s\");\r\n";
	
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

		ruleConfig.getRuleBuilders().stream().forEach(ruleBuilder -> {
			StringBuilder conditionStrBuilder = new StringBuilder();
			ruleStrBuilder.append(String.format(DROOL_RULE_NAME, ruleBuilder.getRuleName()));
			if (StringUtils.isNotEmpty(ruleBuilder.getRuleGroupName())) {
				ruleStrBuilder.append(String.format(DROOL_GROUP_NAME, ruleBuilder.getRuleGroupName()));
			}
			ruleStrBuilder.append(DROOL_WHEN);
			conditionStrBuilder.append(String.format(DROOL_FACT_INITIALIZER,
					ruleBuilder.getModelType().className().toLowerCase(), ruleBuilder.getModelType().packageName()));
			
			Optional.ofNullable(ruleBuilder.getConditionBuilders()).ifPresent(cb -> cb.forEach(conditionBuilder -> {
				ModelTypeEnum modelType = conditionBuilder.getModelType();
				
				conditionStrBuilder.append(modelType.packageName() + "(");
				
				FilterEnum conditionFilter = conditionBuilder.getFilter();
				String symbol = conditionFilter.symbol();
				conditionStrBuilder.append(conditionBuilder.getMetaField());
				conditionStrBuilder.append(symbol);
				conditionStrBuilder.append("'" + conditionBuilder.getMetaValue() + "')");
				
				ConditionalEnum condition = conditionBuilder.getConditionOperator();
				if (condition != null) {
					conditionStrBuilder.append(condition.symbol());
				}
			}));
			
			ruleStrBuilder.append(String.format(DROOL_WHEN_BUILDER, conditionStrBuilder));
			List<EnrichmentEnum> enrichmentEnums = new ArrayList<>();
			
			int[] counter = {0};
			StringBuilder thenBuilder = new StringBuilder();
			ruleBuilder.getActionBuilders().stream().forEach(actionBuilder -> {
				EnrichmentEnum enrichmentEnum = actionBuilder.getEnrichement();

				if (!enrichmentEnums.contains(enrichmentEnum) && !enrichmentEnum.equals(EnrichmentEnum.RULE)) {
					ruleStrBuilder.append(String.format(DROOL_CLASS_OBJECT, enrichmentEnum.className().toLowerCase(),
							enrichmentEnum.packageName()));
					enrichmentEnums.add(enrichmentEnum);
				}

				if (counter[0] == ruleBuilder.getActionBuilders().size() - 1) {
					ruleStrBuilder.append(DROOL_THEN);
				}

				if (!enrichmentEnum.equals(EnrichmentEnum.RULE)) {
					thenBuilder.append(String.format(DROOL_THEN_BUILDER, enrichmentEnum.className().toLowerCase(),
							actionBuilder.getEnrichmentAction() + "(" + enrichmentEnum.parameterName() + ")"));
				} else {
					thenBuilder.append(String.format(DROOL_RULE_FOCUS, actionBuilder.getEnrichmentAction()));
				}
				counter[0] = counter[0]+1;
			});
			
			ruleStrBuilder.append(thenBuilder);
			ruleStrBuilder.append(DROOL_END);
		});
		
		return ruleStrBuilder.toString();
	}

}

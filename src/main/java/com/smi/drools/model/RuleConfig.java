package com.smi.drools.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smi.drools.enumutil.EnrichmentEnum;

public class RuleConfig {

	private String ruleDescription;
	private String modelType;

	private int salience;

	private List<RuleBuilder> ruleBuilders;

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public int getSalience() {
		return salience;
	}

	public void setSalience(int salience) {
		this.salience = salience;
	}

	public List<RuleBuilder> getRuleBuilders() {
		return ruleBuilders;
	}

	public void setRuleBuilders(List<RuleBuilder> ruleBuilders) {
		this.ruleBuilders = ruleBuilders;
	}

	public static void main(String[] args) {
		RuleConfig ruleConfig = new RuleConfig();
		ruleConfig.setModelType("DocumentContext");

		List<RuleBuilder> ruleBuilders = new ArrayList<RuleBuilder>();
		// Rule 1
		RuleBuilder ruleBuilder = new RuleBuilder();
		ruleBuilder.setRuleName("drule1");
		List<ConditionBuilder> conditionBuilders = new ArrayList<ConditionBuilder>();

		ConditionBuilder conditionBuilder = new ConditionBuilder();
		conditionBuilder.setKey("documentType");
		conditionBuilder.setFilter("equals");
		conditionBuilder.setValue("EDI");
		conditionBuilder.setCondition("and");
		conditionBuilders.add(conditionBuilder);

		ConditionBuilder conditionBuilder1 = new ConditionBuilder();
		conditionBuilder1.setKey("documentVersion");
		conditionBuilder1.setFilter("equals");
		conditionBuilder1.setValue("00401");
		conditionBuilder1.setCondition("or");
		conditionBuilders.add(conditionBuilder1);

		ConditionBuilder conditionBuilder2 = new ConditionBuilder();
		conditionBuilder2.setKey("senderEid");
		conditionBuilder2.setFilter("notequals");
		conditionBuilder2.setValue("1123");
		conditionBuilders.add(conditionBuilder2);

		ruleBuilder.setConditionBuilders(conditionBuilders);

		List<ActionBuilder> actionBuilders = new ArrayList<ActionBuilder>();

		ActionBuilder actionBuilder = new ActionBuilder();
		actionBuilder.setKey(EnrichmentEnum.BUSINESSRULEENRICHMENT);
		actionBuilder.setValue("123");
		actionBuilders.add(actionBuilder);

		ActionBuilder actionBuilder1 = new ActionBuilder();
		actionBuilder1.setKey(EnrichmentEnum.BUSINESSRULEENRICHMENT);
		actionBuilder1.setValue("321");
		actionBuilders.add(actionBuilder1);

		ruleBuilder.setActionBuilders(actionBuilders);

		ruleBuilders.add(ruleBuilder);

		// Rule 2
		RuleBuilder ruleBuilder1 = new RuleBuilder();
		ruleBuilder1.setRuleName("drule2");
		List<ConditionBuilder> conditionBuilders1 = new ArrayList<ConditionBuilder>();

		ConditionBuilder conditionBuilder11 = new ConditionBuilder();
		conditionBuilder11.setKey("documentType");
		conditionBuilder11.setFilter("equals");
		conditionBuilder11.setValue("EDI");
		conditionBuilder11.setCondition("and");
		conditionBuilders1.add(conditionBuilder11);

		ConditionBuilder conditionBuilder12 = new ConditionBuilder();
		conditionBuilder12.setKey("documentVersion");
		conditionBuilder12.setFilter("equals");
		conditionBuilder12.setValue("00401");
		conditionBuilder12.setCondition("or");
		conditionBuilders1.add(conditionBuilder12);

		ConditionBuilder conditionBuilder13 = new ConditionBuilder();
		conditionBuilder13.setKey("senderEid");
		conditionBuilder13.setFilter("notequals");
		conditionBuilder13.setValue("1124");
		conditionBuilders1.add(conditionBuilder13);

		ruleBuilder1.setConditionBuilders(conditionBuilders1);

		List<ActionBuilder> actionBuilders1 = new ArrayList<ActionBuilder>();

		ActionBuilder actionBuilder11 = new ActionBuilder();
		actionBuilder11.setKey(EnrichmentEnum.BUSINESSRULEENRICHMENT);
		actionBuilder11.setValue("123");
		actionBuilders1.add(actionBuilder11);

		ActionBuilder actionBuilder12 = new ActionBuilder();
		actionBuilder12.setKey(EnrichmentEnum.BUSINESSRULEENRICHMENT);
		actionBuilder12.setValue("321");
		actionBuilders1.add(actionBuilder12);

		ruleBuilder1.setActionBuilders(actionBuilders1);

		ruleBuilders.add(ruleBuilder1);

		ruleConfig.setRuleBuilders(ruleBuilders);

		ObjectMapper mapper = new ObjectMapper();
		try {
			String writeValueAsString = mapper.writeValueAsString(ruleConfig);
			System.out.println(writeValueAsString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

}

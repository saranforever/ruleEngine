package com.smi.drools.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RuleConfig {

	private String ruleName;
	private String ruleDescription;
	private String modelType;

	private List<ConditionBuilder> conditionBuilders;
	private List<ActionBuilder> actionBuilders;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

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

	public List<ConditionBuilder> getConditionBuilders() {
		return conditionBuilders;
	}

	public void setConditionBuilders(List<ConditionBuilder> conditionBuilders) {
		this.conditionBuilders = conditionBuilders;
	}

	public List<ActionBuilder> getActionBuilders() {
		return actionBuilders;
	}

	public void setActionBuilders(List<ActionBuilder> actionBuilders) {
		this.actionBuilders = actionBuilders;
	}
	
	public static void main(String[] args) {
		RuleConfig ruleConfig = new RuleConfig();
		ruleConfig.setRuleName("docRule");
		ruleConfig.setModelType("DocumentContext");
		
		List<ConditionBuilder> conditionBuilders = new ArrayList<ConditionBuilder>();
		
		ConditionBuilder conditionBuilder = new ConditionBuilder();
		conditionBuilder.setKey("documentType");
		conditionBuilder.setFilter("equals");
		conditionBuilder.setValue("EDI");
		conditionBuilder.setCondition("and");
		conditionBuilders.add(conditionBuilder);
		
		ConditionBuilder conditionBuilder1 = new ConditionBuilder();
		conditionBuilder1.setKey("documentVersion");
		conditionBuilder.setFilter("equals");
		conditionBuilder1.setValue("00401");
		conditionBuilder1.setCondition("or");
		conditionBuilders.add(conditionBuilder1);
		
		ConditionBuilder conditionBuilder2 = new ConditionBuilder();
		conditionBuilder2.setKey("senderEid");
		conditionBuilder2.setFilter("notequals");
		conditionBuilder2.setValue("1123");
		conditionBuilders.add(conditionBuilder2);
		
		ruleConfig.setConditionBuilders(conditionBuilders);
		
		List<ActionBuilder> actionBuilders = new ArrayList<ActionBuilder>();
		
		ActionBuilder actionBuilder = new ActionBuilder();
		actionBuilder.setKey("val1");
		actionBuilder.setValue("123");
		actionBuilders.add(actionBuilder);
		
		ActionBuilder actionBuilder1 = new ActionBuilder();
		actionBuilder1.setKey("val2");
		actionBuilder1.setValue("321");
		actionBuilders.add(actionBuilder1);
		
		ruleConfig.setActionBuilders(actionBuilders);
		
		/*{
		 * "ruleName":"docRule",
		 * "ruleDescription":null,
		 * "modelType":"DocumentContext",
		 * "conditionBuilders":[
			 * {"key":"documentType","filter":"equals","value":"EDI", "condition":"and"},
			 * {"key":"documentVersion","filter":"equals","value":"00401", "condition":"or"},
			 * {"key":"senderEid","filter":"notequals","value":"1123"}
		 * ],
		 * "actionBuilders":[
			 * {"key":"val1","value":"123"},
			 * {"key":"val2","value":"321"}
			 * ]
		 * }*/

		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String writeValueAsString = mapper.writeValueAsString(ruleConfig);
			System.out.println(writeValueAsString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}

}

package com.smi.drools.model;

import java.util.List;

import com.smi.drools.entity.RuleBuilder;

public class RuleTestConfig {

	private String modelType;
	private List<RuleBuilder> ruleBuilders;

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public List<RuleBuilder> getRuleBuilders() {
		return ruleBuilders;
	}

	public void setRuleBuilders(List<RuleBuilder> ruleBuilders) {
		this.ruleBuilders = ruleBuilders;
	}

}

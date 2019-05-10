package com.smi.drools.model;

import java.util.List;

public class RuleBuilder {

	private String ruleName;

	private List<ConditionBuilder> conditionBuilders;
	private List<ActionBuilder> actionBuilders;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
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

}

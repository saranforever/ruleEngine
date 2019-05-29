package com.smi.drools.dto;

import java.io.Serializable;
import java.util.List;

import com.smi.drools.enumutil.ModelTypeEnum;

import lombok.Data;

@Data
public class RuleBuilderDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3224635737919764811L;

	private Long id;

	private ModelTypeEnum modelType;

	private String ruleName;

	private String ruleGroupName;

	private long priority;

	private List<ConditionBuilderDTO> conditionBuilders;

	private List<ActionBuilderDTO> actionBuilders;
}

package com.smi.drools.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RuleConfigDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 859456017285881118L;

	private Long id;
	
	private String name;

	private String ruleDescription;

	private List<RuleBuilderDTO> ruleBuilders;
	
	private RuleDTO rule;
	
	private String emailId;

	private boolean enable;

}

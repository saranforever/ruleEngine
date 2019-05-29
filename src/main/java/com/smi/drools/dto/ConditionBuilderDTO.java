package com.smi.drools.dto;

import java.io.Serializable;

import com.smi.drools.enumutil.ConditionalEnum;
import com.smi.drools.enumutil.FilterEnum;
import com.smi.drools.enumutil.ModelTypeEnum;

import lombok.Data;

@Data
public class ConditionBuilderDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2975943670431717849L;

	private Long id;

	private ModelTypeEnum metaField;

	private FilterEnum filter;

	private String metaValue;

	private ConditionalEnum conditionOperator;

}

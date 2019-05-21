package com.smi.drools.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.smi.drools.enumutil.ConditionalEnum;
import com.smi.drools.enumutil.FilterEnum;
import com.smi.drools.enumutil.ModelTypeEnum;

import lombok.Data;

@Entity
@Table(name = "CONDITION_BUILDER")
@Data
public class ConditionBuilder implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2975943670431717849L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "condition_builder_id")
	private Long id;

	@Column(name = "modelType")
	private ModelTypeEnum modelType;

	@Column(name = "metaField")
	private String metaField;

	@Column(name = "filter")
	private FilterEnum filter;

	@Column(name = "metaValue")
	private String metaValue;

	@Column(name = "conditionOperator")
	private ConditionalEnum conditionOperator;

	@ManyToOne
	@JoinColumn
	private RuleBuilder ruleBuilder;

}

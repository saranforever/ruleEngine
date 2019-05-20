package com.smi.drools.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.smi.drools.enumutil.ModelTypeEnum;

import lombok.Data;

@Entity
@Table(name = "RULE_BUILDER")
@Data
public class RuleBuilder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3224635737919764811L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "modelType")
	private ModelTypeEnum modelType;

	@Column(name = "ruleName", nullable = false)
	private String ruleName;
	
	@Column(name = "ruleGroupName", nullable = true, unique = true)
	private String ruleGroupName;

	@Column(name = "priority")
	private long priority;

	@ManyToOne
	@JoinColumn
	private RuleConfig ruleConfig;

	@OneToMany(mappedBy = "ruleBuilder", cascade = CascadeType.ALL)
	private List<ConditionBuilder> conditionBuilders;

	@OneToMany(mappedBy = "ruleBuilder", cascade = CascadeType.ALL)
	private List<ActionBuilder> actionBuilders;
}

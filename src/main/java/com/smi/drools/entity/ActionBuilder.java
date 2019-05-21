package com.smi.drools.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.smi.drools.enumutil.EnrichmentEnum;

import lombok.Data;

@Entity
@Table(name = "ACTION_BUILDER")
@Data
public class ActionBuilder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5156397965529942825L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "action_builder_id")
	private Long id;

	@Column(name = "enrichement")
	private EnrichmentEnum enrichement;

	@Column(name = "enrichmentAction")
	private String enrichmentAction;

	@ManyToOne
	@JoinColumn
	private RuleBuilder ruleBuilder;

}

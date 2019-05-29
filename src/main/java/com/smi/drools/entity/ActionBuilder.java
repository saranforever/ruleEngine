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

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@JsonIgnore
	@ManyToOne
	@JoinColumn
	private RuleBuilder ruleBuilder;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionBuilder other = (ActionBuilder) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ruleBuilder == null) {
			if (other.ruleBuilder != null)
				return false;
		} else if (!ruleBuilder.equals(other.ruleBuilder))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ruleBuilder == null) ? 0 : ruleBuilder.hashCode());
		return result;
	}
	
}

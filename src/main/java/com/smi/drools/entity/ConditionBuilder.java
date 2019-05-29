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

	@Column(name = "metaField")
	private ModelTypeEnum metaField;

	@Column(name = "filter")
	private FilterEnum filter;

	@Column(name = "metaValue")
	private String metaValue;

	@Column(name = "conditionOperator")
	private ConditionalEnum conditionOperator;

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
		ConditionBuilder other = (ConditionBuilder) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (metaValue == null) {
			if (other.metaValue != null)
				return false;
		} else if (!metaValue.equals(other.metaValue))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((metaValue == null) ? 0 : metaValue.hashCode());
		return result;
	}

}

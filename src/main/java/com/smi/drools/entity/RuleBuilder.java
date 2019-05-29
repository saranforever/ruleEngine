package com.smi.drools.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

	@Column(name = "ruleGroupName", nullable = true)
	private String ruleGroupName;

	@Column(name = "priority")
	private long priority;

	@JsonIgnore
	@ManyToOne
	@JoinColumn
	private RuleConfig ruleConfig;

	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(mappedBy = "ruleBuilder", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ConditionBuilder> conditionBuilders;

	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(mappedBy = "ruleBuilder", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ActionBuilder> actionBuilders;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleBuilder other = (RuleBuilder) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
}

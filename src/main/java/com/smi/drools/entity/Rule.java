package com.smi.drools.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "RULE")
@Data
public class Rule implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String ruleKey;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	private String version;

	@Column(nullable = true)
	private String lastModifyTime;

	@Column(nullable = false)
	private String createTime;

	@OneToOne(mappedBy = "rule")
	private RuleConfig ruleConfig;

	@Column(name = "enable")
	private boolean enable;
}
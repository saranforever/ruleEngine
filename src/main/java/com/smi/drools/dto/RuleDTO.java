package com.smi.drools.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RuleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String ruleKey;

	private String content;

	private String version;

	private String lastModifyTime;

	private String createTime;

	private boolean enable;
}
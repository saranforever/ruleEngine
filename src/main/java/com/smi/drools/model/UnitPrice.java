package com.smi.drools.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class UnitPrice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4041889825779957508L;
	private Validation validation;
	private String value;
}

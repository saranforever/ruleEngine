package com.smi.drools.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Qty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1199056966043832714L;
	private Validation validation;
	private String value;
}
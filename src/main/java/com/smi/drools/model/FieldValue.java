package com.smi.drools.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class FieldValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1664664121526870498L;
	
	private Validation validation;
	private String value;
}

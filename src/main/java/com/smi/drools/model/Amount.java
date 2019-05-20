package com.smi.drools.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Amount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4087103804459913516L;
	
	private Validation validation;
	private String value;
}

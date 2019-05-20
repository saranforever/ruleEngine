package com.smi.drools.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class FieldData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4036749319601367011L;
	
	private String fieldName;
	private List<FieldValue> fieldValues;
}
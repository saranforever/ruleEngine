package com.smi.drools.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Description implements Serializable {

	private Validation validation;
	private String value;
}
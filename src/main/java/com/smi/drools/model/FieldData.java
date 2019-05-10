package com.smi.drools.model;

import java.util.List;

import lombok.Data;

@Data
public class FieldData{
	private String fieldName;
	private List<FieldValue> fieldValues;
}
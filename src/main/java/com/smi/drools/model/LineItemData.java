package com.smi.drools.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LineItemData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1335079594093680644L;
	
	@JsonProperty(value = "AMOUNT")
	private FieldValue amount;
	
	@JsonProperty(value = "UNIT PRICE")
	private FieldValue unitPrice;
	
	@JsonProperty(value = "DESCRIPTION")
	private FieldValue description;
	
	@JsonProperty(value = "QTY")
	private FieldValue qty;
}

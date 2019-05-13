package com.smi.drools.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LineItemData {
	@JsonProperty(value = "AMOUNT")
	private Amount amount;
	@JsonProperty(value = "UNIT PRICE")
	private UnitPrice unitPrice;
	@JsonProperty(value = "DESCRIPTION")
	private Description description;
	@JsonProperty(value = "QTY")
	private Qty qty;
}

package com.smi.drools.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CustomerDocumentContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 643992256194573793L;
	
	private AutoLineItemData autoLineItemData;
	private List<FieldData> fieldDatas;
	private List<LineItemData> lineItemDatas;
}
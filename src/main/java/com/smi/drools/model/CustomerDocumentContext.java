package com.smi.drools.model;

import java.util.List;

import lombok.Data;

@Data
public class CustomerDocumentContext {
	
	private AutoLineItemData autoLineItemData;
	private List<FieldData> fieldDatas;
	private List<LineItemData> lineItemDatas;
}

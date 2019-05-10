package com.smi.drools.model;

import java.util.List;

public class CustomerDocument {
	private List<AutoLineItemData> autoLineItemDatas;
	private List<FieldData> fieldDatas;
	private List<LineItemData> lineItemDatas;


	public List<AutoLineItemData> getAutoLineItemDatas() {
		return autoLineItemDatas;
	}
	public void setAutoLineItemDatas(List<AutoLineItemData> autoLineItemDatas) {
		this.autoLineItemDatas = autoLineItemDatas;
	}
	public List<FieldData> getFieldDatas() {
		return fieldDatas;
	}
	public void setFieldDatas(List<FieldData> fieldDatas) {
		this.fieldDatas = fieldDatas;
	}
	public List<LineItemData> getLineItemDatas() {
		return lineItemDatas;
	}
	public void setLineItemDatas(List<LineItemData> lineItemDatas) {
		this.lineItemDatas = lineItemDatas;
	}
}

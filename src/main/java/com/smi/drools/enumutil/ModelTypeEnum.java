package com.smi.drools.enumutil;

public enum ModelTypeEnum {

	CUSTOMERDOCUMENT(null, "com.smi.drools.model.CustomerDocument", "CustomerDocument"),
	AUTOLINEITEMDATA(CUSTOMERDOCUMENT, "com.smi.drools.model.AutoLineItemData", "AutoLineItemData"),
	FIELDDATA(CUSTOMERDOCUMENT, "com.smi.drools.model.FieldData", "FieldData"),
	FIELDVALUE(CUSTOMERDOCUMENT, "com.smi.drools.model.FieldValue", "FieldValue"),
	AMOUNT(CUSTOMERDOCUMENT, "com.smi.drools.model.Amount", "Amount"),
	UNITPRICE(CUSTOMERDOCUMENT, "com.smi.drools.model.UnitPrice", "UnitPrice"),
	DESCRIPTION(CUSTOMERDOCUMENT, "com.smi.drools.model.Description", "Description"),
	QTY(CUSTOMERDOCUMENT, "com.smi.drools.model.Qty", "Qty");

	String packageName;
	private String className;
	private ModelTypeEnum parent;

	ModelTypeEnum(ModelTypeEnum parent, String packageName, String className) {
		this.parent = parent;
		this.packageName = packageName;
		this.className = className;
	}

	public String packageName() {
		return packageName;
	}

	public String className() {
		return className;
	}

	public ModelTypeEnum parent() {
		return parent;
	}

}

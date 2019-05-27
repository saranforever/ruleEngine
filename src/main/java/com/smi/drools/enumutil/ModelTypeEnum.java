package com.smi.drools.enumutil;

import java.util.stream.Stream;

public enum ModelTypeEnum {

	CUSTOMERDOCUMENT(null, "com.smi.drools.model.CustomerDocument", "CustomerDocument"),
	/*AUTOLINEITEMDATA(CUSTOMERDOCUMENT, "com.smi.drools.model.AutoLineItemData", "AutoLineItemData"),
	FIELDDATA(CUSTOMERDOCUMENT, "com.smi.drools.model.FieldData", "FieldData"),
	FIELDVALUE(CUSTOMERDOCUMENT, "com.smi.drools.model.FieldValue", "FieldValue"),*/
	SENDEREID(CUSTOMERDOCUMENT, "com.smi.drools.model.CustomerDocument", "senderEid"),
	RECEIVEREID(CUSTOMERDOCUMENT, "com.smi.drools.model.CustomerDocument", "receiverEid"),
	FIELDNAME(CUSTOMERDOCUMENT, "com.smi.drools.model.FieldData", "fieldName"),
	AMOUNT(CUSTOMERDOCUMENT, "com.smi.drools.model.FieldValue", "Amount"),
	UNITPRICE(CUSTOMERDOCUMENT, "com.smi.drools.model.FieldValue", "UnitPrice"),
	DESCRIPTION(CUSTOMERDOCUMENT, "com.smi.drools.model.FieldValue", "Description"),
	QTY(CUSTOMERDOCUMENT, "com.smi.drools.model.FieldValue", "Qty");

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
	
	public static Stream<ModelTypeEnum> stream() {
        return Stream.of(ModelTypeEnum.values()); 
    }

}

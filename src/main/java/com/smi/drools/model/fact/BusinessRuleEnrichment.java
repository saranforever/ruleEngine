package com.smi.drools.model.fact;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.smi.drools.model.CustomerDocument;
import com.smi.drools.model.CustomerDocumentContext;
import com.smi.drools.model.FieldData;
import com.smi.drools.model.FieldValue;

public class BusinessRuleEnrichment {

	public CustomerDocument enrichInvoiceNoWithTimeStatmp(CustomerDocument customerDocument) {

		Map<Integer, CustomerDocumentContext> cMap = customerDocument.getCustomerDocumentsContextMap();
		Set<Integer> keySet = cMap.keySet();

		for (Integer key : keySet) {
			CustomerDocumentContext customerDocumentContext = cMap.get(key);
			List<FieldData> fieldDatas = customerDocumentContext.getFieldDatas();
			for (FieldData fieldData : fieldDatas) {
				if (fieldData.getFieldName().equalsIgnoreCase("invoice")) {
					List<FieldValue> fieldValues = fieldData.getFieldValues();
					for (FieldValue fieldValue : fieldValues) {
						if (StringUtils.isEmpty(fieldValue.getValue())) {
							fieldValue.setValue(System.currentTimeMillis() + "");
						}
					}
				}
			}
		}
		return customerDocument;
	}

	public CustomerDocument enrichInvoiceNoWithDateFormat(CustomerDocument customerDocument) {

		Map<Integer, CustomerDocumentContext> cMap = customerDocument.getCustomerDocumentsContextMap();
		Set<Integer> keySet = cMap.keySet();

		for (Integer key : keySet) {
			CustomerDocumentContext customerDocumentContext = cMap.get(key);
			List<FieldData> fieldDatas = customerDocumentContext.getFieldDatas();
			for (FieldData fieldData : fieldDatas) {
				if (fieldData.getFieldName().equalsIgnoreCase("invoice")) {
					List<FieldValue> fieldValues = fieldData.getFieldValues();
					for (FieldValue fieldValue : fieldValues) {
						if (StringUtils.isEmpty(fieldValue.getValue())) {
							fieldValue.setValue("14052019");
						}
					}
				}
			}
		}
		return customerDocument;
	}
}

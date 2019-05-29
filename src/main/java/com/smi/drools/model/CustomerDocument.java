package com.smi.drools.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class CustomerDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7692579087270502453L;

	private String senderEid;
	private String receiverEid;
	private Map<Integer, CustomerDocumentContext> customerDocumentsContextMap;

	public boolean containsFractions(CustomerDocument customerDocument) {
		boolean status = false;

		Map<Integer, CustomerDocumentContext> cMap = customerDocument.getCustomerDocumentsContextMap();
		Set<Integer> keySet = cMap.keySet();

		for (Integer key : keySet) {
			CustomerDocumentContext customerDocumentContext = cMap.get(key);
			List<LineItemData> lineItemDatas = customerDocumentContext.getLineItemDatas();
			for (LineItemData lineItemData : lineItemDatas) {
				FieldValue amount = lineItemData.getAmount();
				String value = amount.getValue();
				value = value.replace(",", "");
				double round = Double.parseDouble(value);
				if (round % 1 != 0) {
					status =  true;
					break;
				}
			}
		}
		return status;
	}
}

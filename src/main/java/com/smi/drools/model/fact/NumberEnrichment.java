package com.smi.drools.model.fact;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smi.drools.model.Amount;
import com.smi.drools.model.CustomerDocument;
import com.smi.drools.model.CustomerDocumentContext;
import com.smi.drools.model.LineItemData;

public class NumberEnrichment {

	public CustomerDocument roundOffAmount(CustomerDocument customerDocument) {

		Map<Integer, CustomerDocumentContext> cMap = customerDocument.getCustomerDocumentsContextMap();
		Set<Integer> keySet = cMap.keySet();

		for (Integer key : keySet) {
			CustomerDocumentContext customerDocumentContext = cMap.get(key);
			List<LineItemData> lineItemDatas = customerDocumentContext.getLineItemDatas();
			for (LineItemData lineItemData : lineItemDatas) {
				Amount amount = lineItemData.getAmount();
				String value = amount.getValue();
				value = value.replace(",", "");
				double round = Math.ceil(Double.parseDouble(value));
				amount.setValue(round + "");
			}
		}
		return customerDocument;
	}
}

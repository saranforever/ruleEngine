package com.smi.drools.model;

import java.util.Map;

import lombok.Data;

@Data
public class CustomerDocument {

	private String senderEid;
	private String receiverEid;
	private Map<Integer, CustomerDocumentContext> customerDocumentsContextMap;
}

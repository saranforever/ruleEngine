package com.smi.drools.model;

import java.io.Serializable;
import java.util.Map;

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
}

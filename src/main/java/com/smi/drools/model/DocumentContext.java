package com.smi.drools.model;

public class DocumentContext {
	
	private String documentType;
	
	private String documentVersion;
	
	private String senderEid;

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentVersion() {
		return documentVersion;
	}

	public void setDocumentVersion(String documentVersion) {
		this.documentVersion = documentVersion;
	}

	public String getSenderEid() {
		return senderEid;
	}

	public void setSenderEid(String senderEid) {
		this.senderEid = senderEid;
	}
	
}

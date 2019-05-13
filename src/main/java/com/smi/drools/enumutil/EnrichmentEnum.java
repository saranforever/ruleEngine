package com.smi.drools.enumutil;

public enum EnrichmentEnum {
	BUSINESSRULEENRICHMENT("com.smi.drools.model.fact.BusinessRuleEnrichment", "BusinessRuleEnrichment");

	String packageName;
	private String className;

	EnrichmentEnum(String packageName, String className) {
		this.packageName = packageName;
		this.className = className;
	}

	public String packageName() {
		return packageName;
	}

	public String className() {
		return className;
	}

}
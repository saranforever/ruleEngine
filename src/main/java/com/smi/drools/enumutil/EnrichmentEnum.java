package com.smi.drools.enumutil;

public enum EnrichmentEnum {
	BUSINESSRULEENRICHMENT("com.smi.drools.model.fact.BusinessRuleEnrichment", "BusinessRuleEnrichment", "customerdocument"),
	NUMBERENRICHMENT("com.smi.drools.model.fact.NumberEnrichment", "NumberEnrichment", "customerdocument"),
	RULE;

	String packageName;
	private String className;
	private String parameterName;

	private EnrichmentEnum() {
	}
	
	EnrichmentEnum(String packageName, String className, String parameterName) {
		this.packageName = packageName;
		this.className = className;
		this.parameterName = parameterName;
	}

	public String packageName() {
		return packageName;
	}

	public String className() {
		return className;
	}
	
	public String parameterName() {
		return parameterName;
	}

}
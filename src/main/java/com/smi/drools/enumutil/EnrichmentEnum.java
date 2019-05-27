package com.smi.drools.enumutil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
	public static Stream<EnrichmentEnum> stream() {
        return Stream.of(EnrichmentEnum.values()); 
    }
	
	public static List<String> getAllMethods(EnrichmentEnum enrichmentEnum) throws ClassNotFoundException {
		Class<?> clazz = Class.forName(enrichmentEnum.packageName());
		return Arrays.asList(clazz.getMethods()).stream().map(s -> String.valueOf(s.getName())).
				filter(name -> !"wait".equals(name))
				.filter(name -> !"toString".equals(name))
				.filter(name -> !"hashCode".equals(name))
				.filter(name -> !"getClass".equals(name))
				.filter(name -> !"notify".equals(name))
				.filter(name -> !"notifyAll".equals(name))
				.filter(name -> !"equals".equals(name))
				.collect(Collectors.toList());
    }

}
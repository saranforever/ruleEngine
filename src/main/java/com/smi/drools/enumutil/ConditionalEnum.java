package com.smi.drools.enumutil;

public enum ConditionalEnum {

	AND("and", " && "), OR("or", " || ");

	String displayName;
	private String symbol;

	ConditionalEnum(String displayName, String symbol) {
		this.displayName = displayName;
		this.symbol = symbol;
	}

	public String packageName() {
		return displayName;
	}

	public String symbol() {
		return symbol;
	}

}

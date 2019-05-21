package com.smi.drools.enumutil;

public enum FilterEnum {

	EQUALS("equals", "=="), NOTEQUALS("not equals", "!="), ROUND_OFF("Round off");

	String displayName;
	private String symbol;

	FilterEnum(String displayName) {
		this.displayName = displayName;
	}

	FilterEnum(String displayName, String symbol) {
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

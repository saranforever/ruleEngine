package com.smi.drools.model;

import com.smi.drools.enumutil.EnrichmentEnum;

public class ActionBuilder {

	private EnrichmentEnum key;

	private String value;

	public EnrichmentEnum getKey() {
		return key;
	}

	public void setKey(EnrichmentEnum key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

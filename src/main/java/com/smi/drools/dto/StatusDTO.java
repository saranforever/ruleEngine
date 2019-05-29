package com.smi.drools.dto;

import lombok.Data;

@Data
public class StatusDTO {

	private int code;
	private String status;
	private String message;

	public StatusDTO(String status) {
		this.status = status;
	}

	public StatusDTO(int code, String status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}
}
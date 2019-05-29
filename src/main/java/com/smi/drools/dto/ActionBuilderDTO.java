package com.smi.drools.dto;

import java.io.Serializable;

import com.smi.drools.enumutil.EnrichmentEnum;

import lombok.Data;

@Data
public class ActionBuilderDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5156397965529942825L;

	private Long id;

	private EnrichmentEnum enrichement;

	private String enrichmentAction;

}

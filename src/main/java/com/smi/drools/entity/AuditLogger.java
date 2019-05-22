package com.smi.drools.entity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;

import lombok.Data;

@Entity
@Table(name="AUDIT_LOGGER")
@Data
public class AuditLogger implements Serializable {    

	/**
	 * 
	 */
	private static final long serialVersionUID = 4931967171972998093L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_logger_id")
	private Long id;

	@ElementCollection
	@JoinTable(name = "audit_logger_rule", joinColumns = @JoinColumn(name = "audit_id"))
	@Column(name = "rulename")
	private List<String> ruleName;

	@Lob
	@Column(name = "content")
	private String content;

	@Column(name = "createdTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
}
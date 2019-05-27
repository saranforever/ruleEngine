package com.smi.drools.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;

import com.smi.drools.entity.AuditLogger;
import com.smi.drools.entity.RuleConfig;
import com.smi.drools.service.BeanUtil;
import com.smi.drools.util.RuleConfigUtil;

public class RuleConfigEntityListener {

	@PostPersist
	public void postPersist(RuleConfig ruleConfig) {
		String ruleStr = RuleConfigUtil.buildRule(ruleConfig);
		AuditLogger auditLogger = new AuditLogger();
		List<String> ruleNames = ruleConfig.getRuleBuilders().stream().map(s -> s.getRuleName())
				.collect(Collectors.toCollection(ArrayList::new));
		auditLogger.setRuleName(ruleNames);
		auditLogger.setTimeStamp(new Date());
		auditLogger.setContent(ruleStr);

		EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
		entityManager.persist(auditLogger);

		System.out.println("postPersist triggered");
	}
}
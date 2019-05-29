package com.smi.drools.service;

import java.util.List;
import java.util.Optional;

import com.smi.drools.entity.RuleConfig;

public interface IRuleConfigService {
	
	RuleConfig save(RuleConfig ruleConfig) throws Exception;

	Optional<RuleConfig> findRuleConfigById(long id);

	List<RuleConfig> findAll();

	List<RuleConfig> findByEmailId(String emailId);

}

package com.smi.drools.service;

import com.smi.drools.entity.RuleConfig;

public interface IRuleConfigService {
	
	void save(RuleConfig ruleConfig);

	RuleConfig findRuleConfigById(long id);

}

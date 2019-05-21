package com.smi.drools.service;

import com.smi.drools.entity.Rule;
import com.smi.drools.entity.RuleConfig;

public interface IRuleConfigService {
	
	void save(Rule rule, RuleConfig ruleConfig);

}

package com.smi.drools.service;

import com.smi.drools.entity.Rule;
import com.smi.drools.entity.RuleConfig;

public interface IRuleService {
	
	void save(Rule rule, RuleConfig ruleConfig);

}

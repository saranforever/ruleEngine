package com.smi.drools.service;

import com.smi.drools.model.Rule;
import com.smi.drools.model.RuleConfig;

public interface IRuleService {
	
	void save(Rule rule, RuleConfig ruleConfig);

}

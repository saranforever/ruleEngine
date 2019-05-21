package com.smi.drools.service;

import com.smi.drools.model.Rule;
import com.smi.drools.model.RuleConfig;

public interface IRuleConfigService {
	
	void save(Rule rule, RuleConfig ruleConfig);

}

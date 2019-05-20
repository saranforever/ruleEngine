package com.smi.drools.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smi.drools.dao.ActionBuilderRepository;
import com.smi.drools.dao.ConditionBuilderRepository;
import com.smi.drools.dao.RuleBuilderRepository;
import com.smi.drools.dao.RuleConfigRepository;
import com.smi.drools.dao.RuleRepository;
import com.smi.drools.model.ActionBuilder;
import com.smi.drools.model.ConditionBuilder;
import com.smi.drools.model.Rule;
import com.smi.drools.model.RuleBuilder;
import com.smi.drools.model.RuleConfig;
import com.smi.drools.service.IRuleService;

@Service
public class RuleServiceImpl implements IRuleService {
	
	@Autowired
	private RuleRepository ruleRepository;
	
	@Autowired
	private RuleConfigRepository ruleConfigRepository;
	
	@Autowired
	private RuleBuilderRepository ruleBuilderRepository;
	
	@Autowired
	private ConditionBuilderRepository conditionBuilderRepository; 
	
	@Autowired
	private ActionBuilderRepository actionBuilderRepository;

	@Override
	public void save(Rule rule, RuleConfig ruleConfig) {
		
		List<RuleBuilder> ruleBuilders = ruleConfig.getRuleBuilders();
		if (!CollectionUtils.isEmpty(ruleBuilders)) {
			// Rule Builder
			for (RuleBuilder ruleBuilder : ruleBuilders) {
				ruleBuilder.setRuleConfig(ruleConfig);
				// Condition Builder
				List<ConditionBuilder> conditionBuilders = ruleBuilder.getConditionBuilders();
				if (!CollectionUtils.isEmpty(conditionBuilders)) {
					for (ConditionBuilder conditionBuilder : conditionBuilders) {
						conditionBuilder.setRuleBuilder(ruleBuilder);
					}
				}
				
				// Action Builder
				List<ActionBuilder> actionBuilders = ruleBuilder.getActionBuilders();
				if (!CollectionUtils.isEmpty(actionBuilders)) {
					for (ActionBuilder actionBuilder : actionBuilders) {
						actionBuilder.setRuleBuilder(ruleBuilder);
					}
				}
			}
		}
		ruleConfigRepository.save(ruleConfig);
		rule.setRuleKey(ruleConfig.getName());
		ruleRepository.save(rule);
	}

}

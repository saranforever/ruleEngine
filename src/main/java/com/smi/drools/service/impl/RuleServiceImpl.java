package com.smi.drools.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smi.drools.dao.RuleConfigRepository;
import com.smi.drools.entity.ActionBuilder;
import com.smi.drools.entity.ConditionBuilder;
import com.smi.drools.entity.Rule;
import com.smi.drools.entity.RuleBuilder;
import com.smi.drools.entity.RuleConfig;
import com.smi.drools.service.IRuleService;

@Service
public class RuleServiceImpl implements IRuleService {
	
	
	@Autowired
	private RuleConfigRepository ruleConfigRepository;
	
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
		rule.setEnable(true);
		rule.setRuleKey(ruleConfig.getName());
		ruleConfig.setRule(rule);
		ruleConfigRepository.save(ruleConfig);
		/*ruleRepository.save(rule);*/
	}

}

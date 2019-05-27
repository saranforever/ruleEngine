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
import com.smi.drools.service.IRuleConfigService;
import com.smi.drools.util.DroolsRulesService;
import com.smi.drools.util.RuleConfigUtil;

@Service
public class RuleConfigServiceImpl implements IRuleConfigService {
	
	@Autowired
	private RuleConfigRepository ruleConfigRepository;
	
	@Autowired
	private DroolsRulesService reloadDroolsRulesService;

	@Override
	public void save(RuleConfig ruleConfig) {
		
		String ruleStr = RuleConfigUtil.buildRule(ruleConfig);

		Rule rule = new Rule();
		rule.setContent(ruleStr);
		rule.setCreateTime("");
		rule.setVersion("7");
		
		List<RuleBuilder> ruleBuilders = ruleConfig.getRuleBuilders();
		if (!CollectionUtils.isEmpty(ruleBuilders)) {
			// Rule Builder
			for (RuleBuilder ruleBuilder : ruleBuilders) {
				ruleBuilder.setRuleConfig(ruleConfig);
				// Condition Builder
				buildConditions(ruleBuilder, ruleBuilder.getConditionBuilders());
				
				// Action Builder
				buildActions(ruleBuilder, ruleBuilder.getActionBuilders());
			}
		}
		rule.setEnable(true);
		rule.setRuleKey(ruleConfig.getName());
		ruleConfig.setRule(rule);
		ruleConfigRepository.save(ruleConfig);
		
		this.reloadDroolsRulesService.addRule(rule);
	}

	private void buildActions(RuleBuilder ruleBuilder, List<ActionBuilder> actionBuilders) {
		if (!CollectionUtils.isEmpty(actionBuilders)) {
			for (ActionBuilder actionBuilder : actionBuilders) {
				actionBuilder.setRuleBuilder(ruleBuilder);
			}
		}
	}

	private void buildConditions(RuleBuilder ruleBuilder, List<ConditionBuilder> conditionBuilders) {
		if (!CollectionUtils.isEmpty(conditionBuilders)) {
			for (ConditionBuilder conditionBuilder : conditionBuilders) {
				conditionBuilder.setRuleBuilder(ruleBuilder);
			}
		}
	}

}

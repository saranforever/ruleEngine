package com.smi.drools.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.drools.core.spi.RuleComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smi.drools.dao.RuleBuilderRepository;
import com.smi.drools.dao.RuleConfigRepository;
import com.smi.drools.dto.StatusDTO;
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
	
	@Autowired 
	private RuleBuilderRepository ruleBuilderRepository;

	/*
	 * @Autowired private RuleRepository ruleRepository;
	 * 
	 * @Autowired private RuleBuilderRepository ruleBuilderRepository;
	 * 
	 * @Autowired private ConditionBuilderRepository conditionBuilderRepository;
	 * 
	 * @Autowired private ActionBuilderRepository actionBuilderRepository;
	 */

	@Override
	public RuleConfig save(RuleConfig ruleConfig) throws Exception {

		String ruleStr = RuleConfigUtil.buildRule(ruleConfig);
		String oldRule = "";
		Rule rule = null;
		if (ruleConfig.getId() != null) {
			rule = ruleConfig.getRule();
			oldRule = rule.getContent();
		} else {
			rule = new Rule();
			rule.setCreateTime("");
			rule.setVersion("7");
		}
		rule.setContent(ruleStr);
		List<RuleBuilder> ruleBuilders = ruleConfig.getRuleBuilders();
		if (!CollectionUtils.isEmpty(ruleBuilders)) {
			// Rule Builder
			for (RuleBuilder ruleBuilder : ruleBuilders) {
				if(ruleBuilderRepository.findByRuleGroupName(ruleBuilder.getRuleGroupName()) > 0){
					throw new Exception("Duplicate Group Name");
				}
				ruleBuilder.setRuleConfig(ruleConfig);
				// Condition Builder
				buildConditions(ruleBuilder, ruleBuilder.getConditionBuilders());

				// Action Builder
				buildActions(ruleBuilder, ruleBuilder.getActionBuilders());
			}
			ruleConfig.setRuleBuilders(ruleBuilders);
		}
		rule.setEnable(true);
		rule.setRuleKey(ruleConfig.getName());
		rule.setRuleConfig(ruleConfig);
		ruleConfig.setRule(rule);

		if (!StringUtils.isEmpty(oldRule)) {
			this.reloadDroolsRulesService.removeRule(ruleConfig.getId());
		}
		ruleConfigRepository.save(ruleConfig);
		this.reloadDroolsRulesService.addRule(rule);
		return ruleConfig;
	}

	private List<ActionBuilder> buildActions(RuleBuilder ruleBuilder, List<ActionBuilder> actionBuilders) {
		List<ActionBuilder> actionsToRemove = null;
		if (!CollectionUtils.isEmpty(actionBuilders)) {
			for (ActionBuilder actionBuilder : actionBuilders) {
				actionBuilder.setRuleBuilder(ruleBuilder);
			}
		}
		return actionsToRemove;
	}

	private List<ConditionBuilder> buildConditions(RuleBuilder ruleBuilder, List<ConditionBuilder> conditionBuilders) {
		List<ConditionBuilder> conditionsToRemove = null;
		if (!CollectionUtils.isEmpty(conditionBuilders)) {
			for (ConditionBuilder conditionBuilder : conditionBuilders) {
				conditionBuilder.setRuleBuilder(ruleBuilder);
			}
		}
		return conditionsToRemove;
	}

	@Override
	public Optional<RuleConfig> findRuleConfigById(long id) {
		return ruleConfigRepository.findById(id);
	}

	@Override
	public List<RuleConfig> findAll() {
		return ruleConfigRepository.findAll();
	}

	@Override
	public List<RuleConfig> findByEmailId(String emailId) {
		return ruleConfigRepository.findByEmailId(emailId);
	}

	@Override
	public StatusDTO deleteRuleConfigById(long id) {
		ruleConfigRepository.deleteById(id);
		return new StatusDTO("ok");
		
	}

}

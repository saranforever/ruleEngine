package com.smi.drools.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
				String oldGroupName = "";
				String oldRuleName = "";
				oldRuleName = ruleBuilderRepository.getRuleNameById(ruleBuilder.getId());
				if (ruleBuilder.getId() != null) {
					oldGroupName = ruleBuilderRepository.getRuleGroupNameById(ruleBuilder.getId());
				}

				if ((ruleBuilder.getId() != null && !oldRuleName.equals(ruleBuilder.getRuleName())
						&& checkRuleNameAlreadyExists(ruleBuilder))
						|| (ruleBuilder.getId() == null && checkRuleNameAlreadyExists(ruleBuilder))) {
					throw new Exception("Duplicate Rule Name");
				}

				if ((ruleBuilder.getId() != null && !oldGroupName.equals(ruleBuilder.getRuleGroupName())
						&& checkGroupNameAlreadyExists(ruleBuilder))
						|| (ruleBuilder.getId() == null && checkGroupNameAlreadyExists(ruleBuilder))) {
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

		// check whether the NAME unique field of the RULECONFIG Entity is duplicate or
		// not
		if (checkRuleConfigNameAlreadyExists(ruleConfig)) {
			throw new Exception("Duplicate RuleConfig name");
		}

		if (!StringUtils.isEmpty(oldRule)) {
			this.reloadDroolsRulesService.removeRule(ruleConfig.getId());
		}
		ruleConfigRepository.save(ruleConfig);
		this.reloadDroolsRulesService.addRule(rule);
		return ruleConfig;
	}

	private boolean checkRuleConfigNameAlreadyExists(RuleConfig ruleConfig) {
		return ruleConfigRepository.getCountByName(ruleConfig.getName()) > 0;
	}

	private boolean checkRuleNameAlreadyExists(RuleBuilder ruleBuilder) {
		return ruleBuilderRepository.getCountByRuleName(ruleBuilder.getRuleName()) > 0;
	}

	private boolean checkGroupNameAlreadyExists(RuleBuilder ruleBuilder) {
		return ruleBuilderRepository.getCountByRuleGroupName(ruleBuilder.getRuleGroupName()) > 0;
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
		this.reloadDroolsRulesService.removeRule(id);
		return new StatusDTO("ok");

	}

}

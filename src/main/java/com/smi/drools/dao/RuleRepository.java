package com.smi.drools.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smi.drools.entity.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long> {
	Rule findByRuleKey(String ruleKey);
}
package com.smi.drools.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smi.drools.model.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long> {
	Rule findByRuleKey(String ruleKey);
}
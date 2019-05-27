package com.smi.drools.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smi.drools.entity.RuleBuilder;

public interface RuleBuilderRepository extends JpaRepository<RuleBuilder, Long> {

	@Query("SELECT rb.ruleGroupName FROM RuleBuilder rb WHERE rb.ruleGroupName != ''")
	public List<String> findCommonRules();

}

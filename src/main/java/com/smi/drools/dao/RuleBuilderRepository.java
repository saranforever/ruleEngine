package com.smi.drools.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smi.drools.entity.RuleBuilder;
import com.smi.drools.entity.RuleConfig;

public interface RuleBuilderRepository extends JpaRepository<RuleBuilder, Long> {

	@Query("SELECT rb.ruleGroupName FROM RuleBuilder rb WHERE rb.ruleGroupName != ''")
	public List<String> findCommonRules();

	public List<RuleBuilder> findByRuleConfig(RuleConfig ruleConfig);
	
	@Query("SELECT count(*) from RuleBuilder rb where rb.ruleGroupName = :ruleGroupName and rb.ruleGroupName != ''")
	public Long getCountByRuleGroupName(@Param("ruleGroupName") String ruleGroupName);

	@Query("SELECT rb.ruleGroupName from RuleBuilder rb where rb.id = :id")
	public String getRuleGroupNameById(@Param("id") Long id);

	@Query("SELECT rb.ruleName from RuleBuilder rb where rb.id = :id")
	public String getRuleNameById(@Param("id") Long id);

	@Query("SELECT count(*) from RuleBuilder rb where rb.ruleName = :ruleName")
	public Long getCountByRuleName(@Param("ruleName") String ruleName);
	

}

package com.smi.drools.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smi.drools.entity.ConditionBuilder;
import com.smi.drools.entity.RuleBuilder;

public interface ConditionBuilderRepository extends JpaRepository<ConditionBuilder, Long> {

	List<ConditionBuilder> findByRuleBuilder(RuleBuilder ruleBuilder);

	@Query("SELECT cb FROM ConditionBuilder cb WHERE cb.ruleBuilder = :ruleBuilder and cb not in (:conditionBuilders)")
	List<ConditionBuilder> findConditionBuildersBy(@Param("ruleBuilder") RuleBuilder ruleBuilder,
			@Param("conditionBuilders") List<ConditionBuilder> conditionBuilders);

}

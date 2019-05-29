package com.smi.drools.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smi.drools.entity.ActionBuilder;
import com.smi.drools.entity.RuleBuilder;

public interface ActionBuilderRepository extends JpaRepository<ActionBuilder, Long> {

	List<ActionBuilder> findByRuleBuilder(RuleBuilder ruleBuilder);

	@Query("SELECT ab FROM ActionBuilder ab WHERE ab.ruleBuilder = :ruleBuilder and ab not in (:actionBuilders)")
	List<ActionBuilder> findActionBuildersBy(@Param("ruleBuilder") RuleBuilder ruleBuilder,
			@Param("actionBuilders") List<ActionBuilder> actionBuilders);

}

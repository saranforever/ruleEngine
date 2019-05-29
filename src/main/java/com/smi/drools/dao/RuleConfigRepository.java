package com.smi.drools.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smi.drools.entity.RuleConfig;

public interface RuleConfigRepository extends JpaRepository<RuleConfig, Long> {

	List<RuleConfig> findByEmailId(String emailId);

}

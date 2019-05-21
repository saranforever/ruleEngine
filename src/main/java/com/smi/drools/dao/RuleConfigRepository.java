package com.smi.drools.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smi.drools.entity.RuleConfig;

public interface RuleConfigRepository extends JpaRepository<RuleConfig, Long> {

}

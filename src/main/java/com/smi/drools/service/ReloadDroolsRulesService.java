package com.smi.drools.service;

import com.smi.drools.model.Rule;
import com.smi.drools.repository.RuleRepository;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReloadDroolsRulesService {

	public static KieContainer kieContainer;

	@Autowired
	private RuleRepository ruleRepository;

	public void reload() {
		KieContainer kieContainer = loadContainerFromString(loadRules());
		this.kieContainer = kieContainer;
	}
	
	public void reload(Rule rule) {
		List<Rule> rules = new ArrayList<Rule>();
		rules.add(rule);
		KieContainer kieContainer = loadContainerFromString(rules);
		this.kieContainer = kieContainer;
	}

	private List<Rule> loadRules() {
		List<Rule> rules = ruleRepository.findAll();
		return rules;
	}

	private KieContainer loadContainerFromString(List<Rule> rules) {
		long startTime = System.currentTimeMillis();
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieFileSystem kfs = ks.newKieFileSystem();

		for (Rule rule : rules) {
			String drl = rule.getContent();
			kfs.write("src/main/resources/" + drl.hashCode() + ".drl", drl);
		}

		KieBuilder kb = ks.newKieBuilder(kfs);

		kb.buildAll();
		if (kb.getResults().hasMessages(Message.Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Time to build rules : " + (endTime - startTime) + " ms");
		startTime = System.currentTimeMillis();
		KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
		endTime = System.currentTimeMillis();
		System.out.println("Time to load container: " + (endTime - startTime) + " ms");
		return kContainer;
	}

}

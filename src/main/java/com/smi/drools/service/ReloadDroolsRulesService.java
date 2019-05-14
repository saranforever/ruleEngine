package com.smi.drools.service;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smi.drools.model.Rule;
import com.smi.drools.repository.RuleRepository;

@Service
public class ReloadDroolsRulesService {

	public static KieContainer kieContainer;
	
	private static final String RULES_PATH = "src/main/resources/";
	
	private static final String RULES_FILE_EXTENSION = ".drl";

	@Autowired
	private RuleRepository ruleRepository;

	public void reload() {
		KieContainer kieReloadedContainer = loadContainerFromString(loadRules());
		ReloadDroolsRulesService.kieContainer = kieReloadedContainer;
	}

	public void reload(Rule rule) {
		List<Rule> rules = new ArrayList<Rule>();
		rules.add(rule);
		KieContainer kieReloadedContainer = loadContainerFromString(rules);
		ReloadDroolsRulesService.kieContainer = kieReloadedContainer;
	}

	private List<Rule> loadRules() {
		return ruleRepository.findAll();
	}

	private KieContainer loadContainerFromString(List<Rule> rules) {
		long startTime = System.currentTimeMillis();
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieFileSystem kfs = ks.newKieFileSystem();

		for (Rule rule : rules) {
			String drl = rule.getContent();
			kfs.write(RULES_PATH + drl.hashCode() + RULES_FILE_EXTENSION, drl);
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

package com.smi.drools.util;

import java.io.IOException;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smi.drools.dao.RuleRepository;
import com.smi.drools.entity.Rule;

@Service
public class DroolsRulesService {

	private KieContainer kieContainer;
	
	private KieFileSystem kfs;  

	private static final String RULES_PATH = "src/main/resources/";

	private static final String RULES_FILE_EXTENSION = ".drl";

	@Autowired
	private RuleRepository ruleRepository;

	private KieServices ks;

	private KieBuilder kb;

	private KieRepository kr;
	
	@Autowired
	public DroolsRulesService(RuleRepository ruleRepository) {
		this.ruleRepository = ruleRepository;
		reload();
	}
	
	public void reload() {
		try {
			this.kieContainer = loadContainerFromString(loadRules());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reload(Rule rule) {
		addRule(rule);
	}

	private List<Rule> loadRules() {
		return ruleRepository.findAll();
	}

	private KieContainer loadContainerFromString(List<Rule> rules) throws IOException {
		long startTime = System.currentTimeMillis();
		this.ks = KieServices.Factory.get();
		this.kr = this.ks.getRepository();
		this.kfs = this.ks.newKieFileSystem();

		for (Rule rule : rules) {
			String drl = rule.getContent();
			this.kfs.write(RULES_PATH + drl.hashCode() + RULES_FILE_EXTENSION, drl);
		}

		this.kb = this.ks.newKieBuilder(this.kfs);

		this.kb.buildAll();
		if (this.kb.getResults().hasMessages(Message.Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Time to build rules : " + (endTime - startTime) + " ms");
		startTime = System.currentTimeMillis();
		this.kieContainer = this.ks.newKieContainer(kr.getDefaultReleaseId());
		endTime = System.currentTimeMillis();
		System.out.println("Time to load container: " + (endTime - startTime) + " ms");
		return this.kieContainer;
	}
	
	public void addRule(Rule rule) {
		String drl = rule.getContent();
		this.kfs.write(RULES_PATH + drl.hashCode() + RULES_FILE_EXTENSION, drl);
		this.kb = this.ks.newKieBuilder(this.kfs);
		this.kb.buildAll();
		if (this.kb.getResults().hasMessages(Message.Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}
		this.kieContainer = this.ks.newKieContainer(this.kr.getDefaultReleaseId());
	}

	public KieContainer getKieContainer() {
		return this.kieContainer;
	}

}

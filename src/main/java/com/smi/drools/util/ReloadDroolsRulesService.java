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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.smi.drools.dao.RuleRepository;
import com.smi.drools.model.Rule;

@Service
public class ReloadDroolsRulesService {

	private KieContainer kieContainer;
	
	private KieFileSystem kfs;  

	private static final String RULES_PATH = "src/main/resources/";

	private static final String RULES_FILE_EXTENSION = ".drl";

	@Autowired
	private RuleRepository ruleRepository;
	
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

	private Resource[] getRuleFiles() throws IOException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		return resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
	}

	private KieContainer loadContainerFromString(List<Rule> rules) throws IOException {
		long startTime = System.currentTimeMillis();
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		this.kfs = ks.newKieFileSystem();

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
		this.kieContainer = ks.newKieContainer(kr.getDefaultReleaseId());
		endTime = System.currentTimeMillis();
		System.out.println("Time to load container: " + (endTime - startTime) + " ms");
		return this.kieContainer;
	}
	
	private void addRule(Rule rule) {
		String drl = rule.getContent();
		this.kfs.write(RULES_PATH + drl.hashCode() + RULES_FILE_EXTENSION, drl);
	}

	public KieContainer getKieContainer() {
		return this.kieContainer;
	}

}

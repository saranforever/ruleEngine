package com.smi.drools.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smi.drools.dao.RuleRepository;
import com.smi.drools.entity.Rule;
import com.smi.drools.model.CustomerDocument;
import com.smi.drools.model.CustomerDocumentContext;
import com.smi.drools.model.fact.BusinessRuleEnrichment;
import com.smi.drools.model.fact.NumberEnrichment;

@Service
public class DroolsRulesService {

	private static final String RULES_PATH = "src/main/resources/";

	private static final String RULES_FILE_EXTENSION = ".drl";

	private KieContainer kieContainer;

	private KieFileSystem kfs;

	private KieServices ks;

	private KieBuilder kb;

	private KieRepository kr;

	@Autowired
	private RuleRepository ruleRepository;

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
			this.kfs.write(RULES_PATH + rule.getRuleConfig().getId() + RULES_FILE_EXTENSION, drl);
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
		this.kfs.write(RULES_PATH + rule.getRuleConfig().getId() + RULES_FILE_EXTENSION, drl);
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

	public void removeRule(Long configId) {
		this.kfs.delete(RULES_PATH + configId + RULES_FILE_EXTENSION);
		this.kb = this.ks.newKieBuilder(this.kfs);
		this.kb.buildAll();
		if (this.kb.getResults().hasMessages(Message.Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}
		this.kieContainer = this.ks.newKieContainer(this.kr.getDefaultReleaseId());
	}

	public CustomerDocument triggerRule(CustomerDocument customerDocument) {
		KieSession kieSession = this.getKieContainer().newKieSession();

		kieSession.insert(customerDocument);
		Map<Integer, CustomerDocumentContext> customerDocumentsContextMap = customerDocument
				.getCustomerDocumentsContextMap();
		Set<Integer> keySet = customerDocumentsContextMap.keySet();
		keySet.stream().forEach(key -> {
			CustomerDocumentContext customerDocumentContext = customerDocumentsContextMap.get(key);
			// AutoLine Data
			if (customerDocumentContext.getAutoLineItemData() != null) {
				kieSession.insert(customerDocumentContext.getAutoLineItemData());
			}

			// FieldData
			customerDocumentContext.getFieldDatas().stream().forEach(fieldData -> {
				kieSession.insert(fieldData);
				fieldData.getFieldValues().stream().forEach(fieldValue -> {
					kieSession.insert(fieldValue);
				});
			});

			// Line Item Data
			customerDocumentContext.getLineItemDatas().stream().forEach(lineItemData -> {
				kieSession.insert(lineItemData);
				kieSession.insert(lineItemData.getAmount());
				kieSession.insert(lineItemData.getQty());
				kieSession.insert(lineItemData.getUnitPrice());
				kieSession.insert(lineItemData.getDescription());
			});
		});

		BusinessRuleEnrichment businessRuleEnrichment = new BusinessRuleEnrichment();
		kieSession.insert(businessRuleEnrichment);

		NumberEnrichment numberEnrichment = new NumberEnrichment();
		kieSession.insert(numberEnrichment);

		long startTime = System.currentTimeMillis();
		int ruleFiredCount = kieSession.fireAllRules();
		kieSession.destroy();
		long endTime = System.currentTimeMillis();
		System.out.println("Time to fetch and fire rule: " + (endTime - startTime) + " ms");
		System.out.println("Triggered" + ruleFiredCount + "DocumentContext");
		return customerDocument;
	}

}

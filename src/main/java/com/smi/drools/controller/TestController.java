package com.smi.drools.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smi.drools.dao.RuleBuilderRepository;
import com.smi.drools.dto.RuleConfigDTO;
import com.smi.drools.entity.RuleConfig;
import com.smi.drools.enumutil.EnrichmentEnum;
import com.smi.drools.enumutil.ModelTypeEnum;
import com.smi.drools.model.CustomerDocument;
import com.smi.drools.model.CustomerDocumentContext;
import com.smi.drools.model.fact.BusinessRuleEnrichment;
import com.smi.drools.model.fact.NumberEnrichment;
import com.smi.drools.service.IRuleConfigService;
import com.smi.drools.util.DroolsRulesService;

import ma.glasnost.orika.MapperFacade;

@CrossOrigin
@RequestMapping("/test")
@Controller
public class TestController {
	
	@Autowired
	private DroolsRulesService reloadDroolsRulesService;

	@Autowired
	private IRuleConfigService ruleConfigService;

	@Autowired
	private RuleBuilderRepository ruleBuilderRepository;
	
	@Autowired
	private MapperFacade mapperFacade;

	@ResponseBody
	@PostMapping("/testdoccontext")
	public CustomerDocument testDocumentContext(@RequestBody CustomerDocument customerDocument) {
		KieSession kieSession = reloadDroolsRulesService.getKieContainer().newKieSession();

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

	@ResponseBody
	@PostMapping("/createdocumentcontextrule")
	public ResponseEntity<RuleConfig> createDocRule(@RequestBody RuleConfigDTO ruleConfigDTO) throws Exception {
		RuleConfig ruleConfig = mapperFacade.map(ruleConfigDTO, RuleConfig.class);
		return new ResponseEntity<>(ruleConfigService.save(ruleConfig), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping("/reload")
	public String reload() {
		reloadDroolsRulesService.reload();
		return "ok";
	}

	@ResponseBody
	@GetMapping("/fetchfields/{model}")
	public List<ModelTypeEnum> fetchFields(@PathVariable("model") ModelTypeEnum modelTypeEnum) {
		return ModelTypeEnum.stream().filter(mt -> (mt.parent() != null && mt.parent().equals(modelTypeEnum)))
				.collect(Collectors.toList());
	}

	@ResponseBody
	@GetMapping(value = "/fetchBusinessMethods/{businessclass}")
	public List<String> getchBusinessMethods(@PathVariable("businessclass") EnrichmentEnum enrichmentEnum)
			throws ClassNotFoundException {
		return enrichmentEnum.equals(EnrichmentEnum.RULE) ? ruleBuilderRepository.findCommonRules()
				: EnrichmentEnum.getAllMethods(enrichmentEnum);
	}

	@ResponseBody
	@GetMapping(value = "/fetchEnrichmentClasses")
	public List<EnrichmentEnum> fetchEnrichmentClasses() {
		return EnrichmentEnum.stream().collect(Collectors.toList());
	}
	
	@ResponseBody
	@GetMapping(value = "/fetchRuleConfig/{id}")
	public ResponseEntity<RuleConfigDTO> fetchRuleConfigBy(@PathVariable("id") long id) {
		Optional<RuleConfig> ruleConfig = ruleConfigService.findRuleConfigById(id);
		return ResponseEntity.ok(mapperFacade.map(ruleConfig.get(), RuleConfigDTO.class));
	}
	
	@ResponseBody
	@GetMapping(value = "/fetchAllRuleConfig")
	public ResponseEntity<List<RuleConfigDTO>> fetchRuleConfigBy(@RequestParam("emailId") String emailId) {
		List<RuleConfig> ruleConfigs = ruleConfigService.findByEmailId(emailId);
		return ResponseEntity.ok(mapperFacade.mapAsList(ruleConfigs, RuleConfigDTO.class));
	}
}

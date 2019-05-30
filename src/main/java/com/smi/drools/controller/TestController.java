package com.smi.drools.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.smi.drools.dto.StatusDTO;
import com.smi.drools.entity.RuleConfig;
import com.smi.drools.enumutil.EnrichmentEnum;
import com.smi.drools.enumutil.ModelTypeEnum;
import com.smi.drools.model.CustomerDocument;
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
		return this.reloadDroolsRulesService.triggerRule(customerDocument);
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
	@GetMapping(value = "/deleteRuleConfig/{id}")
	public ResponseEntity<StatusDTO> deleteRuleConfigBy(@PathVariable("id") long id) {
		return ResponseEntity.ok(ruleConfigService.deleteRuleConfigById(id));
	}
	
	@ResponseBody
	@GetMapping(value = "/fetchAllRuleConfig")
	public ResponseEntity<List<RuleConfigDTO>> fetchRuleConfigBy(@RequestParam("emailId") String emailId) {
		List<RuleConfig> ruleConfigs = ruleConfigService.findByEmailId(emailId);
		return ResponseEntity.ok(mapperFacade.mapAsList(ruleConfigs, RuleConfigDTO.class));
	}
}

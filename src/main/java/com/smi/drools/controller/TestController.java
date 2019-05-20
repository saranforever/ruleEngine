package com.smi.drools.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smi.drools.enumutil.ConditionalEnum;
import com.smi.drools.enumutil.EnrichmentEnum;
import com.smi.drools.enumutil.FilterEnum;
import com.smi.drools.model.ActionBuilder;
import com.smi.drools.model.Address;
import com.smi.drools.model.Amount;
import com.smi.drools.model.AutoLineItemData;
import com.smi.drools.model.ConditionBuilder;
import com.smi.drools.model.CustomerDocument;
import com.smi.drools.model.CustomerDocumentContext;
import com.smi.drools.model.Description;
import com.smi.drools.model.FieldData;
import com.smi.drools.model.FieldValue;
import com.smi.drools.model.LineItemData;
import com.smi.drools.model.Message;
import com.smi.drools.model.Qty;
import com.smi.drools.model.Rule;
import com.smi.drools.model.RuleBuilder;
import com.smi.drools.model.RuleConfig;
import com.smi.drools.model.RuleTestConfig;
import com.smi.drools.model.UnitPrice;
import com.smi.drools.model.Validation;
import com.smi.drools.model.fact.BusinessRuleEnrichment;
import com.smi.drools.model.fact.NumberEnrichment;
import com.smi.drools.service.IRuleService;
import com.smi.drools.util.ReloadDroolsRulesService;
import com.smi.drools.util.RuleConfigUtil;

@RequestMapping("/test")
@Controller
public class TestController {

	@Autowired
	private ReloadDroolsRulesService reloadDroolsRulesService;

	@Autowired
	private IRuleService ruleService;

	@ResponseBody
	@RequestMapping("/address")
	public void test(int num) {
		Address address = new Address();
		address.setPostcode(generateRandom(num));
		KieSession kieSession = reloadDroolsRulesService.getKieContainer().newKieSession();
		
		BusinessRuleEnrichment result = new BusinessRuleEnrichment();
		kieSession.insert(address);
		kieSession.insert(result);
		int ruleFiredCount = kieSession.fireAllRules();
		kieSession.destroy();
		System.out.println("Triggered" + ruleFiredCount + "Rule");

		if (result.isPostCodeResult()) {
			System.out.println("Rule verification");
		}

	}

	@ResponseBody
	@RequestMapping("/testmsg")
	public void testMessage() {
		KieSession kieSession = reloadDroolsRulesService.getKieContainer().newKieSession();

		Message message = new Message();
		message.setStatus(1);
		message.setMsg("hello world!");

		kieSession.insert(message);
		int ruleFiredCount = kieSession.fireAllRules();
		kieSession.destroy();
		System.out.println("Triggered" + ruleFiredCount + "Message");

	}

	@ResponseBody
	@PostMapping("/testdoccontext")
	public CustomerDocument testDocumentContext(@RequestBody CustomerDocument customerDocument) {
		KieSession kieSession = reloadDroolsRulesService.getKieContainer().newKieSession();
		
		kieSession.insert(customerDocument);
		Map<Integer, CustomerDocumentContext> customerDocumentsContextMap = customerDocument.getCustomerDocumentsContextMap();
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
			
			//Line Item Data
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
		
		int ruleFiredCount = kieSession.fireAllRules();
		kieSession.destroy();
		System.out.println("Triggered" + ruleFiredCount + "DocumentContext");
		return customerDocument;
	}

	@ResponseBody
	@PostMapping("/createdocumentcontextrule")
	public String createDocRule(@RequestBody RuleConfig ruleConfig) {
		String ruleStr = RuleConfigUtil.buildRule(ruleConfig);
		
		Rule rule = new Rule();
		rule.setContent(ruleStr);
		rule.setCreateTime("");
		rule.setVersion("7");

		ruleService.save(rule, ruleConfig);
		/*reloadDroolsRulesService.reload();*/

		return "rule created";
	}

	@ResponseBody
	@RequestMapping("/createrule")
	public String createRule() {
		String ruleStr = "package com.smi.drools\r\n";
		ruleStr += "import com.smi.drools.model.Message;\r\n";
		ruleStr += "rule \"rule1\"\r\n";
		ruleStr += "\twhen\r\n";
		ruleStr += "Message( status == 1, myMessage : msg )";
		ruleStr += "\tthen\r\n";
		ruleStr += "\t\tSystem.out.println( 1+\":\"+myMessage );\r\n";
		ruleStr += "end\r\n";

		Rule rule = new Rule();
		rule.setContent(ruleStr);
		rule.setCreateTime("");
		rule.setRuleKey("score");
		rule.setVersion("1");

		ruleService.save(rule, null);
		reloadDroolsRulesService.reload();

		return "rule created";
	}

	/**
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/reload")
	public String reload() throws IOException {
		reloadDroolsRulesService.reload();
		return "ok";
	}

	/**
	 * @param num
	 * @return
	 */
	public String generateRandom(int num) {
		String chars = "0123456789";
		StringBuffer number = new StringBuffer();
		for (int i = 0; i < num; i++) {
			int rand = (int) (Math.random() * 10);
			number = number.append(chars.charAt(rand));
		}
		return number.toString();
	}

	@ResponseBody
	@RequestMapping("/RuleBuilderResponse")
	public RuleTestConfig RuleBuilderResponse() {
		RuleBuilder ruleBuilder = new RuleBuilder();
		ruleBuilder.setRuleName("Rule2");
		ConditionBuilder conditionBuilder = new ConditionBuilder();
		conditionBuilder.setFilter(FilterEnum.EQUALS);
		conditionBuilder.setMetaField("documentType");
		conditionBuilder.setMetaValue("EDI");
		conditionBuilder.setConditionOperator(ConditionalEnum.AND);
		List<ConditionBuilder> conditionBuilders = new ArrayList<>();
		conditionBuilders.add(conditionBuilder);

		ActionBuilder actionBuilder1 = new ActionBuilder();
		actionBuilder1.setEnrichement(EnrichmentEnum.BUSINESSRULEENRICHMENT);
		actionBuilder1.setEnrichmentAction("123");
		ActionBuilder actionBuilder2 = new ActionBuilder();
		actionBuilder2.setEnrichement(EnrichmentEnum.BUSINESSRULEENRICHMENT);
		actionBuilder2.setEnrichmentAction("321");
		List<ActionBuilder> actionBuilders = new ArrayList<ActionBuilder>();
		actionBuilders.add(actionBuilder1);
		actionBuilders.add(actionBuilder2);

		ruleBuilder.setActionBuilders(actionBuilders);
		ruleBuilder.setConditionBuilders(conditionBuilders);

		RuleTestConfig ruleTestConfig = new RuleTestConfig();
		List<RuleBuilder> ruleBuilders = new ArrayList<RuleBuilder>();
		ruleBuilders.add(ruleBuilder);
		ruleBuilders.add(ruleBuilder);

		ruleTestConfig.setModelType("DocumentContext");
		ruleTestConfig.setRuleBuilders(ruleBuilders);
		return ruleTestConfig;
	}

	public AutoLineItemData buildAutoLineItemData() {
		AutoLineItemData autoLineItemData = new AutoLineItemData();
		autoLineItemData.setAutoLine("1");
		return autoLineItemData;
	}

	public List<FieldData> buildFieldData() {

		Validation validation = new Validation();
		validation.setReason("Nil");
		validation.setResult("Success");

		FieldValue fieldValue1 = new FieldValue();
		fieldValue1.setValidation(validation);
		fieldValue1.setValue("");

		List<FieldValue> fieldValues1 = new ArrayList<FieldValue>();
		fieldValues1.add(fieldValue1);

		FieldData fieldData1 = new FieldData();
		fieldData1.setFieldName("Invoice");
		fieldData1.setFieldValues(fieldValues1);
		// ------------EOR-------------------------

		// >>>>>>>>>>>>Build FieldValue2<<<<<<<<<<<
		FieldValue fieldValue2 = new FieldValue();
		fieldValue2.setValidation(validation);
		fieldValue2.setValue(
				"Electro Solution, Joel Willis 858 Lynn Street 07002 Bayonne New Jersey United States joel.willis63@example.com (683)-556-5104");

		List<FieldValue> fieldValues2 = new ArrayList<FieldValue>();
		fieldValues2.add(fieldValue2);

		FieldData fieldData2 = new FieldData();
		fieldData2.setFieldName("ShipTo");
		fieldData2.setFieldValues(fieldValues2);
		// ------------EOR-------------------------

		// >>>>>>>>>>>>Build FieldValue3<<<<<<<<<<<
		FieldValue fieldValue3 = new FieldValue();
		fieldValue3.setValidation(validation);
		fieldValue3.setValue(
				"Electro Solution, Joel Willis 858 Lynn Street 07002 Bayonne New Jersey United States joel.willis63@example.com (683)-556-5104");

		List<FieldValue> fieldValues3 = new ArrayList<FieldValue>();
		fieldValues3.add(fieldValue3);

		FieldData fieldData3 = new FieldData();
		fieldData3.setFieldName("BillTo");
		fieldData3.setFieldValues(fieldValues3);
		// ------------EOR-------------------------

		List<FieldData> fieldDatas = new ArrayList<FieldData>();
		fieldDatas.add(fieldData1);
		fieldDatas.add(fieldData2);
		fieldDatas.add(fieldData3);

		return fieldDatas;
	}

	public List<LineItemData> buildLineItemData() {

		Validation lineValidation = new Validation();
		lineValidation.setReason("Nil");
		lineValidation.setResult("Nil");

		List<LineItemData> lineItemDatas = new ArrayList<LineItemData>();
		double amount = 1000.70; 
		for (int i = 0; i < 1000; i++) {
			// >>>>>>>>>>>>Build LineItemData1<<<<<<<<<<<
			Amount amount1 = new Amount();
			amount1.setValidation(lineValidation);
			amount1.setValue((amount + 1.11) + "");
			
			UnitPrice unitPrice1 = new UnitPrice();
			unitPrice1.setValidation(lineValidation);
			unitPrice1.setValue("450.00 ");
			
			Description description1 = new Description();
			description1.setValidation(lineValidation);
			description1.setValue("Desk Combination ");
			
			Qty qty1 = new Qty();
			qty1.setValidation(lineValidation);
			qty1.setValue("3 ");
			
			LineItemData lineItemData1 = new LineItemData();
			lineItemData1.setAmount(amount1);
			lineItemData1.setUnitPrice(unitPrice1);
			lineItemData1.setDescription(description1);
			lineItemData1.setQty(qty1);
			lineItemDatas.add(lineItemData1);
			// ------------EOR-------------------------
		}

		// >>>>>>>>>>>>Build LineItemData2<<<<<<<<<<<
		/*Amount amount2 = new Amount();
		amount2.setValidation(lineValidation);
		amount2.setValue("225.00 ");

		UnitPrice unitPrice2 = new UnitPrice();
		unitPrice2.setValidation(lineValidation);
		unitPrice2.setValue("25.00 ");

		Description description2 = new Description();
		description2.setValidation(lineValidation);
		description2.setValue("Drawer Black ");

		Qty qty2 = new Qty();
		qty2.setValidation(lineValidation);
		qty2.setValue("9 ");

		LineItemData lineItemData2 = new LineItemData();
		lineItemData2.setAmount(amount2);
		lineItemData2.setUnitPrice(unitPrice2);
		lineItemData2.setDescription(description2);
		lineItemData2.setQty(qty2);*/
		// ------------EOR-------------------------

		// >>>>>>>>>>>>Build LineItemData2<<<<<<<<<<<
		/*Amount amount3 = new Amount();
		amount3.setValidation(lineValidation);
		amount3.setValue("4,002.00 ");

		UnitPrice unitPrice3 = new UnitPrice();
		unitPrice3.setValidation(lineValidation);
		unitPrice3.setValue("800.40 ");

		Description description3 = new Description();
		description3.setValidation(lineValidation);
		description3.setValue("Customizable Desk ");

		Qty qty3 = new Qty();
		qty3.setValidation(lineValidation);
		qty3.setValue("5 ");

		LineItemData lineItemData3 = new LineItemData();
		lineItemData3.setAmount(amount3);
		lineItemData3.setUnitPrice(unitPrice3);
		lineItemData3.setDescription(description3);
		lineItemData3.setQty(qty3);*/
		// ------------EOR-------------------------

		/*lineItemDatas.add(lineItemData2);
		lineItemDatas.add(lineItemData3);*/
		return lineItemDatas;
	}

	@ResponseBody
	@RequestMapping(value = "/response")
	public List<CustomerDocument> getCustomerDocumentResponse() {

		CustomerDocumentContext customerDocumentContext = new CustomerDocumentContext();
		customerDocumentContext.setAutoLineItemData(this.buildAutoLineItemData());
		customerDocumentContext.setFieldDatas(this.buildFieldData());
		customerDocumentContext.setLineItemDatas(this.buildLineItemData());

		Map<Integer, CustomerDocumentContext> customerMap = new HashMap<>();
		customerMap.put(1, customerDocumentContext);

		CustomerDocument customerDocument1 = new CustomerDocument();
		customerDocument1.setSenderEid("S123");
		customerDocument1.setReceiverEid("R123");
		customerDocument1.setCustomerDocumentsContextMap(customerMap);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			System.out.println(mapper.writeValueAsString(customerDocument1));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		List<CustomerDocument> customerDocuments = new ArrayList<>();
		customerDocuments.add(customerDocument1);

		KieSession kieSession = reloadDroolsRulesService.getKieContainer().newKieSession();

		BusinessRuleEnrichment businessRuleEnrichment = new BusinessRuleEnrichment();
		kieSession.insert(customerDocument1);
		kieSession.insert(businessRuleEnrichment);

		int ruleFiredCount = kieSession.fireAllRules();
		kieSession.destroy();
		System.out.println("Triggered" + ruleFiredCount + "DocumentContext");
		return customerDocuments;
	}
}

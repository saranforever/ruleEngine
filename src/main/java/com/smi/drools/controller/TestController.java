package com.smi.drools.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smi.drools.enumutil.EnrichmentEnum;
import com.smi.drools.model.ActionBuilder;
import com.smi.drools.model.Address;
import com.smi.drools.model.Amount;
import com.smi.drools.model.AutoLineItemData;
import com.smi.drools.model.ConditionBuilder;
import com.smi.drools.model.CustomerDocument;
import com.smi.drools.model.CustomerDocumentContext;
import com.smi.drools.model.Description;
import com.smi.drools.model.DocumentContext;
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
import com.smi.drools.repository.RuleRepository;
import com.smi.drools.service.ReloadDroolsRulesService;
import com.smi.drools.util.RuleConfigUtil;

@RequestMapping("/test")
@Controller
public class TestController {

	@Resource
	private ReloadDroolsRulesService rules;

	@Autowired
	private RuleRepository ruleRepository;

	@ResponseBody
	@RequestMapping("/address")
	public void test(int num) {
		Address address = new Address();
		address.setPostcode(generateRandom(num));
		KieSession kieSession = ReloadDroolsRulesService.kieContainer.newKieSession();

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
		KieSession kieSession = ReloadDroolsRulesService.kieContainer.newKieSession();

		Message message = new Message();
		message.setStatus(1);
		message.setMsg("hello world!");

		kieSession.insert(message);
		int ruleFiredCount = kieSession.fireAllRules();
		kieSession.destroy();
		System.out.println("Triggered" + ruleFiredCount + "Message");

	}

	@ResponseBody
	@RequestMapping("/testdoccontext")
	public void testDocumentContext() {

		DocumentContext documentContext = new DocumentContext();
		documentContext.setDocumentType("EDI");
		documentContext.setDocumentVersion("00401");
		documentContext.setSenderEid("1124");

		CustomerDocument customerDocument = new CustomerDocument();
		customerDocument.setSenderEid("S123");
		customerDocument.setReceiverEid("R123");

		KieSession kieSession = ReloadDroolsRulesService.kieContainer.newKieSession();
		kieSession.insert(customerDocument);
		int ruleFiredCount = kieSession.fireAllRules();
		kieSession.destroy();
		System.out.println("Triggered" + ruleFiredCount + "DocumentContext");

	}

	@ResponseBody
	@PostMapping("/createdocumentcontextrule")
	public String createDocRule(@RequestBody RuleConfig ruleConfig) {
		String ruleStr = RuleConfigUtil.buildRule(ruleConfig);

		Rule rule = new Rule();
		rule.setContent(ruleStr);
		rule.setCreateTime("");
		rule.setRuleKey(ruleConfig.getModelType());
		rule.setVersion("4");

		ruleRepository.save(rule);
		rules.reload();

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

		ruleRepository.save(rule);
		rules.reload();

		return "rule created";
	}

	/**
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/reload")
	public String reload() throws IOException {
		rules.reload();
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
		conditionBuilder.setFilter("equals");
		conditionBuilder.setKey("documentType");
		conditionBuilder.setValue("EDI");
		conditionBuilder.setCondition("and");
		List<ConditionBuilder> conditionBuilders = new ArrayList<ConditionBuilder>();
		conditionBuilders.add(conditionBuilder);

		ActionBuilder actionBuilder1 = new ActionBuilder();
		actionBuilder1.setKey(EnrichmentEnum.BUSINESSRULEENRICHMENT);
		actionBuilder1.setValue("123");
		ActionBuilder actionBuilder2 = new ActionBuilder();
		actionBuilder2.setKey(EnrichmentEnum.BUSINESSRULEENRICHMENT);
		actionBuilder2.setValue("321");
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

		// >>>>>>>>>>>>Build LineItemData1<<<<<<<<<<<
		Amount amount1 = new Amount();
		amount1.setValidation(lineValidation);
		amount1.setValue("1,350.00 ");

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
		// ------------EOR-------------------------

		// >>>>>>>>>>>>Build LineItemData2<<<<<<<<<<<
		Amount amount2 = new Amount();
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
		lineItemData2.setQty(qty2);
		// ------------EOR-------------------------

		// >>>>>>>>>>>>Build LineItemData2<<<<<<<<<<<
		Amount amount3 = new Amount();
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
		lineItemData3.setQty(qty3);
		// ------------EOR-------------------------

		List<LineItemData> lineItemDatas = new ArrayList<LineItemData>();
		lineItemDatas.add(lineItemData1);
		lineItemDatas.add(lineItemData2);
		lineItemDatas.add(lineItemData3);
		return lineItemDatas;
	}

	@ResponseBody
	@RequestMapping(value = "/response")
	public List<CustomerDocument> getCustomerDocumentResponse() {

		CustomerDocumentContext customerDocumentContext = new CustomerDocumentContext();
		customerDocumentContext.setAutoLineItemData(this.buildAutoLineItemData());
		customerDocumentContext.setFieldDatas(this.buildFieldData());
		customerDocumentContext.setLineItemDatas(this.buildLineItemData());

		Map<Integer, CustomerDocumentContext> customerMap = new HashMap<Integer, CustomerDocumentContext>();
		customerMap.put(1, customerDocumentContext);

		CustomerDocument customerDocument1 = new CustomerDocument();
		customerDocument1.setSenderEid("S123");
		customerDocument1.setReceiverEid("R123");
		customerDocument1.setCustomerDocumentsContextMap(customerMap);

		CustomerDocument customerDocument2 = new CustomerDocument();
		customerDocument2.setSenderEid("3333");
		customerDocument2.setReceiverEid("4444");
		customerDocument2.setCustomerDocumentsContextMap(customerMap);

		List<CustomerDocument> customerDocuments = new ArrayList<CustomerDocument>();
		customerDocuments.add(customerDocument1);
		customerDocuments.add(customerDocument2);

		KieSession kieSession = ReloadDroolsRulesService.kieContainer.newKieSession();

		BusinessRuleEnrichment businessRuleEnrichment = new BusinessRuleEnrichment();
		kieSession.insert(customerDocument1);
		kieSession.insert(businessRuleEnrichment);

		int ruleFiredCount = kieSession.fireAllRules();
		kieSession.destroy();
		System.out.println("Triggered" + ruleFiredCount + "DocumentContext");
		return customerDocuments;
	}
}

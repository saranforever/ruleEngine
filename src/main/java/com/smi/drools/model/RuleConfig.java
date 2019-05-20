package com.smi.drools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smi.drools.enumutil.ConditionalEnum;
import com.smi.drools.enumutil.EnrichmentEnum;
import com.smi.drools.enumutil.FilterEnum;
import com.smi.drools.enumutil.ModelTypeEnum;

import lombok.Data;

@Entity
@Table(name = "RULE_CONFIG")
@Data
public class RuleConfig implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 859456017285881118L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	
	@Column(name = "ruleDescription")
	private String ruleDescription;

	@OneToMany(mappedBy = "ruleConfig", cascade = CascadeType.ALL)
    private List<RuleBuilder> ruleBuilders;

	public static void main(String[] args) {
		RuleConfig ruleConfig = new RuleConfig();
		ruleConfig.setName("Invoice Enricher");
		ruleConfig.setRuleDescription("This rule is to enrich invoice number if it is empty");

		List<RuleBuilder> ruleBuilders = new ArrayList<>();
		// Rule 1
		RuleBuilder ruleBuilder = new RuleBuilder();
		ruleBuilder.setRuleName("drule1");
		List<ConditionBuilder> conditionBuilders = new ArrayList<>();

		ConditionBuilder conditionBuilder = new ConditionBuilder();
		conditionBuilder.setModelType(ModelTypeEnum.CUSTOMERDOCUMENT);
		conditionBuilder.setMetaField("senderEid");
		conditionBuilder.setFilter(FilterEnum.EQUALS);
		conditionBuilder.setMetaValue("S123");
		conditionBuilder.setConditionOperator(ConditionalEnum.AND);
		conditionBuilders.add(conditionBuilder);

		ConditionBuilder conditionBuilder1 = new ConditionBuilder();
		conditionBuilder1.setModelType(ModelTypeEnum.CUSTOMERDOCUMENT);
		conditionBuilder1.setMetaField("receiverEid");
		conditionBuilder1.setFilter(FilterEnum.EQUALS);
		conditionBuilder1.setMetaValue("R123");
		conditionBuilder1.setConditionOperator(null);
		conditionBuilders.add(conditionBuilder1);

		ruleBuilder.setConditionBuilders(conditionBuilders);

		List<ActionBuilder> actionBuilders = new ArrayList<>();

		ActionBuilder actionBuilder = new ActionBuilder();
		actionBuilder.setEnrichement(EnrichmentEnum.BUSINESSRULEENRICHMENT);
		actionBuilder.setEnrichmentAction("enrichInvoiceNoWithTimeStatmp");
		actionBuilders.add(actionBuilder);

		ActionBuilder actionBuilder1 = new ActionBuilder();
		actionBuilder1.setEnrichement(EnrichmentEnum.BUSINESSRULEENRICHMENT);
		actionBuilder1.setEnrichmentAction("enrichInvoiceNoWithDateFormat");
		actionBuilders.add(actionBuilder1);

		ruleBuilder.setActionBuilders(actionBuilders);
		ruleBuilders.add(ruleBuilder);
		ruleConfig.setRuleBuilders(ruleBuilders);

		ObjectMapper mapper = new ObjectMapper();
		try {
			String writeValueAsString = mapper.writeValueAsString(ruleConfig);
			System.out.println(writeValueAsString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

}

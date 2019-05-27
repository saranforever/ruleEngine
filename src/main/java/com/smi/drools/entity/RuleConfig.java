package com.smi.drools.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smi.drools.audit.RuleConfigEntityListener;
import com.smi.drools.enumutil.ConditionalEnum;
import com.smi.drools.enumutil.EnrichmentEnum;
import com.smi.drools.enumutil.FilterEnum;
import com.smi.drools.enumutil.ModelTypeEnum;

import lombok.Data;

@Entity
@Table(name = "RULE_CONFIG")
@EntityListeners(RuleConfigEntityListener.class)
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
	
	@NotEmpty
	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@NotEmpty
	@Column(name = "ruleDescription")
	private String ruleDescription;

	@OneToMany(mappedBy = "ruleConfig", cascade = CascadeType.ALL)
	private List<RuleBuilder> ruleBuilders;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "rule_id", referencedColumnName = "id")
	private Rule rule;

	@Column(name = "enable")
	private boolean enable;

	public static void main(String[] args) {
		RuleConfig ruleConfig = new RuleConfig();
		ruleConfig.setName("Invoice Enricher");
		ruleConfig.setRuleDescription("This rule is to enrich invoice number if it is empty");

		List<RuleBuilder> ruleBuilders = new ArrayList<>();
		// Rule 1
		RuleBuilder ruleBuilder = new RuleBuilder();
		ruleBuilder.setRuleName("drule11");
		ruleBuilder.setRuleGroupName("");
		ruleBuilder.setModelType(ModelTypeEnum.CUSTOMERDOCUMENT);
		List<ConditionBuilder> conditionBuilders = new ArrayList<>();

		ConditionBuilder conditionBuilder = new ConditionBuilder();
		conditionBuilder.setMetaField(ModelTypeEnum.SENDEREID);
		conditionBuilder.setFilter(FilterEnum.EQUALS);
		conditionBuilder.setMetaValue("S123");
		conditionBuilder.setConditionOperator(ConditionalEnum.AND);
		conditionBuilders.add(conditionBuilder);

		ConditionBuilder conditionBuilder1 = new ConditionBuilder();
		conditionBuilder1.setMetaField(ModelTypeEnum.RECEIVEREID);
		conditionBuilder1.setFilter(FilterEnum.EQUALS);
		conditionBuilder1.setMetaValue("R123");
		conditionBuilder1.setConditionOperator(null);
		conditionBuilders.add(conditionBuilder1);

		ruleBuilder.setConditionBuilders(conditionBuilders);

		List<ActionBuilder> actionBuilders = new ArrayList<>();

		ActionBuilder actionBuilder = new ActionBuilder();
		actionBuilder.setEnrichement(EnrichmentEnum.RULE);
		actionBuilder.setEnrichmentAction("Enricher Common");
		actionBuilders.add(actionBuilder);

		/*ActionBuilder actionBuilder1 = new ActionBuilder();
		actionBuilder1.setEnrichement(EnrichmentEnum.NUMBERENRICHMENT);
		actionBuilder1.setEnrichmentAction("roundOffAmount");
		actionBuilders.add(actionBuilder1);*/

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

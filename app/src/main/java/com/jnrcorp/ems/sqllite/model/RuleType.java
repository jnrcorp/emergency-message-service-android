package com.jnrcorp.ems.sqllite.model;

import java.util.ArrayList;
import java.util.List;

import com.jnrcorp.ems.R;

public enum RuleType {

	CONTAINS(1, R.string.contain_singular, R.string.contain_plural, R.string.contain_detail),
	ENDS_WITH(3, R.string.end_with_singular, R.string.end_with_plural, R.string.end_with_detail),
	STARTS_WITH(2, R.string.start_with_singular, R.string.start_with_plural, R.string.start_with_detail),
	EQUALS(6, R.string.equal_singular, R.string.equal_plural, R.string.equal_with_detail),
	OF_LENGTH(4, R.string.of_length_singular, R.string.of_length_plural, R.string.of_length_detail),
	RANGE(5, R.string.range_singular, R.string.range_plural, R.string.range_detail);

	private int id;
	private int label;
	private int labelPlural;
	private int description;

	private RuleType(int id, int label, int labelPlural, int description) {
		this.id = id;
		this.label = label;
		this.labelPlural = labelPlural;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public int getLabelResource() {
		return label;
	}

	public int getLabelPluralResource() {
		return labelPlural;
	}

	public int getDescriptionResource() {
		return description;
	}

	public static RuleType getById(int id) {
		for (RuleType ruleType : RuleType.values()) {
			if (ruleType.getId() == id) {
				return ruleType;
			}
		}
		return null;
	}

	public static List<String> getRuleTypesForAdapter() {
		List<String> ruleTypes = new ArrayList<String>();
		for (RuleType ruleType : RuleType.values()) {
			ruleTypes.add(ruleType.toString());
		}
		return ruleTypes;
	}

	public static List<RuleType> getForRuleTypeAdapter() {
		List<RuleType> ruleTypes = new ArrayList<RuleType>();
		for (RuleType ruleType : RuleType.values()) {
			ruleTypes.add(ruleType);
		}
		return ruleTypes;
	}

}
package com.jnrcorp.ems.rule.validator;

import android.content.Context;

import com.jnrcorp.ems.model.ValidationResult;
import com.jnrcorp.ems.sqllite.model.RuleFilter;

public abstract class RuleFilterValidator {

	protected abstract boolean phoneNumberFitsRule(String fromPhoneNumber, RuleFilter ruleFilter);

	public abstract ValidationResult inputValid(Context context, String phoneNumberStart, String phoneNumberEnd);

	public boolean isRuleAppropriate(String fromPhoneNumber, RuleFilter ruleFilter) {
		return phoneNumberFitsRule(fromPhoneNumber, ruleFilter);
	}

	protected String sanitize(String fromPhoneNumber) {
		return fromPhoneNumber.replaceAll("+", "");
	}

}

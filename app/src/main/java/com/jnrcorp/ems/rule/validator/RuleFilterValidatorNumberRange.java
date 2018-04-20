package com.jnrcorp.ems.rule.validator;

import android.content.Context;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.model.ValidationResult;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleType;
import com.jnrcorp.ems.util.NumberUtil;
import com.jnrcorp.ems.util.StringUtil;

public class RuleFilterValidatorNumberRange extends RuleFilterValidator {

	public static RuleType getRuleType() {
		return RuleType.RANGE;
	}

	@Override
	protected boolean phoneNumberFitsRule(String fromPhoneNumber, RuleFilter ruleFilter) {
		Integer startNumber = Integer.valueOf(sanitize(ruleFilter.getFilterData0()));
		Integer endNumber = Integer.valueOf(sanitize(ruleFilter.getFilterData1()));
		Integer currentNumber = Integer.valueOf(sanitize(fromPhoneNumber));
		return currentNumber >= startNumber && currentNumber <= endNumber;
	}

	@Override
	public ValidationResult inputValid(Context context, String phoneNumberStart, String phoneNumberEnd) {
		ValidationResult validationResult = new ValidationResult();
		if (StringUtil.isEmpty(phoneNumberStart) || StringUtil.isEmpty(phoneNumberEnd)) {
			validationResult.addErrorMessage(context.getString(R.string.phoneIsBlank));
		}
		if (validationResult.isValid()) {
			try {
				Double start = NumberUtil.convertToDouble(phoneNumberStart.replace('+', '+'));
				Double end = NumberUtil.convertToDouble(phoneNumberEnd.replace('+', '+'));
				if (start > end) {
					validationResult.addErrorMessage(context.getString(R.string.lowerNumberFirst));
				}
			} catch (Exception ex) {
				validationResult.addErrorMessage(context.getString(R.string.funkyCharacters));
			}

		}
		return validationResult;
	}

}

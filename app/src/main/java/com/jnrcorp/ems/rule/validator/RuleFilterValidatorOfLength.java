package com.jnrcorp.ems.rule.validator;

import android.content.Context;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.model.ValidationResult;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleType;
import com.jnrcorp.ems.util.NumberUtil;
import com.jnrcorp.ems.util.StringUtil;

public class RuleFilterValidatorOfLength extends RuleFilterValidator {

	public static RuleType getRuleType() {
		return RuleType.OF_LENGTH;
	}

	@Override
	protected boolean phoneNumberFitsRule(String fromPhoneNumber, RuleFilter ruleFilter) {
		return fromPhoneNumber.length() == Integer.valueOf(ruleFilter.getFilterData0());
	}

	@Override
	public ValidationResult inputValid(Context context, String phoneNumberStart, String phoneNumberEnd) {
		ValidationResult validationResult = new ValidationResult();
		if (StringUtil.isEmpty(phoneNumberStart)) {
			validationResult.addErrorMessage(context.getString(R.string.phoneIsBlank));
		} else if (!NumberUtil.isNumeric(phoneNumberStart)) {
			validationResult.addErrorMessage(context.getString(R.string.ofLengthMustBeNumber));
		}
		if (validationResult.isValid()) {
			Integer lengthInput = NumberUtil.convertToInteger(phoneNumberStart);
			if (lengthInput > 20) {
				validationResult.addErrorMessage(context.getString(R.string.lengthToLong));
			}
		}
		return validationResult;
	}

}

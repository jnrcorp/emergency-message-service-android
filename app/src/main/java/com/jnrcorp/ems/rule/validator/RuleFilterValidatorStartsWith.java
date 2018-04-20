package com.jnrcorp.ems.rule.validator;

import android.content.Context;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.model.ValidationResult;
import com.jnrcorp.ems.sqllite.model.RuleFilter;
import com.jnrcorp.ems.sqllite.model.RuleType;
import com.jnrcorp.ems.util.StringUtil;

public class RuleFilterValidatorStartsWith extends RuleFilterValidator {

	public static RuleType getRuleType() {
		return RuleType.STARTS_WITH;
	}

	@Override
	protected boolean phoneNumberFitsRule(String fromPhoneNumber, RuleFilter ruleFilter) {
		return fromPhoneNumber.startsWith(ruleFilter.getFilterData0());
	}

	@Override
	public ValidationResult inputValid(Context context, String phoneNumberStart, String phoneNumberEnd) {
		ValidationResult validationResult = new ValidationResult();
		if (StringUtil.isEmpty(phoneNumberStart)) {
			validationResult.addErrorMessage(context.getString(R.string.phoneIsBlank));
		} else if (!phoneNumberStart.startsWith("+")) {
			validationResult.addErrorMessage(context.getString(R.string.mustStartWithPlusSign));
		}
		return validationResult;
	}

}

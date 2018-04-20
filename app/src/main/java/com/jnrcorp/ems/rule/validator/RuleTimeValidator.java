package com.jnrcorp.ems.rule.validator;

import java.util.Date;

import android.content.Context;

import com.jnrcorp.ems.R;
import com.jnrcorp.ems.model.ValidationResult;
import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.util.TimeUtil;

public class RuleTimeValidator {

	private RuleTime ruleTime;
	private Context context;

	public RuleTimeValidator(Context context, RuleTime ruleTime) {
		super();
		this.context = context;
		this.ruleTime = ruleTime;
	}

	public ValidationResult validate() {
		ValidationResult validationResult = new ValidationResult();
		Date timeStart = ruleTime.getTimeStart();
		Date timeEnd = ruleTime.getTimeEnd();
		if (TimeUtil.areEqual(timeStart, timeEnd)) {
			validationResult.addErrorMessage(context.getString(R.string.timesAreEqual));
		}
		if (noDaysSelected()) {
			validationResult.addErrorMessage(context.getString(R.string.noDaysSelected));
		}
		return validationResult;
	}

	private boolean noDaysSelected() {
		return ruleTime.getRuleTimeDays() == null || ruleTime.getRuleTimeDays().size() == 0;
	}

}

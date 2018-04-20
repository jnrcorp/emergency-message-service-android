package com.jnrcorp.ems.util;

import android.content.res.Resources;

import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.sqllite.model.RuleTimeDay;

public class RuleTimeUtils {

	private RuleTimeUtils() {
		super();
	}

	public static String getDaysCommaSeparated(Resources resources, RuleTime ruleTime) {
		StringBuilder ruleDays = new StringBuilder();
		boolean first = true;
		for (RuleTimeDay ruleTimeDay : ruleTime.getRuleTimeDays()) {
			if (!first) {
				ruleDays.append(", ");
			} else {
				first = false;
			}
			String dayShort = resources.getString(ruleTimeDay.getDay().getMediumResource());
			ruleDays.append(dayShort);
		}
		return ruleDays.toString();
	}

}

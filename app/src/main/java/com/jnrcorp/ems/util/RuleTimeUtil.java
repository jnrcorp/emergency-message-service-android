package com.jnrcorp.ems.util;

import java.util.Calendar;
import java.util.Locale;

import com.jnrcorp.ems.sqllite.model.EmergencyMessageServiceRule;
import com.jnrcorp.ems.sqllite.model.RuleTime;
import com.jnrcorp.ems.sqllite.model.RuleTimeDay;

public class RuleTimeUtil {

	private RuleTimeUtil() {
		super();
	}

	public static boolean fitsSchedule(EmergencyMessageServiceRule emsRule) {
		boolean fitsSchedule = false;
		if (emsRule.getRuleTimes() != null && emsRule.getRuleTimes().size() > 0) {
			for (RuleTime ruleTime : emsRule.getRuleTimes()) {
				if (isWithinSchedule(ruleTime)) {
					fitsSchedule = true;
				}
			}
		} else {
			fitsSchedule = true;
		}
		return fitsSchedule;
	}

	private static boolean isWithinSchedule(RuleTime ruleTime) {
		Calendar now = Calendar.getInstance(Locale.getDefault());
		boolean onValidDay = onValidDay(ruleTime, now);
		boolean withinTimeFrame = withinTimeFrame(ruleTime, now);
		return onValidDay && withinTimeFrame;
	}

	private static boolean withinTimeFrame(RuleTime ruleTime, Calendar now) {
		Calendar timeStart = TimeUtil.convertToCalendar(ruleTime.getTimeStart());
		boolean isAfterTimeStart = isAfterTimeStart(now, timeStart);
		Calendar timeEnd = TimeUtil.convertToCalendar(ruleTime.getTimeEnd());
		boolean isBeforeTimeEnd = isBeforeTimeEnd(now, timeEnd);
		return isAfterTimeStart && isBeforeTimeEnd;
	}

	private static boolean isBeforeTimeEnd(Calendar now, Calendar timeEnd) {
		int hourNow = now.get(Calendar.HOUR_OF_DAY);
		int minNow = now.get(Calendar.MINUTE);
		int hourEnd = timeEnd.get(Calendar.HOUR_OF_DAY);
		int minEnd = timeEnd.get(Calendar.MINUTE);
		return hourNow <= hourEnd && minNow <= minEnd;
	}

	private static boolean isAfterTimeStart(Calendar now, Calendar timeStart) {
		int hourNow = now.get(Calendar.HOUR_OF_DAY);
		int minNow = now.get(Calendar.MINUTE);
		int hourStart = timeStart.get(Calendar.HOUR_OF_DAY);
		int minStart = timeStart.get(Calendar.MINUTE);
		return hourNow >= hourStart && minNow >= minStart;
	}

	private static boolean onValidDay(RuleTime ruleTime, Calendar now) {
		boolean onValidDay = false;
		if (ruleTime.getRuleTimeDays() != null && ruleTime.getRuleTimeDays().size() > 0) {
			for (RuleTimeDay day : ruleTime.getRuleTimeDays()) {
				int validCalendarDay = day.getDay().getCalendarDay();
				if (validCalendarDay == now.get(Calendar.DAY_OF_WEEK)) {
					onValidDay = true;
				}
			}
		}
		return onValidDay;
	}

}

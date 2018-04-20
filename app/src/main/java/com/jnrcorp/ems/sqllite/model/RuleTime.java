package com.jnrcorp.ems.sqllite.model;

import java.util.Date;
import java.util.List;

public class RuleTime {

	private long timeId;
	private long ruleId;
	private Date timeStart;
	private Date timeEnd;

	private List<RuleTimeDay> ruleTimeDays;

	public RuleTime() {
		super();
	}

	public long getTimeId() {
		return timeId;
	}

	public void setTimeId(long timeId) {
		this.timeId = timeId;
	}

	public long getRuleId() {
		return ruleId;
	}

	public void setRuleId(long ruleId) {
		this.ruleId = ruleId;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public List<RuleTimeDay> getRuleTimeDays() {
		return ruleTimeDays;
	}

	public void setRuleTimeDays(List<RuleTimeDay> ruleTimeDays) {
		this.ruleTimeDays = ruleTimeDays;
	}

}

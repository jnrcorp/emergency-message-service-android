package com.jnrcorp.ems.sqllite.model;

public class RuleFilter {

	private long filterId;
	private long ruleId;
	private FilterType filterType;
	private RuleType ruleType;
	private String filterData0;
	private String filterData1;

	public RuleFilter(FilterType filterType) {
		super();
		this.filterType = filterType;
	}

	public RuleFilter(RuleFilter ruleFilter) {
		super();
		this.filterId = ruleFilter.filterId;
		this.ruleId = ruleFilter.ruleId;
		this.filterType = ruleFilter.filterType;
		this.ruleType = ruleFilter.ruleType;
		this.filterData0 = ruleFilter.filterData0;
		this.filterData1 = ruleFilter.filterData1;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(": FilterType " + filterType.toString());
		sb.append(": RuleType " + ruleType.toString());
		sb.append(": FilterData: " + filterData0 + " through " + filterData1);
		return sb.toString();
	}

	public long getFilterId() {
		return filterId;
	}

	public void setFilterId(long filterId) {
		this.filterId = filterId;
	}

	public long getRuleId() {
		return ruleId;
	}

	public void setRuleId(long ruleId) {
		this.ruleId = ruleId;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}

	public String getFilterData0() {
		return filterData0;
	}

	public void setFilterData0(String filterData0) {
		this.filterData0 = filterData0;
	}

	public String getFilterData1() {
		return filterData1;
	}

	public void setFilterData1(String filterData1) {
		this.filterData1 = filterData1;
	}

}

package com.jnrcorp.ems.sqllite.model;

public class RuleTimeDay implements Comparable<RuleTimeDay> {

	private long dayId;
	private long timeId;
	private Day day;

	public RuleTimeDay() {
		super();
	}

	@Override
	public int compareTo(RuleTimeDay another) {
		Integer myEnumDayId = Integer.valueOf(day.getId());
		Integer theirEnumDayId = Integer.valueOf(another.day.getId());
		return myEnumDayId.compareTo(theirEnumDayId);
	}

	public long getDayId() {
		return dayId;
	}

	public void setDayId(long dayId) {
		this.dayId = dayId;
	}

	public long getTimeId() {
		return timeId;
	}

	public void setTimeId(long timeId) {
		this.timeId = timeId;
	}

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

}

package com.jnrcorp.ems.sqllite.model;

import java.util.Calendar;

import com.jnrcorp.ems.R;

public enum Day {

	SUNDAY(0, R.string.sunday_long, R.string.sunday_medium, Calendar.SUNDAY),
	MONDAY(1, R.string.monday_long, R.string.monday_medium, Calendar.MONDAY),
	TUESDAY(2, R.string.tuesday_long, R.string.tuesday_medium, Calendar.TUESDAY),
	WEDNESDAY(3, R.string.wednesday_long, R.string.wednesday_medium, Calendar.WEDNESDAY),
	THURSDAY(4, R.string.thursday_long, R.string.thursday_medium, Calendar.THURSDAY),
	FRIDAY(5, R.string.friday_long, R.string.friday_medium, Calendar.FRIDAY),
	SATURDAY(6, R.string.saturday_long, R.string.saturday_medium, Calendar.SATURDAY);

	private int id;
	private int longResource;
	private int mediumResource;
	private int calendarDay;

	private Day(int id, int longResource, int mediumResource, int calendarDay) {
		this.id = id;
		this.longResource = longResource;
		this.mediumResource = mediumResource;
		this.calendarDay = calendarDay;
	}

	public int getId() {
		return id;
	}

	public int getLongResource() {
		return longResource;
	}

	public int getMediumResource() {
		return mediumResource;
	}

	public int getCalendarDay() {
		return calendarDay;
	}

}

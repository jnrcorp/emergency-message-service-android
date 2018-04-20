package com.jnrcorp.ems.util;

import java.util.Calendar;
import java.util.Date;

import android.widget.TimePicker;

public class TimePickerUtil {

	private TimePickerUtil() {
		super();
	}

	public static Date getTime(TimePicker timePicker) {
		int hour = timePicker.getCurrentHour();
		int minute = timePicker.getCurrentMinute();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static void setTime(TimePicker timePicker, Date date) {
		int hour = 8;
		int minute = 0;
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			hour = cal.get(Calendar.HOUR_OF_DAY);
			minute = cal.get(Calendar.MINUTE);
		}
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
	}

}

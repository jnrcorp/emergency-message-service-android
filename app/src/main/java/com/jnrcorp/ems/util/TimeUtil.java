package com.jnrcorp.ems.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

import com.jnrcorp.ems.constant.Constants;

public class TimeUtil {

	private static final String TIME_FORMAT_WIDGET = "HH:mm:ss";
	private static final String TIME_FORMAT_DISPLAY = "hh:mm a";

	private TimeUtil() {
		super();
	}

	public static Date getTime24Hour(String input) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_WIDGET, Locale.getDefault());
		Date output = null;
		try {
			output = dateFormat.parse(input);
		} catch (ParseException e) {
			e.printStackTrace();
			Log.e(Constants.LOG_TAG, "Exception while parsing: " + e.getMessage());
		}
		return output;
	}

	public static String getTime24Hour(Date input) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_WIDGET, Locale.getDefault());
		return dateFormat.format(input);
	}

	public static String getTimeForDisplay(Date input) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT_DISPLAY, Locale.getDefault());
		return dateFormat.format(input);
	}

	public static boolean areEqual(Date timeStart, Date timeEnd) {
		Calendar timeStartParts = convertToCalendar(timeStart);
		Calendar timeEndParts = convertToCalendar(timeEnd);
		int hourStart = timeStartParts.get(Calendar.HOUR_OF_DAY);
		int hourEnd = timeEndParts.get(Calendar.HOUR_OF_DAY);
		int minStart = timeStartParts.get(Calendar.MINUTE);
		int minEnd = timeEndParts.get(Calendar.MINUTE);
		boolean hoursSame = hourStart == hourEnd;
		boolean minsSame = minStart == minEnd;
		return hoursSame && minsSame;
	}

	public static Calendar convertToCalendar(Date date) {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTime(date);
		return cal;
	}

}

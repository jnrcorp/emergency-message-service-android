package com.jnrcorp.ems.util;

public class NumberUtil {

	private NumberUtil() {
		super();
	}

	public static Double convertToDouble(String input) {
		Double result = null;
		try {
			if (!StringUtil.isEmpty(input)) {
				result = Double.valueOf(input);
			}
		} catch (Exception ex) {
			// ignore failures
		}
		return result;
	}

	public static Integer convertToInteger(String input) {
		Integer result = null;
		try {
			if (!StringUtil.isEmpty(input)) {
				result = Integer.valueOf(input);
			}
		} catch (Exception ex) {
			// ignore failures
		}
		return result;
	}

	public static boolean isNumeric(String input) {
		return convertToInteger(input) != null;
	}

}

package com.jnrcorp.ems.util;

public class StringUtil {

	private StringUtil() {
		super();
	}

	public static boolean isNotEmpty(String input) {
		return !isEmpty(input);
	}

	public static boolean isEmpty(String input) {
		return input == null || input.trim().equals("null") || input.trim().equals("");
	}

}

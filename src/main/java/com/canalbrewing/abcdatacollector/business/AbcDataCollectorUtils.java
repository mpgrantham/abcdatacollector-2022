package com.canalbrewing.abcdatacollector.business;

public class AbcDataCollectorUtils {

	private AbcDataCollectorUtils() {
	}

	public static boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}

	public static int atoi(String value) {
		if (value == null || value.trim().length() == 0) {
			return 0;
		}

		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
			return 0;
		}
	}

}
package com.acerete.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtils {
	
	/**
	 * Utils to deal with dates in ISO-8601 format
	 */
	
	private final static String TIME_ZONE = "GMT";
	private final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	
	// Thread safe
	private static final ThreadLocal<DateFormat> ISO_8601_DATE_FORMAT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
			return sdf;
		}
	};

	public static Date parseIso8601Date(String dateString) throws ParseException {
		return ISO_8601_DATE_FORMAT.get().parse(dateString);
	}

	public static String formatIso8601Date(Date date) {
		return ISO_8601_DATE_FORMAT.get().format(date);
	}
}

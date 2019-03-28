package com.scosyf.learn.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TimeTest {

	public static final long MILLISECOND_YEAR = 365 * 1000L * 60L * 60L * 24L;
	
	public static final String TIME_PATTERN   = "yyyyMMdd";
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
		System.out.println(getYeas(sdf.parse("20180103"), sdf.parse("20180104")));
	}
	
	public static int getYeas(Date start, Date end) {
		if (start.compareTo(end) >= 0) {
			return 0;
		}
		long st = start.getTime();
		long et = end.getTime();
		return (int) ((et - st) / MILLISECOND_YEAR);
	}
}

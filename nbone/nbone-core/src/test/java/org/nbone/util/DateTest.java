package org.nbone.util;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;
import org.nbone.constants.DateConstant;
import org.nbone.util.DateFPUtils;

public class DateTest {
	//   
	public static String strDateTime  = " 2016-01-07 14:57:29";

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		String[] ss= {DateConstant.DEFAULT_DATETIME_MM_PATTERN,DateConstant.DEFAULT_DATETIME_PATTERN,DateConstant.DEFAULT_DATE_PATTERN};
		Date date = DateUtils.parseDate(strDateTime,ss);
		System.out.println(date);
		
		Date date1 = DateFPUtils.parseDate(strDateTime,ss, Locale.getDefault(), false);
		System.out.println(date1);
		
	}

}

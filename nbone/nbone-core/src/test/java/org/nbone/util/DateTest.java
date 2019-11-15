package org.nbone.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.nbone.constants.DateConstant;
import org.nbone.util.DateFPUtils;

import static org.nbone.constants.DateConstant.DEFAULT_DATETIME_PATTERN;

public class DateTest {
	//   
	public static String strDateTime  = " 2016-01-07 14:57:29";

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		String[] ss= {DateConstant.DEFAULT_DATETIME_MM_PATTERN, DEFAULT_DATETIME_PATTERN,DateConstant.DEFAULT_DATE_PATTERN};
		Date date = DateUtils.parseDate(strDateTime,ss);
		System.out.println(date);
		
		Date date1 = DateFPUtils.parseDate(strDateTime,ss, Locale.getDefault(), false);
		System.out.println(date1);
		
	}


	@Test
	public void dateTest(){
		String month = "2009.07.11";
		String pattern = "yyyy-MM-dd";
		String pattern1 = "yyyy-MM-dd hh:mm:ss";
		String dbDate = "2013-01-20";

		String str11 = "2014-06-09 16:00:00";
		String str22 = "2013-09-05 04:30:00";

		System.out.println(DateUtil.getDatetimeGap(DateUtil.parseDate(str11, DEFAULT_DATETIME_PATTERN),DateUtil.getCurrentDate(), Calendar.MINUTE));

		System.out.println("======================");
		System.out.println(new Timestamp(DateUtil.addDays(DateUtil.getCurrentDate(), 1).getTime()));

		Date end  = DateUtil.addSeconds(DateUtil.getCurrentDate(), 60);
		System.out.println(DateUtil.getDiffDatetime(new Date(), end, TimeUnit.MINUTES));
		System.out.println(DateUtil.getDatetimeGap(new Date(), end, Calendar.MINUTE));

		System.out.println((int)1.2);
		DateUtil.globalFormat.applyPattern(DateUtil.DEFAULT_DATE_PATTERN);
		DateUtil.globalFormat.applyPattern(DEFAULT_DATETIME_PATTERN);
		System.out.println(DateUtil.formatDate(DateUtil.getSystemTimestamp(),"  "));
	}

	@Test
	public  void dateDFTest(String[] args) throws ParseException {
		System.out.println(new Date(0L));
		System.out.println(new Timestamp(0L));
		System.out.println("iii" + DateFPUtils.parseDateByLongString("3"));

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		String longtime = "1452232280151";
		// Date date = dateFormat.parse(longtime);
		System.out.println("jjjjjjjjjjj==" + dateFormat.format(new Date()));
		org.apache.commons.lang3.time.DateUtils.parseDate(longtime,DateConstant.DEFAULT_FORMATS);
		System.out.println(System.currentTimeMillis());
		System.out.println(1452232280151L);
	}

}

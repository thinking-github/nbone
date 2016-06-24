package org.nbone.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonProcessingException;
import org.nbone.constants.DateConstant;

/**
 * DateFormat and DateParse
 * 
 * @see DateFormatUtils
 * @see DateUtils
 * @see Locale
 * @see Calendar#setLenient(boolean)
 * @author thinking
 * @see 2015-08-08
 */
public class DateFPUtils implements DateConstant {
	private static Log logger = LogFactory.getLog(DateFPUtils.class);
	protected static SimpleDateFormat globalFormat = new SimpleDateFormat();

	/**
	 * 
	 * @param longtimeStr
	 * @return parse Date Exception return null
	 */
	public static Date parseDateByLongString(String longtimeStr) {
		if(StringUtils.isBlank(longtimeStr)){
			return null;
		}
		try {
			// Date#getTime()
			// 1452223274050
			long longtime = Long.valueOf(longtimeStr);
			return new Date(longtime);
		} catch (NumberFormatException e2) {
			if (logger.isErrorEnabled()) {
				logger.error(e2);
			}
		}
		return null;
	}

	// ---------------------------------------------------------------
	// parse Date
	// ---------------------------------------------------------------
	/**
	 * 将一个字符串转换成指定格式的日期,按照匹配模式进行 匹配,无法匹配是采用第二种方式
	 * 
	 * @param strDateTime
	 *            需要转换的日期字符串
	 * @param formats
	 *            转换的格式数组，包含多个转换格式，其中任何一种匹配都可以转换成功
	 * @return 当转换成功返回转换成功后的日期，否则返回 null
	 * @see DateUtils#parseDate(String, String[])
	 * @see Date#getTime()
	 * @author thinking
	 */
	public static Date parseDate(String strDateTime, String[] formats) {
		if(StringUtils.isBlank(strDateTime)){
			return null;
		}
		try {
			Date parseDate = DateUtils.parseDate(strDateTime, formats);
			return parseDate;
		} catch (ParseException e) {
			if (logger.isWarnEnabled()) {
				logger.warn(e);
			}
			// 第二种方式
			// 1452223274050
			return parseDateByLongString(strDateTime);

		}
	}

	/**
	 * 
	 * 将时间字符串转换成默认格式的日期(多种模式匹配)
	 * 
	 * @author thinking
	 * @see #parseDate(String, String[])
	 * @see #DEFAULT_FORMATS
	 */
	public static Date parseDateMultiplePattern(String strDateTime) {
		return parseDate(strDateTime,DEFAULT_FORMATS);
	}
	
	/**
	 * 
	 * 将时间字符串转换成默认格式的日期
	 * 
	 * @author thinking
	 * @see #parseDate(String, String[])
	 */
	public static Date parseDate(String strDateTime) {
		return parseDate(strDateTime, new String[] { DEFAULT_DATE_PATTERN });
	}
	
	/**
	 * 
	 * 将时间字符串转换成默认格式的日期
	 * 
	 * @author thinking
	 * @see #parseDate(String, String[])
	 */
	public static Date parseFullDate(String strDateTime) {
		return parseDate(strDateTime, new String[] { DEFAULT_DATETIME_PATTERN });
	}
	
	

	/**
	 * @author thinking
	 * @see #parseDate(String, String[])
	 */
	public static Date parseDate(String strDateTime, String pattern) {
		return parseDate(strDateTime, new String[] { pattern });
	}

	/**
	 * @author thinking
	 * @see #parseTimestamp(String, String[])
	 */
	public static Timestamp parseTimestamp(String strDateTime, String pattern) {
		return parseTimestamp(strDateTime, new String[] { pattern });
	}

	/**
	 * 
	 * 将一个字符串转换成指定格式的Timestamp
	 * 
	 * @author thinking
	 * @see #parseDate(String, String[])
	 */
	public static Timestamp parseTimestamp(String strDateTime, String[] patterns) {
		Date parseDate = parseDate(strDateTime, patterns);
		return new Timestamp(parseDate.getTime());
	}

	// ---------------------------------------------------------------
	// format Date
	// ---------------------------------------------------------------

	/**
	 * 将一个指定的日期转换为默认的格式
	 * 
	 * @author thinking
	 * @see #formatDate(Date, String)
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return formatDate(date, DEFAULT_DATETIME_PATTERN);
	}

	/**
	 * 将一个指定的日期格式化成指定的格式
	 * 
	 * @author thinking
	 */
	public static String formatDate(Date date, String pattern) {
		if (pattern == null || "".equals(pattern.trim())) {
			pattern = DEFAULT_DATETIME_PATTERN;
		}
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 
	 * 将一个指定的日期格式化成指定时区，指定 Locale 的指定的格式
	 * 
	 * @param date
	 *            指定的日期
	 * @param pattern
	 *            指定的格式
	 * @param timezone
	 *            指定的时区
	 * @param locale
	 *            指定的 Locale
	 * @return 格式化好后的日期字符串
	 * @author thinking
	 */
	public static String formatDate(Date date, String pattern,
			TimeZone timezone, Locale locale) {
		return DateFormatUtils.format(date, pattern, timezone, locale);
	}

	/**
	 * Base By JSONLIB <br>
	 * DateTime String to Date
	 * 
	 * @param strDateTime
	 * @param formats
	 * @param locale
	 * @param lenient
	 * @return
	 */
	public static Date parseDate(String strDateTime, String[] formats,
			Locale locale, boolean lenient) {
		int count = 0;
		SimpleDateFormat dateFormat = null;
		int length = formats.length;
		for (int i = 0; i < length; i++) {
			if (dateFormat == null)
				dateFormat = new SimpleDateFormat(formats[i], locale);
			else {
				dateFormat.applyPattern(formats[i]);
			}
			dateFormat.setLenient(lenient);
			try {
				String temp = strDateTime.toLowerCase();
				Date parseDate = dateFormat.parse(temp);
				return parseDate;
			} catch (ParseException localParseException) {
				count++;
			}
		}
		if (count == length) {
			StringBuilder stringBuilder = new StringBuilder("[" + strDateTime
					+ "]");
			stringBuilder.append(" not match of date format [");
			for (int i = 0; i < length; i++) {
				stringBuilder.append(formats[i] + ",");
			}
			stringBuilder.append("]");
			logger.warn(stringBuilder);
		}
		// 第二种方式
		// 1452223274050
		return parseDateByLongString(strDateTime);
	}

	
	/**
	 * 根据时间获得时间所属季度
	 * @param date
	 * @return
	 */
	public final static String formatDateToJidu(Date date) {
		if (date == null) {
			return "";
		}
		String nowDate =formatDate(date, DEFAULT_DATE_PATTERN);
		String months = nowDate.substring(5, 7);
		String jidu = "";
		if(months.equals("01")||months.equals("02")||months.equals("03")){
			jidu = "1";
		}else if(months.equals("04")||months.equals("05")||months.equals("06")){
			jidu = "2";
		}else if(months.equals("07")||months.equals("08")||months.equals("09")){
			jidu = "3";
		}else if(months.equals("10")||months.equals("11")||months.equals("12")){
			jidu = "4";
		}
		return jidu;
	}
	
	public final static String formatDateToNiandu(Date date){
		String nowDate =formatDate(date, DEFAULT_DATE_PATTERN);
		return nowDate.substring(0,4);
	}
	
	/**
	 * Base by Uap
	 * 
	 * @param value
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	@Deprecated
	public static Date parseDate1(String value) {
		if ((value == null) || (value.equals("")))
			return null;
		DateFormat df = null;
		int ymdLength = value.split("-").length;
		int hmsLength = value.split(":").length;
		if ((ymdLength == 2) && (hmsLength == 1))
			df = new SimpleDateFormat("yyyy-MM");
		else if ((ymdLength == 3) && (hmsLength == 1))
			df = new SimpleDateFormat("yyyy-MM-dd");
		else if ((ymdLength == 3) && (hmsLength == 2))
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		else if ((ymdLength == 3) && (hmsLength == 3))
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (df != null)
				return df.parse(value);
		} catch (ParseException e) {
			if (logger.isErrorEnabled())
				logger.error(e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(new Date(0L));
		System.out.println(new Timestamp(0L));
		System.out.println("iii" + parseDateByLongString("3"));

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
		String longtime = "1452232280151";
		// Date date = dateFormat.parse(longtime);
		System.out.println("jjjjjjjjjjj==" + dateFormat.format(new Date()));
		 DateUtils.parseDate(longtime,DateConstant.DEFAULT_FORMATS);
		System.out.println(System.currentTimeMillis());
		System.out.println(1452232280151L);
	}

}

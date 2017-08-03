/**
 * Copyright 2012-2013 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nbone.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.nbone.constants.DateConstant;
import org.springframework.util.Assert;

/**
 * 
 * 日期处理以及格式化和反格式化
 * <p/>
 * 
 * @author thinking
 * @version Date: 2014-06-09
 * @serial 1.0
 * @see DateUtils
 */
public class DateUtil extends DateFPUtils  implements DateConstant{

	/**
	 * 
	 * 将 Date 转换为 Timestamp
	 * @param date 需要转换的 Date
	 * @author thinking     
	 */
	public static Timestamp toTimestamp(Date date) {
		Assert.notNull(date);
		return new Timestamp(date.getTime());
	}
	
	/**
	 * 判断两个日期是否相等 具体到 day
	 * 
	 * @return 相等时返回 true
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		return DateUtils.isSameDay(date1, date2);
	}

	/**
	 * 判断两个时间是否相等 具体到 毫秒
	 * @return 相等时返回 true
	 */
	public static boolean isSameTime(Date date1, Date date2) {
		return DateUtils.isSameInstant(date1, date2);
	}

    //***********************************************************************
	/**
	 * 根据年月日获得指定的日期
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param day
	 *            日期
	 * @return 相应的 Date 型日期
	 */
	public static Date getDate(int year, int month, int day) {
		Assert.isTrue(month > 0 && month < 13);
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, convertRealMonthToCalendarMonth(month), day, 0, 0, 0);
		return calendar.getTime();
	}

	/**
	 * 根据给定的年月日小时分钟秒创建一个日期
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @param day
	 *            日期
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 * @return 返回一个对应的日期
	 */
	public static Date getDateTime(int year, int month, int day, int hour, int minute, int second) {
		Assert.isTrue(month > 0 && month <= 12);
		Assert.isTrue(hour >= 0 && hour <= 23);
		Assert.isTrue(minute >= 0 && minute <= 60);
		Assert.isTrue(second >= 0 && second <= 60);
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, convertRealMonthToCalendarMonth(month), day, hour, minute, second);
		return calendar.getTime();
	}

	/**
	 * 判断给定日期是否为当月的最后一天
	 * 
	 * @param date
	 *            指定的日期
	 * @return 当该日期为当月最后一天则返回 true
	 */
	public static boolean isEndOfTheMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return calendar.get(Calendar.DATE) == maxDay;
	}

	/**
	 * 判断给定日期是否为当年的最后一天
	 * 
	 * @param date
	 *            指定的日期
	 * @return 当该日期为当年最后一天则返回 true
	 */
	public static boolean isEndOfTheYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return (11 == calendar.get(Calendar.MONTH)) && (31 == calendar.get(Calendar.DATE));
	}

	/**
	 * 获得给定日期的月份的最后一天
	 * 
	 * @param date
	 *            指定的日期
	 * @return 指定日期月份的最后一天
	 */
	public static int getLastDayOfTheMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 判断开始日期是否比结束日期早
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 当开始日期比结束日期早时返回 true
	 */
	public static boolean isStartBeforeEndDate(Date startDate, Date endDate) {
		Assert.notNull(startDate, "StartDate is null");
		Assert.notNull(endDate, "EndDate is null");
		return resetTime(startDate).compareTo(resetTime(endDate)) < 0;
	}

	/**
	 * 判断开始日期是否比结束日期早
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 当开始时间比结束时间早时返回 true
	 */
	public static boolean isStartBeforeEndTime(Date startTime, Date endTime) {
		Assert.notNull(startTime, "StartTime is null");
		Assert.notNull(endTime, "EndTime is null");
		return startTime.getTime() <= endTime.getTime();
	}

	/**
	 * 判断给定日期是否为对应日期月份的第一天
	 * 
	 * @param date
	 *            给定日期
	 * @return 当给定日期是否为对应日期月份的第一天返回 true
	 */
	public static boolean isStartOfTheMonth(Date date) {
		Assert.notNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return 1 == calendar.get(Calendar.DATE);
	}

	/**
	 * 判断给定日期是否为对应日期年份的第一天
	 * 
	 * @param date
	 *            给定日期
	 * @return 当给定日期是否为对应日期年份的第一天返回 true
	 */
	public static boolean isStartOfTheYear(Date date) {
		Assert.notNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return (0 == calendar.get(Calendar.MONTH)) && (1 == calendar.get(Calendar.DATE));
	}

	/**
	 * 获取给定日期的月份
	 * 
	 * @param date
	 *            给定日期
	 * @return 给定日期的月份
	 */
	public static int getMonth(Date date) {
		Assert.notNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return convertCalendarMonthToRealMonth(calendar.get(Calendar.MONTH));
	}

	/**
	 * 获取给定日期的年份
	 * 
	 * @param date
	 *            给定日期
	 * @return 给定日期的年份
	 */
	public static int getYear(Date date) {
		Assert.notNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获取给定日期的是该周的第几天
	 * 
	 * @param date
	 *            给定日期
	 * @return 给定日期的是该周的第几天
	 */
	public static int getDayOfWeek(Date date) {
		Assert.notNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 获取给定日期是该月的第几天
	 * 
	 * @param date
	 *            给定日期
	 * @return 给定日期是该月的第几天
	 */
	public static int getDayOfMonth(Date date) {
		Assert.notNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取给定日期是当年的第几天
	 * 
	 * @param date
	 *            给定日期
	 * @return 给定日期是当年的第几天
	 */
	public static int getDayOfYear(Date date) {
		Assert.notNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取给定日期是当天的几点
	 * 
	 * @param date
	 *            给定日期
	 * @return 给定日期是当天的几点
	 */
	public static int getHour(Date date) {
		Assert.notNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取给定日期的分钟
	 * 
	 * @param date
	 *            给定日期
	 * @return 给定日期的分钟
	 */
	public static int getMinute(Date date) {
		Assert.notNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 获取给定日期的秒数
	 * 
	 * @param date
	 *            给定日期
	 * @return 给定日期的秒数
	 */
	public static int getSecond(Date date) {
		Assert.notNull(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 获取系统的 Timestamp
	 * 
	 * @return 系统当前时间的 Timestamp
	 */
	public static Timestamp getSystemTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 获取当前系统日期（只包含日期,不包含时间）
	 * 
	 * @return 系统当前日期，不含小时分钟秒
	 */
	public static Date getSystemDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Date(calendar.getTime().getTime());
	}
	
	/**
	 * 获取当前系统日期
	 * @return  Date
	 */
	public static Date getCurrentDate() {
		return new Date();
	}
	/**
	 * @author thinking 
	 * @return
	 */
	public static String getCurrentTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}
	/**
	 * @author thinking 
	 * @return
	 */
	public static String getCurrentTimeForms() {
		return getCurrentTime(DIRECT_LONG_PATTERN);
	}
	/**
	 * @author thinking 
	 * @return
	 */
	public static String getCurrentTimeFors() {
		return getCurrentTime(DIRECT_DATETIME_PATTERN);
	}
	
	/**
	 * 获得当前日期 yyyyMMdd格式的日期
	 * @author thinking 
	 * @return
	 */
	public static String getCurrentDate_YYYYMMDD() {
		return getCurrentTime(DIRECT_DATE_PATTERN);
	}
	
	/**
	 * 判断字符串日期是否有效
	 * @param dateStr 日期字符
	 * @param pattern 匹配字符
	 * @return
	 * @author thinking
	 */
	public static boolean isValidDate(String dateStr,String pattern){
		if(dateStr == null || "".equals(dateStr.trim()))
	     return false;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
        	format.setLenient(false);
			format.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
		
	}
	
	/**
	 * @author thinking 
	 * @return
	 */
	public static String timeMillisForSid16() {
		//目前时间戳为13位
		String sid = System.currentTimeMillis() + "";
		int len = sid.length() - 16;
		if (len > 0) {
			sid = sid.substring(len, len + 16);
		} else if (len < 0) {
			for (int i = 0; i < Math.abs(len); i++) {
				sid = "0" + sid;
			}
		}
		return sid;
	}

	/**
	 * 给指定日期加几秒
	 * 
	 * @param date
	 *            指定的日期
	 * @param numSeconds
	 *            需要往后加的秒数
	 * @return 加好后的日期
	 */
	public static Date addSeconds(Date date, int numSeconds) {
		Assert.notNull(date);
		return DateUtils.addSeconds(date, numSeconds);
	}

	/**
	 * 给指定日期加几天
	 * 
	 * @param date
	 *            指定的日期
	 * @param numDays
	 *            需要往后加的天数
	 * @return 加好后的日期
	 */
	public static Date addDays(Date date, int numDays) {
		Assert.notNull(date);
		return DateUtils.addDays(date, numDays);
	}

	/**
	 * 给指定日期加几个小时
	 * 
	 * @param date
	 *            指定的日期
	 * @param numHours
	 *            需要往后加的小时数
	 * @return 加好后的日期
	 */
	public static Date addHours(Date date, int numHours) {
		Assert.notNull(date);
		return DateUtils.addHours(date, numHours);
	}

	/**
	 * 给指定日期加几分钟
	 * 
	 * @param date
	 *            指定的日期
	 * @param numMins
	 *            需要往后加的分钟数
	 * @return 加好后的日期
	 */
	public static Date addMinutes(Date date, int numMins) {
		Assert.notNull(date);
		return DateUtils.addMinutes(date, numMins);
	}

	/**
	 * 给指定日期加几个月
	 * 
	 * @param date
	 *            指定的日期
	 * @param numMonths
	 *            需要往后加的月数
	 * @return 加好后的日期
	 */
	public static Date addMonths(Date date, int numMonths) {
		Assert.notNull(date);
		return DateUtils.addMonths(date, numMonths);
	}

	/**
	 * 给指定日期加几年
	 * 
	 * @param date
	 *            指定的日期
	 * @param numYears
	 *            需要往后加的年数
	 * @return 加好后的日期
	 */
	public static Date addYears(Date date, int numYears) {
		Assert.notNull(date);
		return DateUtils.addYears(date, numYears);
	}
	
	/**
	 * 将指定日期的小时、分钟、秒清零
	 * 
	 * @param date
	 *            指定的日期
	 * @return 小时、分钟、秒被清零的日期
	 */
	public static Date resetTime(Date date) {
		Assert.notNull(date);
		return getDate(getYear(date), getMonth(date), getDayOfMonth(date));
	}

	/**
	 * 因为 Calendar 的月月份比实际月份小一个，所以我们需要将实际月份减一才可以将实际月份转换成 Calendar 的月份
	 * 
	 * @param realMonth
	 * @return
	 */
	private static int convertRealMonthToCalendarMonth(int realMonth) {
		return realMonth - 1;
	}

	/**
	 * 因为 Calendar 的月月份比实际月份小一个，所以我们需要将 Calendar 月份加一才可以将 Calendar 的月份转换成实际月份
	 * 
	 * @param calendarMonth
	 * @return
	 */
	private static int convertCalendarMonthToRealMonth(int calendarMonth) {
		return calendarMonth + 1;
	}

	public static Date getFirstDayOfMonthForDB(Date today) {
		int year = getYear(today);
		int month = getMonth(today);
		if (getDayOfMonth(today) == 1) {
			month--;
		}
		// 处理跨年的情况
		if (month == 0) {
			year--;
			month = 12;
		}
		return getDate(year, month, 1);
	}

	public static Date getLastDayOfMonthForDB(Date today) {
		int year = getYear(today);
		int month = getMonth(today);
		if (getDayOfMonth(today) != 1) {
			month++;
		}
		// 处理 12 月问题
		if (month == 13) {
			month = 1;
			year++;
		}
		return getDate(year, month, 1);
	}

	public static Date getFirstDayOfQuarterForDB(Date today) {
		// 123 456 789 10 11 12
		int year = getYear(today);
		int month = getMonth(today);
		int day = getDayOfMonth(today);
		if (day == 1) {
			if ((month == 4 || month == 7 || month == 10)) {
				month -= 3;
			} else if (month == 1) {
				month = 10;
				year--;
			}
		}
		if (month >= 1 && month <= 3) {
			month = 1;
		} else if (month >= 4 && month <= 6) {
			month = 4;
		} else if (month >= 7 && month <= 9) {
			month = 7;
		} else if (month >= 10 && month <= 12) {
			month = 10;
		}

		return getDate(year, month, 1);
	}

	public static Date getLastDayOfQuarterForDB(Date today) {
		// 123 456 789 10 11 12
		int year = getYear(today);
		int month = getMonth(today);
		int day = getDayOfMonth(today);
		if (!(month == 1 || month == 4 || month == 7 || month == 10)) {
			if (month >= 1 && month <= 3) {
				month = 4;
			} else if (month >= 4 && month <= 6) {
				month = 7;
			} else if (month >= 7 && month <= 9) {
				month = 10;
			} else if (month >= 10 && month <= 12) {
				month = 1;
				year++;
			}
		}
		return getDate(year, month, 1);
	}

	/**
	 * 取得时间与当前时间的差距,格式是N小时N分钟前
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String getDiffFromNow(Date date) {
		Date nowDate = new Date();
		Long nowLong = nowDate.getTime() / 1000;
		Long dateLong = date.getTime() / 1000;
		String returnValue = "";
		Long diffLong = nowLong - dateLong;
		Long secondLong = diffLong / 60L;
		if (secondLong == 0) {
			returnValue = "不到1分钟前";
		} else if (secondLong < 60) {
			returnValue = secondLong + "分钟前";
		} else {
			Long hourLong = secondLong / 60L;
			secondLong = secondLong - 60L * hourLong;
			returnValue = hourLong + "小时" + secondLong + "分钟前";
		}
		return returnValue;
	}


	/**
	 * 通过字符串获取某月第一天日期Date
	 * 
	 * @param month
	 *            月份字符串
	 * @param pattern
	 *            月份字符串格式，为空时默认为"yyyy/MM/dd"
	 * @return 返回该月份第一天日期
	 */
	public static Date getFirstDateOfTheMonth(String month, String pattern) {
		Date thisDate;
		if ("".equals(pattern)) {
			thisDate = parseDate(month);
		} else {
			thisDate = parseDate(month, pattern);
		}
		Assert.notNull(thisDate);
		if (isStartOfTheMonth(thisDate)) {
			return thisDate;
		} else {
			int thisYear = getYear(thisDate);
			int thisMonth = getMonth(thisDate);
			int thisDay = 1;
			return getDate(thisYear, thisMonth, thisDay);
		}
	}

	/**
	 * 通过一个日期获取某月第一天日期Date
	 * 
	 * @param date
	 *            月份中某一天
	 * @return 返回该月份第一天日期
	 */
	public static Date getFirstDateOfTheMonth(Date date) {

		Assert.notNull(date);
		if (isStartOfTheMonth(date)) {
			return date;
		} else {
			int thisYear = getYear(date);
			int thisMonth = getMonth(date);
			int thisDay = 1;
			return getDate(thisYear, thisMonth, thisDay);
		}
	}

	/**
	 * 通过字符串获取某月最后一天日期Date
	 * 
	 * @param month
	 *            月份字符串
	 * @param pattern
	 *            月份字符串格式，为空时默认为"yyyy/MM/dd"
	 * @return 返回该月份最后一天日期
	 */
	public static Date getLastDateOfTheMonth(String month, String pattern) {
		Date firstDateOfTheMonth = getFirstDateOfTheMonth(month, pattern);
		Assert.notNull(firstDateOfTheMonth);
		int daysOfTheMonth = getLastDayOfTheMonth(firstDateOfTheMonth) - 1;
		Date lastDateOfTheMonth = addDays(firstDateOfTheMonth, daysOfTheMonth);
		return lastDateOfTheMonth;
	}

	/**
	 * 通过日期获取某月最后一天日期Date
	 * 
	 * @param date
	 *            月份中某一天
	 * @return 返回该月份最后一天日期
	 */
	public static Date getLastDateOfTheMonth(Date date) {
		Date firstDateOfTheMonth = getFirstDateOfTheMonth(date);
		Assert.notNull(firstDateOfTheMonth);
		int daysOfTheMonth = getLastDayOfTheMonth(firstDateOfTheMonth) - 1;
		Date lastDateOfTheMonth = addDays(firstDateOfTheMonth, daysOfTheMonth);
		return lastDateOfTheMonth;
	}

	/**
	 * 查询当前年份.
	 * 
	 * @return int
	 */
	public static int getYear() {
		Calendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 查询当前月份.
	 * 
	 * @return int
	 */
	public static int getMonth() {
		Calendar calendar = new GregorianCalendar();
		int month = calendar.get(Calendar.MONTH) + 1;
		return month;
	}

	/**
	 * 查询当前天数（按月）.
	 * 
	 * @return int
	 */
	public static int getDay() {
		Calendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return day;
	}


	public static Date getDateAfterSeconds(int seconds) {
		long now = System.currentTimeMillis() + seconds * 1000;
		return new Date(now);
	}
	
	
	/**
	 * 判断两个日期的时间差
	 * 此方法只能整除(功能不完善) 
	 * @param startDatetime
	 * @param endDatetime
	 * @param timeUnit
	 * @return
	 */
	public static double getDiffDatetime(Date startDatetime, Date endDatetime,TimeUnit timeUnit){
		long start  = startDatetime.getTime();
		long end  = endDatetime.getTime();
		long apple =  end - start;
		double result  = apple;
		if(TimeUnit.DAYS  == timeUnit) {
			result = TimeUnit.MILLISECONDS.toDays(apple);
		}
		else if(TimeUnit.HOURS  == timeUnit) {
			result = TimeUnit.MILLISECONDS.toHours(apple);
		}
		else if(TimeUnit.MINUTES  == timeUnit) {
			result = TimeUnit.MILLISECONDS.toMinutes(apple);
		}
		else if(TimeUnit.SECONDS  == timeUnit) {
			result = TimeUnit.MILLISECONDS.toSeconds(apple);
		}
		else if(TimeUnit.MILLISECONDS  == timeUnit) {
			result = TimeUnit.MILLISECONDS.toMillis(apple);
		}
		else {
			result = apple;
		}
		return result;
	}
	
	//************************************************************************************************
	//更新方法 2014-08-01
	/**
	 *  获取两个时间的时间间隔
	 * @param startDatetime 开始时间
	 * @param endDatetime   结束时间
	 * @param dateType      时间类型  可为null  当dateType为null时 返回的时间差为毫秒级
	 * @return  时间差    
	 *        
	 */
	public static double getDatetimeGap(Date startDatetime, Date endDatetime,Integer dateType) {
		Assert.notNull(startDatetime, "startDatetime not be null");
		Assert.notNull(startDatetime, "endDatetime not be null");
		
		long apple = endDatetime.getTime() - startDatetime.getTime();
		if(dateType == null){
			
			return apple;
		}
		// YEAR =1
		if(dateType == Calendar.YEAR){
			
			return apple/(double)(1000 * 60 * 60 * 24 * 12 * 365);
		}
		//MONTH = 2
		else if(dateType == Calendar.MONTH){
			
			return apple/(double)(1000 * 60 * 60 * 24 * 12);
		}
		//DAY_OF_YEAR =6
		else if(dateType == Calendar.DAY_OF_YEAR){
				
			return apple/(double)(1000 * 60 * 60 * 24);
		}
		//HOUR =10
		else if(dateType == Calendar.HOUR){
			
			return apple/(double)(1000 * 60 * 60);
		}
		//MINUTE =12
		else if(dateType == Calendar.MINUTE){
			
			return apple/(double)(1000 * 60);
		}
		// SECOND = 13
		else if(dateType == Calendar.SECOND){
			
			return apple/1000L; 
		}
		// MILLISECOND = 14
		else if(dateType == Calendar.MILLISECOND){
			
			return apple;
		}
		
		return apple;
	}
	
	/**
	 * 将yyyy-MM-dd HH:mm:ss 转化成 yyyyMMddHHmmss
	 * @param s
	 * @return
	 */
	public static String getDataString(String s) {
		String y = s.substring(0, 4);
		String m = s.substring(5, 7);
		String d = s.substring(8, 10);
		String h = s.substring(11, 13);
		String mi = s.substring(14, 16);
		String se = s.substring(17, 19);
		return (new StringBuilder(String.valueOf(y))).append(m).append(d)
				.append(h).append(mi).append(se).toString();
	}

	public static Date getFristDayOfThisMonth(String datetime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(datetime));
		calendar.set(5, 1);
		Date date = new Date(calendar.getTime().getTime());
		return date;
	}


	public static String getCurrentMonthEnd() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentDate());
		cal.set(11, 0);
		cal.set(12, 0);
		cal.set(13, 0);
		cal.set(14, 0);
		int year = cal.get(1);
		int month = cal.get(2);
		if (month == 0) {
			year--;
			month = 12;
		} else {
			month--;
		}
		String currentMonthEnd = (new StringBuilder(String.valueOf(String
				.valueOf(year)))).append(String.valueOf(month)).toString();
		return currentMonthEnd;
	}

	public static boolean isAfterToday(Date date) {
		return date != null && date.after(new Date());
	}
	
	//******************************************************************************************
	//更新2
    
	/**
	 * 获取一个月的中天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthDays(int year, int month) {
		int days[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (2 == month && 0 == year % 4 && (0 != year % 100 || 0 == year % 400))
			days[1] = 29;
		return days[month - 1];
	}
    /**
     * 获取当前月的中天数
     * @return
     */
	public static int getCurMonthDays() {
		int year = Calendar.getInstance().get(1);
		int month = Calendar.getInstance().get(2) + 1;
		return getMonthDays(year, month);
	}


	public static String getWeekday(String dateStr) throws Exception {
		Date date = parseDate(dateStr);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return (new SimpleDateFormat("EEEE")).format(c.getTime());
	}

	public static String getWeekday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return (new SimpleDateFormat("EEEE")).format(c.getTime());
	}


	public static void main(String[] args) {
		String month = "2009.07.11";
		String pattern = "yyyy-MM-dd";
		String pattern1 = "yyyy-MM-dd hh:mm:ss";
		String dbDate = "2013-01-20";
		
		String str11 = "2014-06-09 16:00:00";
		String str22 = "2013-09-05 04:30:00";
		
		System.out.println(getDatetimeGap(parseDate(str11, DEFAULT_DATETIME_PATTERN),getCurrentDate(),Calendar.MINUTE));
		
		System.out.println("======================");
		System.out.println(new Timestamp(addDays(getCurrentDate(), 1).getTime()));
		
		Date end  = addSeconds(getCurrentDate(), 60);
		System.out.println(getDiffDatetime(new Date(), end, TimeUnit.MINUTES));
		System.out.println(getDatetimeGap(new Date(), end, Calendar.MINUTE));
		
		System.out.println((int)1.2);
		globalFormat.applyPattern(DEFAULT_DATE_PATTERN);
		globalFormat.applyPattern(DEFAULT_DATETIME_PATTERN);
		System.out.println(formatDate(getSystemTimestamp(),"  "));
		
	}
}
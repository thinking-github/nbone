package org.nbone.constants;

/**
 * DateConstant
 * @author thinking 2014-06-09
 * @version 1.0
 */
public interface DateConstant {
	
	
	/**
	 * yyyy年MM月 -----CHINA CN 
	 */
	public static final String DATE_MM_PATTERN_CN = "yyyy年MM月";
	public static final String DATE_PATTERN_CN = "yyyy年MM月dd日";
	public static final String TIME_PATTERN_CN = "HH时mm分ss秒";
	public static final String DATETIME_MM_PATTERN_CN = "yyyy年MM月dd日HH时mm分";
	public static final String DATETIME_PATTERN_CN = "yyyy年MM月dd日HH时mm分ss秒";
	public static final String LONG_PATTERN_CN = "yyyy年MM月dd日HH时mm分ss秒.SSS";
	/**
	 * yyyy/MM   ----slash
	 */
	public static final String SLASH_DATE_MM_PATTERN = "yyyy/MM";
	public static final String SLASH_DATE_PATTERN = "yyyy/MM/dd";
	public final static String SLASH_DATETIME_MM_PATTERN  = "yyyy/MM/dd HH:mm"; 
	public final static String SLASH_DATETIME_PATTERN  = "yyyy/MM/dd HH:mm:ss"; 
	
	/**
	 * 不含分隔符号  WithOutSeparator <br>
	 * yyyyMM  yyyyMMdd
	 */
	public static final String DIRECT_DATE_MM_PATTERN = "yyyyMM";
	public static final String DIRECT_DATE_PATTERN = "yyyyMMdd";
	public static final String DIRECT_TIME_PATTERN = "HHmmss";
	public static final String DIRECT_DATETIME_MM_PATTERN = "yyyyMMddHHmm";
	public static final String DIRECT_DATETIME_PATTERN = "yyyyMMddHHmmss";
	/**
	 * 17 bit<br>
	 * yyyyMMdd HHmmss SSS
	 */
	public static final String DIRECT_LONG_PATTERN = "yyyyMMddHHmmssSSS";
	

	//默认含分隔符号
	/**
	 * 默认日期匹配格式 yyyy-MM
	 */
	public final static String DEFAULT_DATE_MM_PATTERN  = "yyyy-MM"; 
	
	/**
	 * 默认日期匹配格式 yyyy-MM-dd
	 */
	public final static String DEFAULT_DATE_PATTERN  = "yyyy-MM-dd"; 
	
	/**
	 * 默认时间匹配格式 HH:mm:ss
	 */
	public final static String DEFAULT_TIME_PATTERN  = "HH:mm:ss"; 
	
	/**
	 * 默认日期时间匹配格式 yyyy-MM-dd HH:mm
	 */
	public final static String DEFAULT_DATETIME_MM_PATTERN  = "yyyy-MM-dd HH:mm"; 
	
	/**
	 * 默认日期时间匹配格式 yyyy-MM-dd HH:mm:ss
	 */
	public final static String DEFAULT_DATETIME_PATTERN  = "yyyy-MM-dd HH:mm:ss"; 
	
	/**
	 * 默认长日期时间匹配格式 yyyy-MM-dd HH:mm:ss.SSS
	 */
	public final static String DEFAULT_LONG_PATTERN  = "yyyy-MM-dd HH:mm:ss.SSS"; 
	
	
	/**
	 * default formats match pattern
	 */
	public final static String[] DEFAULT_FORMATS  = {
													DEFAULT_DATETIME_PATTERN,
													DEFAULT_DATETIME_MM_PATTERN,
													DEFAULT_DATE_PATTERN, 
													DEFAULT_TIME_PATTERN,
													DEFAULT_DATE_MM_PATTERN
													}; 
	
	
	

}

package org.nbone.util.lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * @author thinking
 * @version 1.0
 * @since   2013-12-12
 *
 */
public class CheckStrUtils {


	public CheckStrUtils() {
	}

	public static boolean validate(String reg, String str)
	  {
	    Pattern pattern = Pattern.compile(reg);
	    Matcher matcher = pattern.matcher(str);
	    return matcher.matches();
	  }

	  public static boolean isEmail(String str)
	  {
	    String reg = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
	    return validate(reg, str);
	  }

	  public static boolean isIP(String str)
	  {
	    String reg = "((25[0-5]|2[0-4]\\d|[1]?\\d?\\d)\\.){3}((25[0-5]|2[0-4]\\d|[1]?\\d?\\d))";
	    return validate(reg, str);
	  }

	  public static boolean isUrl(String str)
	  {
	    String reg = "((http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?)|((((ht|f)tp(s?))://)?(www.|[a-zA-Z].)[a-zA-Z0-9-.]+.(com|edu|gov|mil|net|org|biz|info|name|museum|us|ca|uk)(:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\;\\?\\'\\\\+&amp;%\\$#\\=~_\\-]+))*)|(\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/))))";
	    return validate(reg, str);
	  }

	  public static boolean isTelephone(String str)
	  {
	    String reg = "^(0([3-9]\\d\\d|10|2[1-9])-?[2-8]\\d{6,7})$";
	    return validate(reg, str);
	  }

	  public static boolean isPasswLength(String str)
	  {
	    String reg = "^\\w{6,18}$";
	    return validate(reg, str);
	  }

	  public static boolean isPostalcode(String str)
	  {
	    String reg = "^[1-9]\\d{5}$";
	    return validate(reg, str);
	  }

	  public static boolean isHandset(String str)
	  {
	    String reg = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
	    return validate(reg, str);
	  }

	  public static boolean isIDcard(String str)
	  {
	    String reg = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})[1|2][0-9]{3}((0[1-9])|(1[0-2]))((0[1-9])|([1|2][0-9])|(3[0-1]))[0-9]{3}[Xx0-9]";

	    return validate(reg, str);
	  }

	  public static boolean isMonth(String str)
	  {
	    String reg = "^(0?[1-9]|1[0-2])$";
	    return validate(reg, str);
	  }

	  public static boolean isDay(String str)
	  {
	    String reg = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
	    return validate(reg, str);
	  }

	  public static boolean isNumber(String str)
	  {
	    String reg = "^[0-9]+$";
	    return validate(reg, str);
	  }

	  public static boolean isIntNumber(String str)
	  {
	    String reg = "^[1-9]\\d*$";
	    return validate(reg, str);
	  }

	  public static boolean isUpChar(String str)
	  {
	    String reg = "^[A-Z]+$";
	    return validate(reg, str);
	  }

	  public static boolean isLowChar(String str)
	  {
	    String reg = "^[a-z]+$";
	    return validate(reg, str);
	  }

	  public static boolean isChinese(String str)
	  {
	    String reg = "^[Α-￥]+$";
	    return validate(reg, str);
	  }

	  public static boolean isTime(String str)
	  {
	    String reg = "^(([0-1][0-9])|(2[0-3])):([0-5][0-9]):([0-5][0-9])$";
	    return validate(reg, str);
	  }

	  public static boolean isEFloat(String str)
	  {
	    String reg = "[\\+\\-]?[\\d]+([\\.][\\d]*)?([Ee][\\+\\-]?[\\d]+)?";
	    return validate(reg, str);
	  }

	  public static boolean isQuoteIn(String str)
	  {
	    String reg = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+";
	    return validate(reg, str);
	  }

	  public static boolean isFloat(String str)
	  {
	    String reg = "^(-?\\d*)(\\.\\d*)?";
	    return validate(reg, str);
	  }

	  public static boolean isValidHour(String hourStr)
	  {
	    if ((hourStr == null) || (hourStr.length() != 2))
	      return false;
	    if (!isNumber(hourStr)) {
	      return false;
	    }
	    int hour = Integer.parseInt(hourStr);
	    if ((hour < 0) || (hour > 23)) {
	      return false;
	    }
	    return true;
	  }

	  public static boolean isValidMinuteOrSecond(String str)
	  {
	    if ((str == null) || (str.length() != 2))
	      return false;
	    if (!isNumber(str)) {
	      return false;
	    }
	    int s = Integer.parseInt(str);
	    if ((s < 0) || (s > 59)) {
	      return false;
	    }
	    return true;
	  }
//-------------------------------2014-07-26-----------------------------------------
	  /**
	   * 判断是否是UUID
	   * @param str
	   * @return
	   * @author Thinking  2014-7-26
	   */
	  public static boolean isUUID(String str)
	  {
		String reg = "[a-fA-F0-9]+";
		  
		return validate(reg, str);
	    
	  }

}

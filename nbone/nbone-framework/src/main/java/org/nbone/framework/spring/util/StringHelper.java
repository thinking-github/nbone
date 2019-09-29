package org.nbone.framework.spring.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.NumberUtils;
/**
 * 
 * @author thinking
 * @since 2016-08-05
 * @version 1.0.1
 */
@SuppressWarnings({"unused","unchecked"})
public class StringHelper extends org.springframework.util.StringUtils {
	
	
	public static List<String> commaDelimitedListToList(String str) {
		return delimitedListToList(str,",");
	}
	
	
	public static List<String> delimitedListToList(String str, String delimiter) {
		return delimitedListToList(str, delimiter,(String)null);
	}

	/**
	 *
	 * @param str
	 * @param delimiter
	 * @param charsToDelete
	 * @see org.springframework.util.StringUtils#delimitedListToStringArray(String, String, String)
	 * @return
	 */
	public static List<String> delimitedListToList(String str, String delimiter, String charsToDelete) {
		List<String> result = new ArrayList<String>();
		if (str == null) {
			return result;
		}
		if (delimiter == null) {
			result.add(str);
			return result;
		}
		
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		}
		else {
			int pos = 0;
			int delPos;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return result;
	}

	/**
	 * @param str
	 * @param delimiter
	 * @param targetClass
	 * @param <T>
	 * @return
	 */
	public static <T extends Number> T[] delimitedListToNumberArray(String str, String delimiter,Class<T> targetClass) {
		List<String> temp = delimitedListToList(str, delimiter);
		Object array = Array.newInstance(targetClass,temp.size());
		for (int i = 0; i < temp.size(); i++) {
			T number = NumberUtils.parseNumber(temp.get(i),targetClass);
			Array.set(array,i,number);
		}
		return (T[]) array;
	}

	/**
	 *  <li>1,2,3,4 --> Integer[]{1,2,3,4}
	 * 	<li>1,2,3,4 --> Long[]{1,2,3,4}
	 * @param str
	 * @param targetClass
	 * @param <T>
	 * @return
	 */
	public static <T extends Number> T[] delimitedListToNumberArray(String str,Class<T> targetClass){
		return delimitedListToNumberArray(str,",",targetClass);
	}

	public static <T extends Number> List<T> delimitedListToListWithNumber(String str, String delimiter,Class<T> targetClass) {
		List<String> temp = delimitedListToList(str, delimiter);
		List<T> result  = new ArrayList<T>(temp.size());
		for (String string : temp) {
		 T number = NumberUtils.parseNumber(string,targetClass);
		 result.add(number);
		}
		return result;
	}
	
	public static <T extends Number> List<T> delimitedListToListWithNumber(String str,Class<T> targetClass) {
		return delimitedListToListWithNumber(str, ",", targetClass);
	}
	
	
	

}

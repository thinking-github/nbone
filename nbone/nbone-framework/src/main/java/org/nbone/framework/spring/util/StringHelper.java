package org.nbone.framework.spring.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.NumberUtils;
/**
 * 
 * @author thinking
 * @since 2016-08-05
 * @version 1.0.1
 */
public class StringHelper extends org.springframework.util.StringUtils {
	
	
	public static List<String> commaDelimitedListToList(String str) {
		return delimitedListToList(str,",");
	}
	
	
	public static List<String> delimitedListToList(String str, String delimiter) {
		return delimitedListToList(str, delimiter,(String)null);
	}
	
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

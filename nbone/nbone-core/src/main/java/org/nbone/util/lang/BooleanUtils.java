/*
 * ${nbone.copyright}
 * ${nbone.license}
 * 
 */
package org.nbone.util.lang;
/**
 * 
 * <p></p>
 * @author thinking
 * @version 1.0 
 * @since   2015-12-12
 * @see org.apache.commons.lang3.BooleanUtils
 * @see org.springframework.beans.propertyeditors.CustomBooleanEditor
 */
public class BooleanUtils {
	
	/**
	 * <p>默认false 只有符合 true相关条件时才为true(用于默认关闭功能)
	 * <pre>
	 *  "true"  == true;
	 *  "yes"   == true;
	 *  "y"     == true;
	 *  "on"    == true;
	 *  "1"     == true;
	 * </pre>
	 * @param s
	 * @return
	 */
	 public static boolean valueOf(String s) {
		 if(s == null){
			 return false;
		 }
		 
		 if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("y") || s.equalsIgnoreCase("on")){
			 
			 return true;
		 }
		 if(s.trim().equals("1")){
			 
			 return true;
		 }
		 
		return false;
	 }
	 
	 public static boolean valueOf(int value) {
	        if(value == 1){
	        	return true;
	        }		 
			return false;
	}
	 
	 /**
	  * <p>默认true 只有符合 false相关条件时才为false(用于默认开启功能)
	  * @param s
	  * @return
	  */
	 public static boolean defaultTrueValueOf(String s) {
		 if(s == null){
			 return true;
		 }
		 
		 if(s.equalsIgnoreCase("false") || s.equalsIgnoreCase("no") || s.equalsIgnoreCase("n") || s.equalsIgnoreCase("off")){
			 
			 return false;
		 }
		 if(s.trim().equals("0")){
			 
			 return false;
		 }
		 
		return true;
	 }
	 
	 
	 
	

}

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

}

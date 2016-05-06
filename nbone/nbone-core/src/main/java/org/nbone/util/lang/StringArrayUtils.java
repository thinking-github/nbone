package org.nbone.util.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author uap
 * @author thinking
 * @version 1.0 
 * @since 2013-08-12
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class StringArrayUtils {

	 public static boolean contains(String[] array, String t)
	  {
	    for (String tt : array) {
	      if (tt.equals(t)) {
	        return true;
	      }
	    }
	    return false;
	  }

	 
	public static String[] subtract(String[] arr_1, String[] arr_2)
	  {
	    
		List list = new ArrayList(Arrays.asList(arr_1));
	    list.removeAll(Arrays.asList(arr_2));

	    return (String[])list.toArray(new String[list.size()]);
	  }

	  public static String strArrToString(String[] arrStrs)
	  {
	    StringBuffer sb = new StringBuffer();
	    String ret = "";
	    for (String str : arrStrs) {
	      sb.append(str + ";");
	    }
	    ret = sb.substring(0, sb.length() - 1);
	    return ret;
	  }

	  public static String[] trim(String[] arrStrs)
	  {
	    List list = Arrays.asList(arrStrs);
	    String[] newArrStrs = new String[list.size()];
	    for (int i = 0; i < list.size(); i++) {
	      String value = (String)list.get(i);
	      value = value.trim();
	      newArrStrs[i] = value;
	    }
	    return newArrStrs;
	  }
}

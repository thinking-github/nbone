package org.nbone.persistence.util;

import java.util.Collection;

public class SqlUtils {
	
	/**
	 * "2014-01-01,kkk,000"  to (tempA.remark = '2014-01-01' or  tempA.remark = 'kkk' or  tempA.remark = '000')<br>
	 * "1,2,3"  to (tempA.remark = 1 or  tempA.remark = 2 or  tempA.remark = 3)
	 * @param name   Field name
	 * @param value  Field value
	 * @param clazz  target object class
	 * @return
	 */
    public static String stringSplit2In(String name, String value,Class<?> clazz)
    {
    	 String fh = "";
    	if(clazz == String.class){
    		    fh = "'";
    	}
        String arr[] = value.split(",");
        int length = arr.length;
        StringBuilder moreOrsb = new StringBuilder("( ");
        for(int i = 0; i < length-1; i++)
        {
            String did = arr[i];
            moreOrsb.append(name).append(" = ").append(fh).append(did).append(fh).append(" or ");
        }
        moreOrsb.append(name).append(" = ").append(fh).append(arr[length-1]).append(fh);
        moreOrsb.append(" )");
        return moreOrsb.toString();
    }
    
	/**
	 * "2014-01-01,kkk,000"  to (tempA.remark != '2014-01-01' and  tempA.remark != 'kkk' and  tempA.remark != '000')<br>
	 * "1,2,3"  to (tempA.remark != 1 and  tempA.remark != 2 and  tempA.remark != 3)
	 * @param name   Field name
	 * @param value  Field value
	 * @param clazz  target object class
	 * @return
	 */
    public static String stringSplit2Notin(String name, String value,Class<?> clazz)
    {
    	 String fh = "";
     	if(clazz == String.class){
     		    fh = "'";
     	}
         String arr[] = value.split(",");
         int length = arr.length;
         StringBuilder notinsb = new StringBuilder("( ");
         for(int i = 0; i < length-1; i++)
         {
             String did = arr[i];
             notinsb.append(name).append(" != ").append(fh).append(did).append(fh).append("  and ");
         }
         notinsb.append(name).append(" != ").append(fh).append(arr[length-1]).append(fh);
         notinsb.append(" )");
         return notinsb.toString();
    }
    
    public static StringBuilder list2In(String name,Collection<?> values){
		return list2In(name,values.toArray());
    }
    /**
     * [1,2,3] ---> id in (?,?,?)
     * @param name
     * @param values
     * @return
     */
    public static StringBuilder list2In(String name,Object[] values){
   	  StringBuilder sql = new StringBuilder(" ").append(name).append(" in ").append("(");
   	  int length = values.length;
   	  for (int i = 0; i <  length -1; i++) {
   		 sql.append(" ?, ");
	  }
      sql.append("? )");
	  return sql;
   }
    
    //Like Match Char _ %
    public static boolean isLikeMatchChar(String value)
    {
      boolean result = false;
      if ((value.contains("_")) || (value.contains("%"))) {
        return true;
      }
      return result;
    }

    public static String dealLikeMatchChar(String value)
    {
      if (isLikeMatchChar(value)) {
        value = value.replace("_", "\\_");
        value = value.replace("%", "\\%");
      }
      return value;
    }
    
	public static void main(String[] args) {
		String ss = "kk,oo";
		String ss4int = "1,2,3";
		System.out.println(stringSplit2In("user.id", ss, String.class));
		System.out.println(stringSplit2In("user.id", ss4int,Integer.class));
		
		System.out.println(stringSplit2Notin("user.id", ss4int,String.class));
		System.out.println(stringSplit2Notin("user.id", ss4int,Integer.class));
		
	}

}

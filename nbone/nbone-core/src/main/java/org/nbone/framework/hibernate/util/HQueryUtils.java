package org.nbone.framework.hibernate.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class HQueryUtils {
	
	
	@SuppressWarnings({ "rawtypes" })
	public Query setParameter(Query query, Map<String, Object> map) {  
        if (map != null) {  
            Set<String> keySet = map.keySet();  
            for (String string : keySet) {  
                Object obj = map.get(string);  
                if (obj instanceof Collection) {  
                    query.setParameterList(string, (Collection)obj);  
                }
                else if(obj instanceof Object[]) {  
                    query.setParameterList(string, (Object[])obj);  
                }
                else {  
                    query.setParameter(string, obj);  
                }  
            }  
        }  
        return query;  
    } 
	
	public static  Query applyNamedParameterToQuery(Query queryObject, String paramName, Object value)
			throws HibernateException {

		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection) value);
		}
		else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		}
		else {
			queryObject.setParameter(paramName, value);
		}
		return queryObject;
	}
	
	
    public static String getCountQueryString(String hql) {
    	
        int index = hql.toLowerCase().indexOf("from".toLowerCase());
        int groupby = hql.toLowerCase().indexOf("group by".toLowerCase());
        if(index != -1)
        {
            String counthql = groupby != -1 ? hql.substring(index, groupby) : hql.substring(index);
            index = counthql.toLowerCase().indexOf("from".toLowerCase());
            int orderby = counthql.toLowerCase().indexOf("order by".toLowerCase());
            counthql = orderby != -1 ? counthql.substring(index, orderby) : counthql.substring(index);
            return (new StringBuilder("select count(*) ")).append(counthql).toString();
        } else
        {
        }
		return null;
    }

}

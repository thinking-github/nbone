package org.nbone.component.logger.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.nbone.component.logger.LoggerStatic;
import org.springframework.util.Assert;

public class LoggerUtils {
	
	  /**
     * 根据模糊的JOSN String 返回标准的JSON String<br>
     * 例如: 处理这样的字符 <b> 0:QUERY,1:CREATE,2:DELETE,3:UPDATE </b><br>
     * 注意:配置文件中注意使用 ": , { }"的冲突
     * @param str 
     * @return
     */
    public static String getStandardJSONObject(String str){
    	String json;
    	StringBuilder sb = new StringBuilder();
    	if(str == null){
    		return "{}";
    	}
    	//处理是否含有{}
		if(str.startsWith(LoggerStatic.JSON_OBJECT_PREFIX) && str.endsWith(LoggerStatic.JSON_OBJECT_SUFFIX)){
			json = str;
		}else{
			json = LoggerStatic.JSON_OBJECT_PREFIX+str+LoggerStatic.JSON_OBJECT_SUFFIX;
		}
	    //处理是否含有""或者 ''
		if(json.indexOf("'") ==-1 && json.indexOf("\"")==-1){
			int length = json.length();
			for (int i = 0; i < length; i++) {
				char c = json.charAt(i);
				switch (c) {
				case '{':
					//防止中间位置的 "{" 的影响结构
					if(i==0){
						sb.append(c+"\"");
					}else{
						sb.append(c);
					}
					break;
				case ':':
					sb.append("\""+c+"\"");
					break;
				case ',':
					sb.append("\""+c+"\"");
					break;
				case '}':
					//防止中间位置的 "}" 的影响结构
					if(i==length-1){
						sb.append("\""+c);
					}else{
						sb.append(c);
					}
					break;
				default:
					sb.append(c);
					break;
				}
			}
		}else{
			sb.append(json);
		}
		
		return sb.toString();
    }
    
    /**
     * 通过值查找map中的键
     * @param map
     * @param val
     * @return
     */
    public static String getMapKeyByValue(Map<String,Object> map,String val){
    	Assert.notNull(map, "map not is null.");
    	Assert.notNull(val, "value not is null.");
    	
    	for (Map.Entry<String,Object> entry : map.entrySet()) {
    		if(val.equals(entry.getValue())){
				return String.valueOf(entry.getKey());
			}
		}
		return null;
    }
	

}

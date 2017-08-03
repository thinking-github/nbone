package org.nbone.component.logger;

import java.util.HashMap;
import java.util.Map;

public class LoggerContext{

	private LoggerContext() {
		
	}
	private static LoggerContext context = new LoggerContext();
	
	private Map<String,Object> map = new HashMap<String, Object>();
	
	public static LoggerContext getCurrentLoggerContext(){
		
		return context;
	}
	public Object get (String id){
			
		return map.get(id);
	}
	
	public Map getMap (String id){
		
		return (Map) get(id);
	}
	public String  getString (String id){
			
			return String.valueOf(get(id));
		}
	
	public void  put(String id,Object value){
		
		map.put(id, value);
	}
	
	
	
	
	
	
	

}

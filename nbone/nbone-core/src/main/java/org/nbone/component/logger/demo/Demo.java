package org.nbone.component.logger.demo;


import java.util.HashMap;
import java.util.Map;

import org.nbone.component.logger.util.LoggerUtils;
import org.nbone.util.json.JSONOperUtils;

import net.sf.json.JSONObject;

public class Demo {
	
	public static void main(String[] args) {
		Map map = new HashMap();
		map.put("0333", "查询");
		map.put("1", "创建");
		System.out.println(LoggerUtils.getMapKeyByValue(map, "查询"));
		String ss = "ID:00102,OPERATION_TYPE:QUERY,DESC:用户所在单位查询|ID=$[0]";
		System.out.println(LoggerUtils.getStandardJSONObject("0:QUERY,1:CREATE,2:DELETE,3:UPDATE"));
		System.out.println(JSONObject.fromObject(LoggerUtils.getStandardJSONObject("0:QUERY,1:CREATE,2:DELETE,3:UPDATE")));
		System.out.println(LoggerUtils.getStandardJSONObject(ss));
		System.out.println(JSONObject.fromObject(LoggerUtils.getStandardJSONObject(ss)));
		
		
		int  chen = 1;
		Object[] obj = {chen};
		if(obj[0] instanceof Number ){
			System.out.println(chen);
		}
	}

}

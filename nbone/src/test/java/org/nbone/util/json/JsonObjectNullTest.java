package org.nbone.util.json;

import org.nbone.pojo.UserJson;
import org.nbone.util.json.jackson.JsonUtils;

import com.alibaba.fastjson.JSON;

public class JsonObjectNullTest {

	public static void main(String[] args) throws Exception {
		
		
		
		UserJson user = UserJson.getUserJson();
		user.setPassword("");
		String json1= JSONOperUtils.pojoToJSON(user);
		String json1Filter= JSONOperUtils.pojoToJSONFilter(user);
		System.out.println(json1);
		System.out.println(json1Filter);
		UserJson user1 = JSONOperUtils.JSONToObject(json1, UserJson.class);
		System.out.println("=====================");
		
		
		String json2 = JsonUtils.pojoToJson(user);
		String json2filter = JsonUtils.pojoToJsonFilter(user);
		System.out.println(json2);
		System.out.println(json2filter);
		UserJson user2 = JsonUtils.toObjectFromJson(json2, UserJson.class);
		System.out.println("=====================");
		
		
		
		String json3 = JSON.toJSONString(user);
		System.out.println(json3);
		UserJson user3 = JSON.parseObject(json3, UserJson.class);
		
	
		

	}

}

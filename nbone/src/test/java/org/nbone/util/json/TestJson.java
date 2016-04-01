package org.nbone.util.json;

import org.nbone.util.json.jackson.JsonUtils;

public class TestJson {
	
	
	public static void main(String[] args) throws Exception {
		System.out.println(JSONOperUtils.JSONToJSONObject("{id:'001',name:'hhhhhhh|${0}'}"));
			test("0");		
			System.out.println("----------------");
		//System.out.println(user2);
      /*  for (int i = 0; i < 20; i++) {
        	test("--"+(i++));		
        	System.out.println("----------------");
		}*/
		
	}
	
	public static void test(String id) throws Exception{
		
         UserJSONDataType user  = UserJSONDataType.getUser(id);
		
		long start1 = System.currentTimeMillis();
		String userString = "{}";
		userString = JSONOperUtils.pojoToJSON(user);
		long end1 = System.currentTimeMillis();
		System.out.println("JSONLIB---"+(end1-start1));
		
		System.out.println(userString);
		
		long start2 = System.currentTimeMillis();
		String userString2 = JsonUtils.pojoToJson(user);
		long end2 = System.currentTimeMillis();
		System.out.println("objectmap---"+(end2-start2));
		
		System.out.println(userString2);
		
		long start3 = System.currentTimeMillis();
		UserJSONDataType user2 =  JSONOperUtils.JSONToObject(userString2, UserJSONDataType.class);
		long end3 = System.currentTimeMillis();
		System.out.println("JSONLIB---"+(end3-start3));
		
		
		long start4 = System.currentTimeMillis();
		UserJSONDataType user1= JsonUtils.toObjectFromJson(userString2, UserJSONDataType.class);
		long end4 = System.currentTimeMillis();
		System.out.println("objectmap---"+(end4-start4));
		
	}

}

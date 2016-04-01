package org.nbone.util.json;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.nbone.pojo.User;
import org.nbone.test.UserTest;
import org.nbone.util.json.jackson.JsonUtils;
import org.nbone.web.support.WebResultWrapper;

import net.sf.json.JSONArray;

public class JOSNTEST {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		List<String> stringList = new ArrayList<String>();
		stringList.add("777777");
		stringList.add("888888");
		UserTest user = UserTest.getUser("001");
		user.setUser1(new User("user1..", "user1=="));
		//user.setStringlist(stringList);
		//List<UserTest> resultUserList = new ArrayList<UserTest>();
		List resultUserList = new ArrayList();
		//resultUserList.add("999");
		resultUserList.add(UserTest.getUser("002"));
		resultUserList.add(user);
		WebResultWrapper resultS = WebResultWrapper.successResultWraped(resultUserList);
		String webResultWrapperString = JSONOperUtils.pojoToJSON(resultS);
		
		String resultUserListS = JSONOperUtils.pojoToJSON(resultUserList);
		
		Map<String,Class<?>> classMap = new HashMap<String, Class<?>>();
		//classMap.put("stringlist", UserTest.class);
		System.out.println("====JSONOperUtils.pojoToJSON-webResultWrapperString");
		System.out.println(webResultWrapperString);
		
		//List list = JSONOperUtils.JSONToObject(resultUserListS,ArrayList.class,classMap);
		List dd= JSONOperUtils.JSONToList(resultUserListS);
		
		Collection<UserTest> cool = JSONOperUtils.toCollectionForBean(resultUserListS, UserTest.class);
       
		//WebResultWrapper ooclassMap = JSONOperUtils.JSONToObject(webResultWrapperString,WebResultWrapper.class,classMap);
		
		String[] hi ={"resultValue"};
		WebResultWrapper ttt= JSONOperUtils.toJavaBean(webResultWrapperString, WebResultWrapper.class, hi);
		
		List list  = (List) JSONArray.toCollection((JSONArray) ttt.getResultValue(), UserTest.class);
		
		
		System.out.println("=====obj1==================");
		
		
		String webResultWrapperString1 =JsonUtils.pojoToJson(resultS);
		System.out.println("JsonUtil.pojoToJson-------------");
		System.out.println(webResultWrapperString1);
		
		
		TypeReference<List<Map<String,Object>>>  tr_list4Map =  new  TypeReference<List<Map<String,Object>>>() {};
		TypeReference<List<UserTest>>  tr_list4U =  new  TypeReference<List<UserTest>>() {};
		TypeReference<UserTest>  tr_user =  new  TypeReference<UserTest>(){};
		TypeReference<Map>  tr_map =  new  TypeReference<Map>(){};
		ParameterizedType type = 	(ParameterizedType) tr_list4Map.getType();
		System.out.println("type=="+type.getActualTypeArguments()[0]);
		System.out.println("type=="+type.getRawType());
		System.out.println("type=="+type.getOwnerType());
		
		WebResultWrapper webResultWrapper = JsonUtils.toObjectFromJson(webResultWrapperString1,WebResultWrapper.class);
		List<UserTest> list_u = JsonUtils.convertValue(webResultWrapper.getResultValue(), tr_list4U);
		List<Map<String,Object>> list_m =  JsonUtils.convertValue(webResultWrapper.getResultValue(), tr_list4Map);
		
		
		System.out.println("JsonUtil.toObjectFromJsont-");
		System.out.println(webResultWrapper);
		
		
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
		

	}

}

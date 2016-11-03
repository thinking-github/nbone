package org.nbone.util.reflect;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nbone.mx.datacontrols.datapage.PagerModel;
import org.nbone.test.UserTest;

import com.fasterxml.jackson.core.type.TypeReference;


public class TypeReferenceTest {

	public static void main(String[] args) {
		
		List<String> list = new ArrayList<String>();
		Map<String,Object> map = new HashMap<String, Object>();
		
		TypeReference<List<Map<String,Object>>>  tr_list4Map =  new  TypeReference<List<Map<String,Object>>>() {};
		TypeReference<List<UserTest>>  tr_list4U =  new  TypeReference<List<UserTest>>() {};
		
		
		Type typeListMap  = tr_list4Map.getType();
		Type typeList  = tr_list4U.getType();
		
		Type[] type0 = ParameterizedTypeUtils.getActualTypeArguments(tr_list4U.getClass());
		
		
		Type[] type1 = ParameterizedTypeUtils.getActualTypeArguments(list.getClass());
		Type[] type2 = ParameterizedTypeUtils.getActualTypeArguments(map.getClass());
		
		PagerModel<Object> page  = new PagerModel<Object>();		
		Class<?> class1 = GenericsUtils.getSuperClassGenricType(page.getClass());
		Type type_1 = ParameterizedTypeUtils.getActualTypeArgument(page.getClass());
		

	}

}

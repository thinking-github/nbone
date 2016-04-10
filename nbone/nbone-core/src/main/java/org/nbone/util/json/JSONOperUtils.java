package org.nbone.util.json;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.nbone.constant.DateConstant;

import net.sf.ezmorph.MorpherRegistry;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.JSONUtils;

/**
 * base by  net.sf.json
 * @author thinking  
 * @since   2014-04-26
 *
 */
public class JSONOperUtils implements DateConstant{


	public static <T> T JSONToObject(String json, Class<T> beanClz) {
		return JSONToObject(json, beanClz, null);
	}
	/**
	 * JSONString to POJO 
	 * @param json
	 * @param beanClz
	 * @param classMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T JSONToObject(String json, Class<T> beanClz, Map classMap) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		T bean ;
		if(classMap == null){
			bean = (T) JSONObject.toBean(jsonObject, beanClz);
		}else{
			bean = (T) JSONObject.toBean(jsonObject, beanClz, classMap);
		}
		return bean;
	}

	/**
	 * 将JSON字符串 转化成JSONObject (Map 中可以含有JSONArray 、JSONObject等)<br>
	 * JSONObject is Map
	 * @param json
	 * @return
	 * @see JSONObject
	 */
	public static JSONObject JSONToJSONObject(String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		return jsonObject;
	}

	@SuppressWarnings("rawtypes")
	public static <T> Map<String,T> JSONToMapForBean(String json, Class<T> beanClz) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		Map<String,T> map = new HashMap<String,T>();
		String key;
		for (Iterator iter = jsonObject.keys(); iter.hasNext(); map.put(key,(T)JSONObject.toBean(jsonObject.getJSONObject(key), beanClz)))
			key = (String) iter.next();

		return map;
	}

	/**
	 * 
	 * JSONString to Map<String,T>(Map of  element convert target Type Object)<br>
	 * Map 中元素必须保持类型一致
	 * @param <T>
	 * @param json
	 * @param beanClz
	 * @param classMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Map<String,T> JSONToMapForBean(String json, Class<T> beanClz, Map classMap) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		Map<String,T> map = new HashMap<String, T>();
		String key;
		for (Iterator iter = jsonObject.keys(); iter.hasNext(); map.put(key,(T) JSONObject.toBean(jsonObject.getJSONObject(key), beanClz,classMap)))
			key = (String) iter.next();

		return map;
	}
	/**
	 * 将JSON数组字符串 转化成String[]
	 * @param json
	 * @return
	 */
	public static String[] JSON2StringArray(String json) {
		Object[] objArray = JSONToArray(json);
		String[]  result = new String[objArray.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = String.valueOf(objArray[i]);
		}
		return result;
	}
	
	/**
	 * 将JSON数组字符串 转化成Object[]
	 * @param json
	 * @return
	 */
	public static Object[] JSONToArray(String json) {
		return JSONArray.fromObject(json).toArray();
	}
	/**
	 * 将JSON数组字符串 转化成List(List 中可以含有JSONArray 、JSONObject等)
	 * @param json
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public static List<Object> JSONToList(String json) {
		JSONArray array = JSONArray.fromObject(json);
		int size = array.size();
		List<Object>  resultList = new ArrayList<Object>(size);
		for (int i = 0; i < size; i++) {
			resultList.add(array.get(i));
		}
		return resultList;
	}
    /**
     * @see #JSONToListForBean(String, Class, Map)
     */
	public static  <T> List<T> JSONToListForBean(String json, Class<T> clazz) {
		return JSONToListForBean(json, clazz, null);
	}
	 /**
     * JSONArray String  to List<T>,(child element convert target Type Object)<br>
     * 数组中元素必须保持类型一致
     * @param json
     * @param clazz 将数组字符串中的单个对象转化成此目标对象
     * @param classMap
     * @return
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> JSONToListForBean(String json, Class<T> clazz, Map classMap) {
		JSONArray array = JSONArray.fromObject(json);
		int size = array.size();
		List<T> resultList  = new ArrayList<T>(size);
 		for (int i = 0; i < size; i++) {
 			//array of element must is JSONObject,not is JSONArray
			JSONObject jsonObject = array.getJSONObject(i);
			T bean = null;
			if(classMap == null){
				bean= (T) JSONObject.toBean(jsonObject, clazz);
			}else{
				bean  = (T) JSONObject.toBean(jsonObject, clazz, classMap);
			}
			resultList.add(bean);
		}

		return resultList;
	}
	
	//------------------------start new add method 20150107-------------------------
	/**
	 * JSONArray String  to Collection<T>, 
	 * @param json
	 * @param clazz
	 * @return default return List
	 */
	@SuppressWarnings("unchecked")
	public static  <T> Collection<T> toCollectionForBean(String json, Class<T> clazz) {
		JSONArray array = JSONArray.fromObject(json);
		Collection<T> collection = JSONArray.toCollection(array, clazz);
		return collection;
	}
	/**
	 * JsonString to JavaBean  
	 * @param json
	 * @param beanClz
	 * @param highKeys 不好转化的Bean propertie 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toJavaBean(String json, Class<T> beanClz,String[] highKeys) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		T result;
		if(highKeys == null){
			return (T) JSONObject.toBean(jsonObject, beanClz);
		}else{
				//save temp
				//remove
			    Map<String,Object> properties = new HashMap<String, Object>();
			    for (int i = 0; i < highKeys.length; i++) {
			    	String key =  highKeys[i];
			    	Object object = jsonObject.get(key);
			    	jsonObject.remove(key);
			    	properties.put(key, object);
			    	
			    }
			    result =  (T) JSONObject.toBean(jsonObject, beanClz);
			    
			    try {
					BeanUtils.populate(result, properties);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		
		return result;
	}
	
	//------------------------end new add method 20150107-------------------------
	
	
    /**
     * javaObject to  JSON String （Object、Object[]、Map、Collection、List）
     * @param obj
     * @return
     */
	public static String pojoToJSON(Object obj,JsonConfig jsonConfig) {
		String jsonStr = null;
		
		if (obj == null)
			return "{}";
		if ((obj instanceof Collection) || (obj instanceof Object[]) || obj.getClass().isArray())
			jsonStr = JSONArray.fromObject(obj, jsonConfig).toString();
		else
			jsonStr = JSONObject.fromObject(obj, jsonConfig).toString();
		return jsonStr;
	}
	
	
	public static String pojoToJSON(Object obj) {
		return pojoToJSON(obj,jsonCfg);
		
	}
	/**
	 * pojo to json string (值为空的不进行序列化)
	 * @param obj
	 * @return
	 */
	public static String pojoToJSONFilter(Object obj) {
		
		return pojoToJSON(obj,jsonCfg1);
	}
	
	
	static JsonConfig jsonCfg;
	static JsonConfig jsonCfg1;
	static {
		//JavaObject to JSON String 
		jsonCfg = new JsonConfig();
		JsonValueProcessor toJSONProcessor = new JsonValueProcessorImpl(DEFAULT_DATETIME_PATTERN);
		jsonCfg.registerJsonValueProcessor(Date.class,toJSONProcessor);
		jsonCfg.registerJsonValueProcessor(Timestamp.class,toJSONProcessor);
		jsonCfg.registerJsonValueProcessor(java.sql.Date.class,toJSONProcessor);
		
		//jsonCfg.setJsonPropertyFilter(new NullToPropertyFilter());
		
		
		
		//JSON String to JavaObject
		MorpherRegistry mr = JSONUtils.getMorpherRegistry();
		String[] pattern = DEFAULT_FORMATS;
		//Date convert
		//java.sql.Timestamp convert
		//java.sql.Date convert
		//DateMorpher dm = new DateMorpher(pattern);
		EzmorphDateMorpher dateMorpher = new EzmorphDateMorpher(pattern);
		EzmorphSqlTimestampMorpher timestampMorpher = new EzmorphSqlTimestampMorpher(pattern);
		EzmorphSqlDateMorpher      sqlDateMorpher = new EzmorphSqlDateMorpher(pattern);
		//mr.registerMorpher(dm);
		mr.registerMorpher(dateMorpher);
		mr.registerMorpher(timestampMorpher);
		mr.registerMorpher(sqlDateMorpher);
		
		//--------------------------------------------------
		jsonCfg1 = new JsonConfig();
		jsonCfg1.setJsonPropertyFilter(new NullValuePropertyFilter());
	}
	
	
	   
	  public static void main(String[] args) {
		  String ss = "[0,1,2,3,{id=9},['l','oo']]";
		  JSONArray array = JSONArray.fromObject(ss);
		  Collection coll = JSONArray.toCollection(array);
		  
		  
		System.out.println(JSONToList(ss));
	  }

}

package org.nbone.util.json.jackson;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.nbone.constants.DateConstant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

/**
 * base by org.codehaus.jackson.map.ObjectMapper
 * @author thinking  2014-04-26
 * @version 1.0
 * @since 2014-04-26
 */
public class JsonUtils implements DateConstant{
	
	private static final ObjectMapper mapper;
	/**
	 * 第二种场景使用
	 */
	private static ObjectMapper mapper1 = new ObjectMapper();
	
	static{
		mapper = newInstance();
	}
	
	public static ObjectMapper newInstance() {
		ObjectMapper mapper = new ObjectMapper();
		//mapper.setSerializationInclusion(Inclusion.ALWAYS);
		//mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		//mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
		
		/*Json反序列化时忽略多余的属性*/
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		SimpleModule module = new SimpleModule("JsonUtil", new Version(1, 0, 0, null));
		//JavaObject to JSONString
		module.addSerializer(new ToJsonLongSerializer());
		module.addSerializer(new ToJsonSqlTimestampSerializer(DEFAULT_DATETIME_PATTERN));
		module.addSerializer(new ToJsonDateSerializer(DEFAULT_DATETIME_PATTERN));
		module.addSerializer(new ToJsonStringSerializer());
		//JSONString to JavaObject
		module.addDeserializer(Date.class, new CustomDateDeSerializer(Date.class,DEFAULT_FORMATS));
		module.addDeserializer(Timestamp.class, (JsonDeserializer)new CustomSqlTimestampDeSerializer(Timestamp.class,DEFAULT_FORMATS));
		module.addDeserializer(java.sql.Date.class,(JsonDeserializer)new CustomSqlDateDeSerializer(java.sql.Date.class, DEFAULT_FORMATS));
		
		mapper.registerModule(module);
		return mapper;
	}


	private ObjectMapper getMapper() {
		return mapper;
	}

	/**
	 * JSONString to List/Map/POJO(list默认 List<Map>) 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T toObjectFromJson(String json, Class<T> clazz) throws Exception {
		return mapper.readValue(json, clazz);
	}
	
	/**
	 * JSONString  to List/Map/POJO(支持泛型) <br>
	 * 
	 * TypeReference<List<UserTest>>  tr =  new  TypeReference<List<UserTest>>(){};
	 * TypeReference<UserTest>  tr1 =  new  TypeReference<UserTest>(){};
	 * @param json
	 * @param typeRef {@link TypeReference}
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T toObjectFromJson(String json,TypeReference<T> typeRef) throws Exception{
		return mapper.readValue(json, typeRef);
	}

    /**
     * map to Bean 
     * @param fromValue
     * @param toValueType
     * @return
     * @throws Exception
     */
	public static <T> T convertValue(Object fromValue, Class<T> toValueType) throws Exception {
		return mapper.convertValue(fromValue, toValueType);
	}
	
	/**
	 *  map to Bean/ List<Map<String,Object>>  to List<Bean>
	 * @param fromValue
	 * @param toValueTypeRef
	 * @return
	 * @throws Exception
	 */
	public static <T> T convertValue(Object fromValue,TypeReference<T>  toValueTypeRef) throws Exception {
		return mapper.convertValue(fromValue,  toValueTypeRef);
	}
	
	
	public static String pojoToJson(Object value) throws Exception {
		mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		
		return mapper.writeValueAsString(value);
	}
	/**
	 * pojo to json string (值为空的不进行序列化)
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String pojoToJsonFilter(Object value) throws Exception {
		mapper1.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper1.writeValueAsString(value);
	}
	
	
	   //Base by UAP
	   static final ObjectMapper objectMapper;
	   static boolean isPretty = true;

	    static 
	    {
	    	//Base by UAP
	        DefaultSerializerProvider sp = new DefaultSerializerProvider.Impl();
	        objectMapper = new ObjectMapper(null, sp, null);
	        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
	        //objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
	        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	        SimpleModule module = new SimpleModule("myCustomerModule", new Version(1, 0, 0, null));
	        module.addDeserializer(Date.class, new CustomDateDeSerializer(Date.class,DEFAULT_FORMATS));
	        objectMapper.registerModule(module);
	        //HibernateModule module = new HibernateModule();
	        //module.configure(com.fasterxml.jackson.module.hibernate.HibernateModule.Feature.FORCE_LAZY_LOADING, false);
	        objectMapper.registerModule(module);
	    }
	

	
}

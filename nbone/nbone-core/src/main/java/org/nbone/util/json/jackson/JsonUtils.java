package org.nbone.util.json.jackson;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.type.TypeReference;
import org.nbone.constant.DateConstant;

/**
 * base by org.codehaus.jackson.map.ObjectMapper
 * @author thinking  2014-04-26
 * @version 1.0
 * @since 2014-04-26
 */
public class JsonUtils implements DateConstant{
	
	private static JsonUtils jsonUtil;
	private ObjectMapper mapper;
	
	private static ObjectMapper mapper1 = new ObjectMapper();
	
	private JsonUtils(ObjectMapper mapper){
		this.mapper = mapper;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static JsonUtils getInstance() {
		if(jsonUtil == null){
			ObjectMapper mapper = new ObjectMapper();
			
			mapper.setSerializationInclusion(Inclusion.ALWAYS);
			//mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
			//mapper.getSerializerProvider().setNullValueSerializer(new NullValueSerializer());
			
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
			jsonUtil = new JsonUtils(mapper);
		}
		return jsonUtil;
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
		return getInstance().getMapper().readValue(json, clazz);
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
		return getInstance().getMapper().readValue(json, typeRef);
	}

    /**
     * map to Bean 
     * @param fromValue
     * @param toValueType
     * @return
     * @throws Exception
     */
	public static <T> T convertValue(Object fromValue, Class<T> toValueType) throws Exception {
		return getInstance().getMapper().convertValue(fromValue, toValueType);
	}
	
	/**
	 *  map to Bean/ List<Map<String,Object>>  to List<Bean>
	 * @param fromValue
	 * @param toValueTypeRef
	 * @return
	 * @throws Exception
	 */
	public static <T> T convertValue(Object fromValue,TypeReference<T>  toValueTypeRef) throws Exception {
		return getInstance().getMapper().convertValue(fromValue,  toValueTypeRef);
	}
	
	
	public static String pojoToJson(Object value) throws Exception {
		ObjectMapper mapper =  getInstance().getMapper();
		mapper.setSerializationInclusion(Inclusion.ALWAYS);
		return mapper.writeValueAsString(value);
	}
	/**
	 * pojo to json string (值为空的不进行序列化)
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String pojoToJsonFilter(Object value) throws Exception {
		mapper1.setSerializationInclusion(Inclusion.NON_NULL);
		return mapper1.writeValueAsString(value);
	}
	
	
	   //Base by UAP
	   static final ObjectMapper objectMapper;
	   static boolean isPretty = true;

	    static 
	    {
	    	//Base by UAP
	        StdSerializerProvider sp = new StdSerializerProvider();
	        objectMapper = new ObjectMapper(null, sp, null);
	        objectMapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
	        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
	        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	        SimpleModule module = new SimpleModule("myCustomerModule", new Version(1, 0, 0, null));
	        module.addDeserializer(Date.class, new CustomDateDeSerializer(Date.class,DEFAULT_FORMATS));
	        objectMapper.registerModule(module);
	        //HibernateModule module = new HibernateModule();
	        //module.configure(com.fasterxml.jackson.module.hibernate.HibernateModule.Feature.FORCE_LAZY_LOADING, false);
	        objectMapper.registerModule(module);
	    }
	

	
}

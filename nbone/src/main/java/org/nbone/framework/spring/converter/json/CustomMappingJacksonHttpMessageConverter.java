package org.nbone.framework.spring.converter.json;

import java.io.IOException;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;


/**
 * 
 * @author thinking
 * @since 2012-9-25
 */
public class CustomMappingJacksonHttpMessageConverter extends MappingJacksonHttpMessageConverter implements InitializingBean {
	
	private JsonSerializer<?>[] jsonSerializers;

	public CustomMappingJacksonHttpMessageConverter() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		if(jsonSerializers != null && jsonSerializers.length > 0){
			SimpleModule module = new SimpleModule("customMappingJacksonHttpMessageConverter", 
					new Version(1, 0, 0, null));
			for(JsonSerializer<?> serializer : jsonSerializers){
				module.addSerializer(serializer);
			}
			super.getObjectMapper().registerModule(module);
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.http.converter.AbstractHttpMessageConverter#readInternal(java.lang.Class, org.springframework.http.HttpInputMessage)
	 */
	@Override
	protected Object readInternal(Class<? extends Object> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		// TODO Auto-generated method stub
		return super.readInternal(clazz, inputMessage);
	}
	
	public JsonSerializer<?>[] getJsonSerializers() {
		return jsonSerializers;
	}

	public void setJsonSerializers(JsonSerializer<?>[] jsonSerializers) {
		this.jsonSerializers = jsonSerializers;
	}
	
	
	
	
}

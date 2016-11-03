package org.nbone.framework.spring.converter.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;



/**
 * 转换json时将Long类型的数据转换为String类型输出
 * @author thinking
 * @since 2012-9-25
 */
public class LongSerializer extends JsonSerializer<Long> {

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(Long arg0, JsonGenerator arg1, SerializerProvider arg2)
			throws IOException, JsonProcessingException {
		arg1.writeString(String.valueOf(arg0));
	}
	
	@Override
	public Class<Long> handledType() {
		return Long.class;
	}

}

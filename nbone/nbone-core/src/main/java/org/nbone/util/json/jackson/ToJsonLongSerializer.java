package org.nbone.util.json.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;



/**
 * JavaObject to JSON String <br>
 * Long类型的数据转换为String类型输出
 * @author thinking 2012-9-25
 */
public class ToJsonLongSerializer extends JsonSerializer<Long> {

	
	@Override
	public Class<Long> handledType() {
		return Long.class;
	}

	@Override
	public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeString(String.valueOf(value));
	}
	
	

}

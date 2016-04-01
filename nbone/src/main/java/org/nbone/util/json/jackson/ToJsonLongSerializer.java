package org.nbone.util.json.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * JavaObject to JSON String <br>
 * Long类型的数据转换为String类型输出
 * @author thinking 2012-9-25
 */
public class ToJsonLongSerializer extends JsonSerializer<Long> {

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

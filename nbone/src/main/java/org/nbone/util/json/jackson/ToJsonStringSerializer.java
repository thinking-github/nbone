package org.nbone.util.json.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class ToJsonStringSerializer extends JsonSerializer<String> {

	@Override
	public void serialize(String arg0, JsonGenerator arg1,SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		arg1.writeString(arg0);
	}

	@Override
	public Class<String> handledType() {
		return String.class;
	}



}

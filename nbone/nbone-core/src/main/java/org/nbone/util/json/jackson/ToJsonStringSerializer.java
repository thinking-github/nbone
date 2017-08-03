package org.nbone.util.json.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;



public class ToJsonStringSerializer extends JsonSerializer<String> {

	@Override
	public Class<String> handledType() {
		return String.class;
	}

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeString(value);
		
	}



}

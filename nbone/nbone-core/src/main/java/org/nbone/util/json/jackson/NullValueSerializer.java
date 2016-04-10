package org.nbone.util.json.jackson;

import java.io.IOException;
import java.lang.reflect.Type;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.ser.std.SerializerBase;

public class NullValueSerializer extends SerializerBase<Object> {

	protected NullValueSerializer() {
		super(Object.class);
	}

	@Override
	public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		
		//jgen.writeNull();
		jgen.writeString("");
		
	}

	@Override
	public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
		System.out.println("----------------------");
		return super.getSchema(provider, typeHint);
	}
	
	

	

}

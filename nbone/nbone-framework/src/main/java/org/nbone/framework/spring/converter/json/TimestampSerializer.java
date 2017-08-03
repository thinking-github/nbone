package org.nbone.framework.spring.converter.json;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TimestampSerializer extends JsonSerializer<java.sql.Timestamp> {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
	@Override
	public void serialize(Timestamp arg0, JsonGenerator arg1,SerializerProvider arg2)
			throws IOException,JsonProcessingException {
		arg1.writeString(String.valueOf(dateFormat.format(arg0)));
	}
	@Override
	public Class<Timestamp> handledType() {
		return Timestamp.class;
	}
}

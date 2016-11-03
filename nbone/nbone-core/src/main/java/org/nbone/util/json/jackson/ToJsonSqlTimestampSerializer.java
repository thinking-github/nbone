package org.nbone.util.json.jackson;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


/**
 * JavaObject to JSON String <br>
 * Timestamp类型的数据转换为String类型输出
 * @author thinking 2012-9-25
 *
 */
public class ToJsonSqlTimestampSerializer extends JsonSerializer<java.sql.Timestamp> {
	
	private String pattern;
	private SimpleDateFormat dateFormat;
	
	public ToJsonSqlTimestampSerializer(String pattern) {
		this.pattern = pattern;
		dateFormat = new SimpleDateFormat();
	}
	@Override
	public Class<Timestamp> handledType() {
		return Timestamp.class;
	}

	@Override
	public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		dateFormat.applyPattern(pattern);
		gen.writeString(String.valueOf(dateFormat.format(value)));
		
	}
}

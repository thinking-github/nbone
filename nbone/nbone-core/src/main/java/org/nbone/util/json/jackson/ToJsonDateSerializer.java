package org.nbone.util.json.jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;



/**
 * JavaObject to JSON String <br>
 * Date类型的数据转换为String类型输出
 * @author thinking 2012-9-25
 *
 */
public class ToJsonDateSerializer extends JsonSerializer<Date>{
	
	private String pattern;
	private SimpleDateFormat dateFormat;
	
	public ToJsonDateSerializer(String pattern) {
		this.pattern = pattern;
		dateFormat = new SimpleDateFormat();
	}
	
	@Override
	public Class<Date> handledType() {
		return Date.class;
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		dateFormat.applyPattern(pattern);
		gen.writeString(String.valueOf(dateFormat.format(value)));
		
	}
}

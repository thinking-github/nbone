package org.nbone.util.json.jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

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
	public void serialize(Date arg0, JsonGenerator arg1,SerializerProvider arg2)
			throws IOException,JsonProcessingException {
		dateFormat.applyPattern(pattern);
		arg1.writeString(String.valueOf(dateFormat.format(arg0)));
		
	}
	@Override
	public Class<Date> handledType() {
		return Date.class;
	}
}

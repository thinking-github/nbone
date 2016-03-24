package org.nbone.util.json;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

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
	public void serialize(Timestamp arg0, JsonGenerator arg1,SerializerProvider arg2)
			throws IOException,JsonProcessingException {
		dateFormat.applyPattern(pattern);
		arg1.writeString(String.valueOf(dateFormat.format(arg0)));
		
	}
	@Override
	public Class<Timestamp> handledType() {
		return Timestamp.class;
	}
}

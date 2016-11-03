package org.nbone.util.json.jackson;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * reference org.codehaus.jackson <br>
 * JSON String to JavaObject <br>
 * 
 * @author thinking 2012-9-25
 * @see StdDeserializer
 *
 */
public class CustomSqlDateDeSerializer extends CustomAbstractDateDeSerializer<java.sql.Date> {

	private static final long serialVersionUID = 1L;

	protected CustomSqlDateDeSerializer(Class<java.sql.Date> vc, String[] formats) {
		super(vc, formats);
	}

	@Override
	public java.sql.Date deserialize(JsonParser parser, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		Date parseDate = doDeserialize(parser, arg1);
		if (parseDate == null)
			return null;
		return new java.sql.Date(parseDate.getTime());
	}
}

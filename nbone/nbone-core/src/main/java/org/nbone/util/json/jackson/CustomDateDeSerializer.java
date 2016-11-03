package org.nbone.util.json.jackson;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;


/**
 * reference org.codehaus.jackson <br>
 * JSON String to JavaObject <br>
 * 
 * @author thinking 2012-9-25
 * @see StdDeserializer
 *
 */
public class CustomDateDeSerializer extends CustomAbstractDateDeSerializer<Date> {

	private static final long serialVersionUID = 1L;

	protected CustomDateDeSerializer(Class<Date> vc, String[] formats) {
		super(vc, formats);
	}

	public Date deserialize(JsonParser parser, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		return super.deserialize(parser, arg1);
	}

}

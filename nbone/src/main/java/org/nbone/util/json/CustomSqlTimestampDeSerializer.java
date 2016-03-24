package org.nbone.util.json;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
import org.nbone.util.DateFPUtils;
/**
 * reference  org.codehaus.jackson <br>
 * JSON String to JavaObject <br>
 * @author thinking 2012-9-25
 * @see StdDeserializer
 *
 */
public class CustomSqlTimestampDeSerializer extends CustomAbstractDateDeSerializer<Timestamp>{
	
  protected CustomSqlTimestampDeSerializer(Class<Timestamp> vc,String[] formats) {
		super(vc, formats);
	}
 
@Override
public Timestamp deserialize(JsonParser parser, DeserializationContext arg1)
		throws IOException, JsonProcessingException {
	Date  parseDate = doDeserialize(parser, arg1);
	if(parseDate == null) return null;
	return new Timestamp(parseDate.getTime());
}

}

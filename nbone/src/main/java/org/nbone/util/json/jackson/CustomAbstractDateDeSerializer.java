package org.nbone.util.json.jackson;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
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
public abstract class CustomAbstractDateDeSerializer<T extends Date> extends StdDeserializer<Date>
{
  protected static  Log logger = LogFactory.getLog(CustomAbstractDateDeSerializer.class);
  private String[] formats;
  
  protected CustomAbstractDateDeSerializer(Class<? extends Date> vc,String[] formats) {
    super(vc);
    this.formats = formats;
  }

  public T deserialize(JsonParser parser, DeserializationContext arg1)throws IOException, JsonProcessingException{
	  Date  parseDate =  doDeserialize(parser, arg1);
    return (T) parseDate;
  }
  
  protected Date doDeserialize(JsonParser parser, DeserializationContext arg1) throws JsonParseException, IOException{
	  String value = parser.getText();
	  Date parseDate  = DateFPUtils.parseDate(value, formats);
	return parseDate;
  }
}

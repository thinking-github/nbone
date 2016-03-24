package org.nbone.util.json;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdDeserializer;
/**
 * reference  org.codehaus.jackson <br>
 * JSON String to JavaObject <br>
 * @author thinking 2012-9-25
 * @see StdDeserializer
 *
 */
public  class CustomDateDeSerializer extends CustomAbstractDateDeSerializer<Date>{
  
  protected CustomDateDeSerializer(Class<Date> vc,String[] formats) {
    super(vc, formats);
  }

  public Date deserialize(JsonParser parser, DeserializationContext arg1)throws IOException, JsonProcessingException
 {
    return super.deserialize(parser, arg1);
  }
  
}

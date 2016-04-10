package org.nbone.util.json;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.object.AbstractObjectMorpher;
import net.sf.ezmorph.object.DateMorpher;

import org.nbone.util.DateFPUtils;
/**
 * JSONString to {@link java.sql.Timestamp}  convert <br>
 * base by net.sf.ezmorph.object.DateMorpher
 * @author thinking
 * @see EzmorphDateMorpher
 */
public class EzmorphSqlTimestampMorpher  extends EzmorphDateMorpher  {
	
	  private Timestamp defaultValue;

	  public EzmorphSqlTimestampMorpher(String[] formats){
		  super(formats);
	  }

	  public Object morph(Object value){
	    Date parseDate   = this.doMorph(value);
	    if(parseDate == null) return null;
	    return new Timestamp(parseDate.getTime());
	  }
		   
      @Override
	  public Class<?> morphsTo(){
	    return Timestamp.class;
	  }
}

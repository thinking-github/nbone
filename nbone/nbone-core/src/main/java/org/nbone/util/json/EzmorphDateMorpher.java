package org.nbone.util.json;

import java.util.Date;
import java.util.Locale;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.object.AbstractObjectMorpher;
import net.sf.ezmorph.object.DateMorpher;

import org.nbone.util.DateFPUtils;
/**
 * 
 * JSONString to {@link Date}  convert(String date / long date 1452223274050) <br>
 * base by net.sf.ezmorph.object.DateMorpher
 * @author thinking
 * @see DateMorpher
 *
 */
public class EzmorphDateMorpher  extends AbstractObjectMorpher  {
	
	  private Date defaultValue;
	  private String[] formats;
	  private boolean lenient;
	  private Locale locale;

	  public EzmorphDateMorpher(String[] formats)
	  {
	    this(formats, Locale.getDefault(), false);
	  }

	  public EzmorphDateMorpher(String[] formats, Locale locale)
	  {
	    this(formats, locale, false);
	  }
	  
	  public EzmorphDateMorpher(String[] formats, boolean lenient)
	  {
	    this(formats, Locale.getDefault(), lenient);
	  }

	  public EzmorphDateMorpher(String[] formats, Date defaultValue)
	  {
	    this(formats, defaultValue, Locale.getDefault(), false);
	  }
	  
	  public EzmorphDateMorpher(String[] formats, Date defaultValue, Locale locale, boolean lenient)
	  {
	    super(true);
	    if ((formats == null) || (formats.length == 0)) {
	      throw new MorphException("invalid array of formats");
	    }

	    this.formats = formats;

	    if (locale == null)
	      this.locale = Locale.getDefault();
	    else {
	      this.locale = locale;
	    }

	    this.lenient = lenient;
	    setDefaultValue(defaultValue);
	  }

	  public EzmorphDateMorpher(String[] formats, Locale locale, boolean lenient)
	  {
	    if ((formats == null) || (formats.length == 0)) {
	      throw new MorphException("invalid array of formats");
	    }

	    this.formats = formats;

	    if (locale == null)
	      this.locale = Locale.getDefault();
	    else {
	      this.locale = locale;
	    }

	    this.lenient = lenient;
	  }


	  public Object morph(Object value)
	  {
	    Date parseDate   = this.doMorph(value);
	    return parseDate;

	  }
	  
	  protected Date doMorph(Object value){
		    if (value == null || value.toString().trim().equals("")) {
			      return null;
			}
		    if(value instanceof Date){
		    	Date date = (Date) value;
		    	return date;
		    }
		    if (Date.class.isAssignableFrom(value.getClass())) {
		      return (Date)value;
		    }

		    if (!supports(value.getClass())) {
		      throw new MorphException(value.getClass() + " is not supported");
		    }
		    String strValue = String.valueOf(value);
		    //string to date 
		    Date parseDate  = DateFPUtils.parseDate(strValue, formats, locale, lenient);
	        if(parseDate != null){
	        	return parseDate;
	        }
		    if (isUseDefault()) {
		      return this.defaultValue;
		    }
		    throw new MorphException("Unable to parse the date " + value);
	  }
      @Override
	  public Class<?> morphsTo()
	  {
	    return Date.class;
	  }
      
	  public Date getDefaultValue()
	  {
	    return (Date)this.defaultValue.clone();
	  }
	  
	  public void setDefaultValue(Date defaultValue)
	  {
	    this.defaultValue = ((Date)defaultValue.clone());
	  }

	  //String date / long date 1452223274050
	  public boolean supports(Class clazz)
	  {
	    return String.class.isAssignableFrom(clazz) || 
	    	   Long.class.isAssignableFrom(clazz)   || 
	    	   Integer.class.isAssignableFrom(clazz)|| 
	    	   long.class.isAssignableFrom(clazz)   ||
	    	   int.class.isAssignableFrom(clazz)  ;
	  }

}

package org.nbone.util.json;

/**
 * JSONString to {@link java.sql.Date}  convert <br>
 * @author thinking
 * @see EzmorphDateMorpher
 *
 */
public class EzmorphSqlDateMorpher extends EzmorphDateMorpher {

	private java.sql.Date defaultValue;
	
	public EzmorphSqlDateMorpher(String[] formats) {
		super(formats);
	}

	@Override
	public Object morph(Object value) {
		    java.util.Date parseDate   = super.doMorph(value);
		    if(parseDate == null) return null;
		    return new java.sql.Date(parseDate.getTime());
	}

	@Override
	public Class<?> morphsTo() {
		return java.sql.Date.class;
	}

}

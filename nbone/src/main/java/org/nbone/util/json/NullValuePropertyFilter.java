package org.nbone.util.json;

/**
 * java object to json 过滤null值
 * @author thinking
 * @since 2016-04-04
 *
 */
public class NullValuePropertyFilter implements net.sf.json.util.PropertyFilter {

	@Override
	public boolean apply(Object source, String name, Object value) {
		
		if(value == null || String.valueOf(value).equals("")){
			return true;
		}
		
		
		return false;
	}

}

package org.nbone.util.text;

import java.text.FieldPosition;
import java.util.Map;

import jodd.bean.BeanTemplateParser;
import jodd.util.StringTemplateParser;
import jodd.util.StringTemplateParser.MacroResolver;
/**
 * @author thinking
 * @version 1.0.1
 * @since 2016-08-12
 */
public class JoddFormat  extends BaseFormat{
	
	private static final long serialVersionUID = -4242969107880533215L;

	public JoddFormat(String pattern) {
		super(pattern);
	}

	public static String format(String pattern,Object dataModel) {
		 
		 JoddFormat joddFormat = new JoddFormat(pattern);
		return joddFormat.format(dataModel);
	 }
	 

	@SuppressWarnings("unchecked")
	@Override
	public StringBuffer format(Object dataModel, StringBuffer toAppendTo, FieldPosition pos) {
		StringBuffer out  = new StringBuffer();
		if(dataModel instanceof Map){
			final Map<String, ?> map = (Map<String, ?>) dataModel;
			 
			 StringTemplateParser stp = new StringTemplateParser();
			    String result = stp.parse(pattern, new MacroResolver() {
			        public String resolve(String macroName) {
			            return map.get(macroName)+"";
			        }
			    });
			    
			    out.append(result);
		}else{
			BeanTemplateParser btp = new BeanTemplateParser();
		    String result = btp.parse(pattern, dataModel);
		    out.append(result);
		}
		return out;
	}


}

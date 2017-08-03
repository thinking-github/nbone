package org.nbone.util.text;

import java.io.StringWriter;
import java.io.Writer;
import java.text.FieldPosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.nbone.constants.CharsetConstant;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author  thinking
 * @version 1.0 
 * @since 2015-12-12
 * @see org.springframework.ui.freemarker.FreeMarkerTemplateUtils
 */
public class FreemarkerFormat extends BaseFormat implements CharsetConstant {
	
	private static final long serialVersionUID = 3343475495847994770L;
	
	private static Configuration config; 
	private static StringTemplateLoader stringLoader; 
	
    public FreemarkerFormat(String pattern) {
    	super(pattern);
    }
	
    /**
     * 
     * @param pattern 模板字符串
     * @param dataModel 动态绑定变量
     * @return
     */
    public static String format(String pattern, Object dataModel) {
    	FreemarkerFormat temp = new FreemarkerFormat(pattern);
    	return temp.format(dataModel);
    }

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		StringBuffer out  = new StringBuffer();
		try {
			
			 stringLoader.putTemplate(FreemarkerFormat.class.getName(), this.pattern);
			 Template template = config.getTemplate(FreemarkerFormat.class.getName(), CharsetConstant.CHARSET_UTF8);
			 Writer writer = new StringWriter();
			 template.process(obj, writer);
			 
			 out.append(writer.toString());
			 
		} catch (Exception e) {;
			e.printStackTrace();
		} 
		return out;
	}

	
	static {
		 Configuration cfg = new Configuration();   
		 cfg.setDefaultEncoding(CharsetConstant.CHARSET_UTF8);   
		 StringTemplateLoader stringLoader = new StringTemplateLoader();
		 cfg.setTemplateLoader(stringLoader);
		 
		 
		 FreemarkerFormat.config = cfg;
		 FreemarkerFormat.stringLoader = stringLoader;
	}
	

}

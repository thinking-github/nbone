package org.nbone.util.text;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.nbone.constants.CharsetConstant;

import java.io.StringWriter;
import java.io.Writer;
import java.text.FieldPosition;

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
    	FreemarkerFormat freemarker = new FreemarkerFormat(pattern);
    	return freemarker.format(dataModel,null);
    }

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		StringBuffer out = new StringBuffer();
		try {

			Object object = stringLoader.findTemplateSource(this.pattern);
			if (object == null) {
				stringLoader.putTemplate(this.pattern, this.pattern);
			}

			Template template = config.getTemplate(this.pattern, CharsetConstant.CHARSET_UTF8);
			Writer writer = new StringWriter();
			template.process(obj, writer);

			out.append(writer.toString());

			if (toAppendTo != null) {
				out.append(toAppendTo);
			}

		} catch (Exception e) {
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

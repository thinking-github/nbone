package org.nbone.framework.spring.web.method.annotation;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author thinking
 * @version 1.0 
 * @since 2015-08-12
 * @see ItemRequestResponseBodyMethodProcessor
 * @see RawResponseBodyMethodProcessor
 */
public interface JsonProcessor {
	
	  
	  MappingJackson2HttpMessageConverter json2MessageConverter =  new MappingJackson2HttpMessageConverter();
	  
	
	

}

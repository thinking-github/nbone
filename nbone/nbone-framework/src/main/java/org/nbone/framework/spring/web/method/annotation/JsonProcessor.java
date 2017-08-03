package org.nbone.framework.spring.web.method.annotation;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * @author thinking
 * @version 1.0 
 * @since 2015-08-12
 * @see ItemRequestResponseBodyMethodProcessor
 * @see RawResponseBodyMethodProcessor
 */
@SuppressWarnings("unchecked")
public class JsonProcessor {
	
	 /*默认的Json 转换器*/
	 public final static HttpMessageConverter<?> json2MessageConverter;
	  
	  //HttpMessageConverter<?> fastJsonConverter =  new FastJsonHttpMessageConverter();
	 public final static List<?>  msgConverters ;
	  
	  /*@SuppressWarnings("unchecked")
	  List<?>  msgConverters1  = Lists.newArrayList(json2MessageConverter);
	  
	  List<?>  msgConverters2  = ImmutableList.of(json2MessageConverter);
	  
	  */
	  static {
		  MappingJackson2HttpMessageConverter json= new MappingJackson2HttpMessageConverter();
		  json2MessageConverter = json;
		  
		  msgConverters = Arrays.asList(json2MessageConverter);
		  
	  }
	  
	  
	  
	  
	  
	
	

}

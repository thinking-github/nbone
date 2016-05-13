package org.nbone.framework.spring.web.method.annotation;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * @author thinking
 * @version 1.0 
 * @since 2015-08-12
 * @see ItemRequestResponseBodyMethodProcessor
 * @see RawResponseBodyMethodProcessor
 */
public interface JsonProcessor {
	
	 /*默认的Json 转换器*/
	  HttpMessageConverter<?> json2MessageConverter =  new MappingJackson2HttpMessageConverter();
	  
	  //HttpMessageConverter<?> fastJsonConverter =  new FastJsonHttpMessageConverter();
	  
	  @SuppressWarnings("unchecked")
	  List<?>  msgConverters  = Arrays.asList(json2MessageConverter);
	  
	  /*@SuppressWarnings("unchecked")
	  List<?>  msgConverters1  = Lists.newArrayList(json2MessageConverter);
	  
	  List<?>  msgConverters2  = ImmutableList.of(json2MessageConverter);
	  
	  */
	  
	  
	  
	  
	  
	
	

}

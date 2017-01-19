package org.nbone.framework.spring.web.client;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.nbone.constants.CharsetConstant;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
/**
 * RestTemplate 存在乱码问题
 * @author thinking
 * @since 2016-11-4
 *
 */
public class RestTemplateFactory {
	
	public static RestTemplate getRestTemplate(){
		return getRestTemplate(CharsetConstant.UTF_8);
	}
	
	public static RestTemplate getRestTemplate(Charset charset){
		if(charset == null){
			charset = CharsetConstant.UTF_8;
		}
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new ByteArrayHttpMessageConverter());
    	messageConverters.add(new StringHttpMessageConverter(charset));
    	messageConverters.add(new MappingJackson2HttpMessageConverter());
    	
    	
    	RestTemplate rest = new RestTemplate(messageConverters);
		return rest;
	}

}

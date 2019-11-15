package org.nbone.framework.spring.web.client;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.nbone.constants.CharsetConstant;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 存在乱码问题
 *
 * @author thinking
 * @since 2016-11-4
 */
public class RestTemplateFactory {

  /*  static {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity = new HttpEntity<>(req,requestHeaders);
    }
*/

    public static RestTemplate getRestTemplate(int timeout) {
        return getRestTemplate(CharsetConstant.UTF_8, timeout, timeout);
    }

    public static RestTemplate getRestTemplate(Charset charset, int connectTimeout, int readTimeout) {
        if (charset == null) {
            charset = CharsetConstant.UTF_8;
        }
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(charset));
        messageConverters.add(new MappingJackson2HttpMessageConverter());

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        RestTemplate restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

}

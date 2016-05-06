package org.nbone.framework.spring.web.method.annotation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.nbone.framework.spring.web.MapWapper;
import org.nbone.framework.spring.web.bind.annotation.JsonRequestParam;
import org.nbone.util.json.jackson.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

/**
 * 
 * 解析请求参数json字符串 
 * 
 * @author thinking
 * @version 1.0
 * @since spring 3.1
 */
public class JsonRequestParamMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver implements WebArgumentResolver {

    private ObjectMapper mapper = JsonUtils.getInstance();
    
	public JsonRequestParamMethodArgumentResolver() {
		super(null);
	}
	
	public boolean supportsParameter(MethodParameter parameter) {

		return parameter.hasParameterAnnotation(JsonRequestParam.class);
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		JsonRequestParam annotation = parameter.getParameterAnnotation(JsonRequestParam.class);
		return new RequestJsonParamNamedValueInfo(annotation); 
				
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
		String[] paramValues = webRequest.getParameterValues(name);
		Class<?> paramType = parameter.getParameterType();
        if (paramValues == null) {
            return null;
            
        } 
        
        try {
            if(paramValues.length == 1) {
                String text = paramValues[0]; 
                Type type = parameter.getGenericParameterType();

                if(MapWapper.class.isAssignableFrom(paramType)) {
                    MapWapper<?, ?> jsonMap = (MapWapper<?, ?>) paramType.newInstance();
                    
                    MapType mapType = (MapType) getJavaType(HashMap.class);
                    
                    if(type instanceof ParameterizedType) {
                        mapType = (MapType) mapType.narrowKey((Class<?>)((ParameterizedType)type).getActualTypeArguments()[0]);
                        mapType = (MapType) mapType.narrowContentsBy((Class<?>)((ParameterizedType)type).getActualTypeArguments()[1]); 
                    }
                    jsonMap.setInnerMap(mapper.<Map>readValue(text, mapType));
                    return jsonMap;
                }
                
                JavaType javaType = getJavaType(paramType);


                if(Collection.class.isAssignableFrom(paramType)) {
                    javaType = javaType.narrowContentsBy((Class<?>)((ParameterizedType)type).getActualTypeArguments()[0]);                        
                }
                //XXX:thinking
                if(!StringUtils.hasText(text)){
            		
                	if(Collection.class.isAssignableFrom(paramType)){
                		text = "[]";
                	}else{
                		text = "{}";
                	}
                	//throw new JsonMappingException("request json parameter '" + name + "' is null .thinking");
                }

                return mapper.readValue(text, javaType);
            }
            
        } catch (Exception e) {
            throw new ServletRequestBindingException("Could not read request json parameter .thinking", e);
        }

        throw new UnsupportedOperationException(
                "too many request json parameter '" + name + "' for method parameter type [" + paramType + "], only support one json parameter");
	}
	
	protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.type(clazz);
    }

	@Override
	protected void handleMissingValue(String paramName, MethodParameter parameter) throws ServletException {
	    String paramType = parameter.getParameterType().getName();
	    throw new ServletRequestBindingException(
                "Missing request json parameter '" + paramName + "' for method parameter type [" + paramType + "]");
	}

	
	
	private class RequestJsonParamNamedValueInfo extends NamedValueInfo {

		private RequestJsonParamNamedValueInfo() {
			super("", false, null);
		}
		
		private RequestJsonParamNamedValueInfo(JsonRequestParam annotation) {
			super(annotation.value(), annotation.required(), null);
		}
	}

	/**
	 * spring 3.1之前
	 */
    @Override
    public Object resolveArgument(MethodParameter parameter, NativeWebRequest request) throws Exception {
        if(!supportsParameter(parameter)) {
            return WebArgumentResolver.UNRESOLVED;
        }
        return resolveArgument(parameter, null, request, null);
    }
}
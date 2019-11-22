package org.springframework.web.servlet.mvc.method.annotation;

import org.nbone.framework.spring.web.bind.annotation.ResultResponseBody;
import org.nbone.mvc.rest.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-19
 */
@ControllerAdvice
public class ResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    public final static String LOG_ID_HEADER_NAME = "X-LogId";

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.hasMethodAnnotation(ResultResponseBody.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {


        Object result = doBodyWriteInternal(body, returnType, selectedContentType, selectedConverterType, request, response);
        return result;
    }

    protected Object doBodyWriteInternal(Object body, MethodParameter returnType,
                                         MediaType selectedContentType, Class selectedConverterType,
                                         ServerHttpRequest request, ServerHttpResponse response) {
        String logId = getLogId(request, response);
        if (body instanceof ApiResponse) {
            ApiResponse apiResponse = (ApiResponse) body;
            if(StringUtils.isEmpty(apiResponse.getLogId())){
                apiResponse.setLogId(logId);
            }
            return body;
        }
        return ApiResponse.success(body).logId(logId);
    }


    private String getLogId(ServerHttpRequest request, ServerHttpResponse response) {
        String logId = request.getHeaders().getFirst(LOG_ID_HEADER_NAME);
        if (StringUtils.hasLength(logId)) {
            return logId;
        }
        return response.getHeaders().getFirst(LOG_ID_HEADER_NAME);

    }

}

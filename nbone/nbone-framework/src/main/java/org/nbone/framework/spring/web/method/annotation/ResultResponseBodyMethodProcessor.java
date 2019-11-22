package org.nbone.framework.spring.web.method.annotation;

import org.nbone.framework.spring.web.bind.annotation.ResultResponseBody;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-11-19
 * @see  ResultResponseBody
 */
public class ResultResponseBodyMethodProcessor implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(ResultResponseBody.class) != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

        System.out.println("------------ResultResponseBodyMethodProcessor");


    }
}

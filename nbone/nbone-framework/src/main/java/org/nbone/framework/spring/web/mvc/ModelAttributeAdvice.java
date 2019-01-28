package org.nbone.framework.spring.web.mvc;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *  @ModelAttribute methods --> @InitBinder methods --> Controller methods <br>
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2018/12/27
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
 * @see org.springframework.web.bind.annotation.support.HandlerMethodResolver
 */
@ControllerAdvice
public class ModelAttributeAdvice {

    /**
     *
     * @param request
     * @param response
     * @see  org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#invokeHandlerMethod(HttpServletRequest, HttpServletResponse, HandlerMethod)
     * @see  org.springframework.web.method.annotation.ModelFactory#initModel(NativeWebRequest, ModelAndViewContainer, HandlerMethod)
     */
    @ModelAttribute
    protected void modelAttribute(HttpServletRequest request, HttpServletResponse response) {


    }
}

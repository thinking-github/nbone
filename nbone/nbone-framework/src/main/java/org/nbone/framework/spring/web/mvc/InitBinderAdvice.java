package org.nbone.framework.spring.web.mvc;

import org.nbone.util.DateFPUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2018/12/27
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
 * @see org.springframework.web.bind.annotation.support.HandlerMethodResolver
 */
@ControllerAdvice
public class InitBinderAdvice {
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateFPUtils.parseDateMultiplePattern(text));
            }
        });

    }

}

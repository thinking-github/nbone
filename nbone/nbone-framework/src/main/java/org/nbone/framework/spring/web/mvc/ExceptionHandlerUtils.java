package org.nbone.framework.spring.web.mvc;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;


/**
 * @author chenyicheng
 * @version 1.0
 * @since 2018/12/27
 */
public class ExceptionHandlerUtils {


    /**
     * 解析 @Valid 的参数验证异常的消息
     * @param ex
     * @return
     */
    public static String getMessage(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();
        String msg = result.getAllErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.joining(","));

        return  msg;
    }

    public static String getMessage(BindException ex) {
        BindingResult result = ex.getBindingResult();
        return  getMessage(result);
    }


    public static String getMessage(BindingResult result) {
        if(result.hasErrors()){
            String msg = result.getAllErrors()
                    .stream()
                    .map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(","));
            return msg;
        }

        return  null;
    }


}

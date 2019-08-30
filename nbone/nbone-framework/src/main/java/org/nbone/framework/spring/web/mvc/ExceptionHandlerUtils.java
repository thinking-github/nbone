package org.nbone.framework.spring.web.mvc;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author chenyicheng
 * @version 1.0
 * @since 2018/12/27
 */
public class ExceptionHandlerUtils {


    /**
     * 解析 @Valid 的参数验证异常的消息
     *
     * @param ex
     * @return
     */
    public static String getMessage(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();
        String msg = result.getAllErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.joining(","));

        return msg;
    }

    public static String getMessage(BindException ex) {
        BindingResult result = ex.getBindingResult();
        return getMessage(result);
    }


    public static String getMessage(BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            int count = 0;
            StringBuilder message = new StringBuilder();
            for (ObjectError error : errors) {
                String msg;
                String errorMessage = error.getDefaultMessage();
                errorMessage = errorMessage.replaceAll("java.lang.", "");
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    msg = String.format("[%s = %s] %s", fieldError.getField(), fieldError.getRejectedValue(), errorMessage);
                } else {
                    msg = String.format("[%s] %s", error.getObjectName(), errorMessage);
                }
                if (count > 0) {
                    message.append(",");
                }
                message.append(msg);

                count++;
            }
            return message.toString();
        }

        return null;
    }


}

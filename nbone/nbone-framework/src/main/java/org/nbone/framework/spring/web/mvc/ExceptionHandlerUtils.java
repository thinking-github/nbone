package org.nbone.framework.spring.web.mvc;

import org.nbone.core.exception.ExceptionUtils;
import org.nbone.core.exception.InvalidArgumentException;
import org.nbone.core.exception.InvalidStateException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.nbone.web.util.RequestUtils.isDebug;
import static org.nbone.web.util.RequestUtils.isTrace;


/**
 * @author chenyicheng
 * @version 1.0
 * @since 2018/12/27
 */
public class ExceptionHandlerUtils {

    public final static String[] JAVA_LONG = {"java.lang.", "java.net."};

    /**
     * 解析 @Valid 的参数验证异常的消息
     *
     * @param ex
     * @return
     */
    public static String getMessage(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();

       /* String msg = result.getAllErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.joining(","));*/

        return getMessage(result);
    }

    public static String getMessage(BindException ex) {
        BindingResult result = ex.getBindingResult();
        return getMessage(result);
    }

    public static String getMessage(HttpServletRequest request, Exception ex) {
        if (isDebug(request) || isTrace(request)) {
            return ExceptionUtils.getStackTrace(ex);
        }
        if (ex instanceof InvalidArgumentException || ex instanceof InvalidStateException) {
            return ex.getMessage();
        }
        return getMessage(ex);
    }

    public static String getMessage(Exception ex) {
        String message = ex.getMessage();
        if (message == null) {
            return ex.getClass().getName() + ": message = null";
        }
        if (ex instanceof InvalidArgumentException || ex instanceof InvalidStateException) {
            return message;
        }
        if (ex instanceof IllegalArgumentException || ex instanceof IllegalStateException) {
            return message;
        }
        if (message.length() <= 128) {
            if (message.contains("Exception")) {
                return message;
            }
            return ex.getClass().getSimpleName() + ": " + message;
        } else {
            for (String regex : JAVA_LONG) {
                message = message.replaceAll(regex, "");
            }

        }
        return message;
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
                    String fieldName = fieldError.getField();
                    // Column 'type' cannot be null
                    Object value = fieldError.getRejectedValue();
                    if(value != null && value.getClass().isArray()){
                        value = Arrays.toString((Object[]) value);
                    }
                    msg = String.format("field '%s' %s [%s = %s]", fieldName, errorMessage, fieldName, value);
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

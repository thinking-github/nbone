package org.nbone.framework.spring.web.mvc;

import org.nbone.core.exception.ExceptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2018/12/27
 */
@RestController
@ControllerAdvice
public class ExceptionHandlerAdvice {

    Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    private int errorCode = 0;

    public ExceptionHandlerAdvice() {
    }

    public ExceptionHandlerAdvice(int errorCode) {
        this.errorCode = errorCode;
    }


    // 非法参数异常/非法状态异常
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object baseIllegelException(Exception ex, WebRequest req) {
        logger.error("baseIllegelException:", ex);
        return new ExceptionInfo(errorCode, ex.getMessage(), ex);
    }


    /**
     * @Valid 的参数验证异常处理
     *
     * @param ex
     * @param req
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest req) {
        logger.error("Bad argument:", ex);

        String msg = ExceptionHandlerUtils.getMessage(ex);

        return new ExceptionInfo(errorCode, msg, ex);
    }
}

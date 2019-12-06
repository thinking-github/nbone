package org.nbone.framework.spring.web.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.nbone.core.exception.*;
import org.nbone.framework.spring.web.filter.RequestIdFilter;
import org.nbone.framework.spring.web.log.domain.ErrorLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2018/12/27
 */
@RestController
@ControllerAdvice
public class ExceptionHandlerAdvice {

    Logger logger = LoggerFactory.getLogger("exceptionHandlerAdvice");

    /**
     * 全局错误代码
     */
    private int errorCode = 0;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    /**
     *  persistence; persistent; Persist; persisted;
     */
    private boolean persistence = true;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    public ExceptionHandlerAdvice() {
    }

    public ExceptionHandlerAdvice(int errorCode) {
        this.errorCode = errorCode;
    }

    public ExceptionHandlerAdvice(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ExceptionHandlerAdvice(int errorCode, JdbcTemplate jdbcTemplate) {
        this.errorCode = errorCode;
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isPersistence() {
        return persistence;
    }

    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }

    /**
     * 非法参数异常/非法状态异常
     */
    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public Object baseIllegalException(Exception ex, HttpServletRequest req, HttpServletResponse response) {
        String msg = ExceptionHandlerUtils.getMessage(req,ex);
        return exception(ex,msg,req,response);
    }

    @ExceptionHandler(value = {InvalidArgumentException.class, InvalidStateException.class})
    public Object invalidArgumentException(Exception ex, HttpServletRequest req, HttpServletResponse response) {
        String msg = ExceptionHandlerUtils.getMessage(req,ex);
        return exception(ex,msg,req,response);
    }


    /**
     * @param ex
     * @param req
     * @return
     * @Valid 的参数验证异常处理
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object bindException(BindException ex, HttpServletRequest req, HttpServletResponse response) {
        String msg = ExceptionHandlerUtils.getMessage(ex);
        return exception(ex,msg,req,response);
    }

    /**
     *  请求参数绑定异常<li>  ServletRequestBindingException [MissingServletRequestParameterException,
     *                                                      MissingPathVariableException,
     *                                                      UnsatisfiedServletRequestParameterException]
     *                <li> HttpRequestMethodNotSupportedException
     *                <li> MissingServletRequestPartException
     *                <li> NoHandlerFoundException
     *                <li> HttpMediaTypeException[HttpMediaTypeNotSupportedException,HttpMediaTypeNotAcceptableException]
     * @param ex
     * @param req
     * @param response
     * @return
     */
    @ExceptionHandler(value = ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object requestException(ServletException ex, HttpServletRequest req, HttpServletResponse response) {
        String msg = ExceptionHandlerUtils.getMessage(req,ex);
        return exception(ex,msg,req,response);
    }
    @ExceptionHandler(value = ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object requestBindingException(ServletRequestBindingException ex, HttpServletRequest req, HttpServletResponse response) {
        return  requestException(ex,req,response);
    }
    /**
     * @param ex
     * @param req
     * @return
     * @Valid 的参数验证异常处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Object methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest req, HttpServletResponse response) {
        String msg = ExceptionHandlerUtils.getMessage(ex);
        return exception(ex,msg,false,req,response);
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    @ResponseStatus(HttpStatus.OK)
    public Object jsonException(JsonProcessingException ex, HttpServletRequest req, HttpServletResponse response) {
        String msg = ExceptionHandlerUtils.getMessage(req,ex);
        return exception(ex,msg,req,response);
    }

    @ExceptionHandler(value = {ModuleSystemException.class, SubSystemException.class})
    @ResponseStatus(HttpStatus.OK)
    public Object subSystemException(Exception ex, HttpServletRequest req, HttpServletResponse response) {
        String msg = ExceptionHandlerUtils.getMessage(req,ex);
        return exception(ex, msg, req, response);
    }

    protected ExceptionInfo exception(Exception ex, String message, HttpServletRequest req, HttpServletResponse response) {
        return exception(ex,message,true,req,response);
    }

    protected ExceptionInfo exception(Exception ex, String message, boolean recordStack,
                                      HttpServletRequest req, HttpServletResponse response) {
        if (ex instanceof IllegalArgumentException
                || ex instanceof InvalidArgumentException
                || ex instanceof InvalidStateException || !recordStack) {
            logger.error("Bad request argument:[" + req.getRequestURI() + "] " + ex.getMessage());
        } else {
            logger.error("Bad request argument:[" + req.getRequestURI() + "]", ex);
        }

        if (message == null) {
            message = ex.getMessage();
        }
        String msg = message;

        String requestId = getRequestId(req);
        ayncErrorLog(ex, msg, req, response);
        return new ExceptionInfo(errorCode, msg, ex).requestId(requestId);
    }



    protected String getRequestId(WebRequest request) {
        String requestId = (String) request.getAttribute(RequestIdFilter.REQUEST_ID, WebRequest.SCOPE_REQUEST);
        return requestId;
    }

    protected String getRequestId(HttpServletRequest request) {
        String requestId = (String) request.getAttribute(RequestIdFilter.REQUEST_ID);
        return requestId;
    }


    /**
     * 持久化错误日志
     *
     * @param ex       Exception
     * @param message  Exception message
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    protected void errorLog(Exception ex, String message, HttpServletRequest request, HttpServletResponse response) {
        if (jdbcTemplate != null && persistence) {
            ErrorLog errorLog = new ErrorLog();
            if (message == null) {
                message = ex.getMessage();
            }

            errorLog.setRequestId(getRequestId(request));
            errorLog.setRemoteHost(request.getRemoteHost());
            errorLog.setUserName(request.getRemoteUser());
            errorLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
            errorLog.setMethod(request.getMethod());
            String query = request.getRequestURI();
            String q = request.getQueryString();
            if (q != null) {
                query = query + "?" + q;
            }
            errorLog.setQuery(query);
            errorLog.setStatus(response.getStatus());
            errorLog.setVirtualHost(request.getServerName());

            String referer = request.getHeader("referer");
            String userAgent = request.getHeader("user-agent");
            errorLog.setReferer(referer);
            errorLog.setUserAgent(userAgent);

            errorLog.setErrorMessage(message);
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            errorLog.setStackTrace(stackTrace);

            String insert = String.format(ErrorLog.INSERT_SQL, errorLog.getTableName());
            jdbcTemplate.update(insert, errorLog.getObjects());

        }
    }

    protected void ayncErrorLog(Exception ex, String message, HttpServletRequest request, HttpServletResponse response) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                errorLog(ex, message, request, response);
            }
        });
    }

    protected void ayncErrorLog(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ayncErrorLog(ex, ex.getMessage(), request, response);
    }


}

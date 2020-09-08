package org.nbone.mvc.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * @author thinking
 * @version 1.0
 * @since 2015年12月12日下午1:45:26
 * <p>
 * //@see org.apache.shiro.authz.Authorizer
 */
public interface AuthenticationService<T> {


    /**
     * 用户是否登录检测
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return userId, 如果大于0表示已登录，等于0表示未登录，返回userId方便记录用户操作日志
     */
    long requiredUser(HttpServletRequest request, HttpServletResponse response);

    /**
     * 用户是否登录检测
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param annotation 访问限制注解
     * @param <T>        返回当前用户实体
     * @return
     */
    <T> T checkSession(HttpServletRequest request, HttpServletResponse response, Annotation annotation);
}

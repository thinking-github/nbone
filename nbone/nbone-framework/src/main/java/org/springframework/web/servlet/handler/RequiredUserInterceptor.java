package org.springframework.web.servlet.handler;

import org.nbone.mvc.service.AuthenticationService;
import org.nbone.security.access.annotation.RequiredUser;
import org.nbone.web.SuperHttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * 每次访问必须含有用户信息 (必须登录)
 *
 * @author thinking
 * @version 1.0
 * @see UserRoleAuthorizationInterceptor
 * @since 2019-12-24
 */
public class RequiredUserInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequiredUserInterceptor.class);
    /**
     * debug
     */
    private boolean enableDebug;
    /**
     * appId request Parameter name
     */
    private String appIdName = "appId";

    /**
     * api request Parameter name
     */
    private String apiVersionName = "Api-Version";

    /**
     * 当前用户检测通过后存储的名称
     */
    private String attributeName = "accountInfo";

    /**
     * 支持自定义注解
     */
    private Class<? extends Annotation> annotationType = RequiredUser.class;

    /**
     * 验证用户服务
     */
    private AuthenticationService authenticationService;

    public RequiredUserInterceptor() {
    }

    public RequiredUserInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Annotation annotation = handlerMethod.getMethodAnnotation(annotationType);
        if (annotation == null) {
            annotation = handlerMethod.getBeanType().getAnnotation(annotationType);
        }

        //RequiredUser Annotation not exist break
        if (annotation == null) {
            return true;
        }
        String appId = ServletRequestUtils.getStringParameter(request, appIdName);
        // debug mode
        if (enableDebug) {
            boolean debug = ServletRequestUtils.getBooleanParameter(request, "debug", false);
            if (debug) {
                String userId = request.getHeader("uid");
                if (StringUtils.isEmpty(userId)) {
                    userId = request.getParameter("uid");
                }
                if (StringUtils.isEmpty(userId)) {
                    SuperHttpServlet.sendJson(response, 400, "debug mode request uid parameter must not be null.");
                }
                Object accountInfo = debugUser(request, userId, appId);
                request.setAttribute(attributeName, accountInfo);
                return true;
            }
        }

        //check user login
        boolean bool = checkUser(request, response, annotation, appId);
        return bool;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }


    protected Object debugUser(HttpServletRequest request, String uid, String appId) {
        return uid;
    }

    protected boolean checkUser(HttpServletRequest request, HttpServletResponse response, Annotation annotation, String appId)
            throws Exception {
        boolean required = true;
        String version;
        try {
            if (RequiredUser.class.isAssignableFrom(annotationType)) {
                RequiredUser requiredUser = (RequiredUser) annotation;
                required = requiredUser.required();
                version = requiredUser.version();
                // version check
                if (StringUtils.hasLength(version) && !"all".equals(version)) {
                    String clientVersion = request.getHeader(apiVersionName);
                    if (clientVersion == null || !clientVersion.equals(version)) {
                        return true;
                    }
                }
            } else {
                boolean customCheck = customAnnotationHandle(request, response, annotation,appId);
                if (customCheck) {
                    return true;
                }
                required = customAnnotationRequired(request, response, annotation);
            }

            Object accountInfo = authenticationService.checkSession(request, response, annotation);
            if (required && accountInfo == null) {
                error(request, response, appId,null);
                return false;
            }
            request.setAttribute(attributeName, accountInfo);
        } catch (Exception e) {
            if (required) {
                error(request, response, appId,e);
                return false;
            } else {
                logger.error("exception: " + e.getClass() + "message: " + e.getMessage());
            }
        }
        return true;
    }


    /**
     * 自定义注解版本检测
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param annotation 自定义注解
     * @return true 放行(白名单) false 继续向下检测
     */
    protected boolean customAnnotationHandle(HttpServletRequest request, HttpServletResponse response, Annotation annotation,String appId) {
        return false;
    }

    /**
     * 自定义注解是否含有必须登录属性
     *
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     * @param annotation 自定义注解
     * @return true 必须登录 false 可以未登录访问
     */
    protected boolean customAnnotationRequired(HttpServletRequest request, HttpServletResponse response, Annotation annotation) {
        return true;
    }


    private void error(HttpServletRequest request, HttpServletResponse response, String appId,Exception e) throws Exception {
        long timestamp = System.currentTimeMillis();
        logger.error("timestamp=" + timestamp + " session check failed appId=" + appId,e);
        SuperHttpServlet.sendJson(response, 401, "sessionCheck failed appId=" + appId, timestamp);
    }

    public String getAppIdName() {
        return appIdName;
    }

    public void setAppIdName(String appIdName) {
        this.appIdName = appIdName;
    }

    public String getApiVersionName() {
        return apiVersionName;
    }

    public void setApiVersionName(String apiVersionName) {
        this.apiVersionName = apiVersionName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Class<? extends Annotation> getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}

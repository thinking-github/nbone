package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.*;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 此类用于Controller Method  反射执行
 * @author thinking
 * @version 1.0
 * @since 2019/7/12
 */
public class ServletInvocableHandlerMethodX extends ServletInvocableHandlerMethod {

    /**
     * lcoal class cache  父类中含有相关对象
     */
    private WebDataBinderFactory dataBinderFactory;
    private HandlerMethodArgumentResolverComposite argumentResolvers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
    private ParameterNameDiscoverer parameterNameDiscoverer;
    /**
     *  Controller Method  拦截器执行链
     */
    private InvokeExecutionChain invokeExecutionChain;


    public ServletInvocableHandlerMethodX(Object handler, Method method, InvokeExecutionChain invokeExecutionChain) {
        super(handler, method);
        this.invokeExecutionChain = invokeExecutionChain;
    }

    public ServletInvocableHandlerMethodX(HandlerMethod handlerMethod, InvokeExecutionChain invokeExecutionChain) {
        super(handlerMethod);
        this.invokeExecutionChain = invokeExecutionChain;
    }


    @Override
    public Object invokeForRequest(NativeWebRequest request, ModelAndViewContainer mavContainer,
                                   Object... providedArgs) throws Exception {

        Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
        if (logger.isTraceEnabled()) {
            logger.trace("Invoking '" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) +
                    "' with arguments " + Arrays.toString(args));
        }
        // XXX: 2019/7/12  添加调用 Controller Method 之前的预处理  add  preHandle   preInvoke  thinking
        if(invokeExecutionChain != null){
            invokeExecutionChain.applyPreHandle(request,args);
        }

        Object returnValue = doInvoke(args);
        if (logger.isTraceEnabled()) {
            logger.trace("Method [" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) +
                    "] returned [" + returnValue + "]");
        }
        return returnValue;
    }

    private Object[] getMethodArgumentValues(NativeWebRequest request, ModelAndViewContainer mavContainer,
                                             Object... providedArgs) throws Exception {

        MethodParameter[] parameters = getMethodParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];
            parameter.initParameterNameDiscovery(this.parameterNameDiscoverer);
            args[i] = resolveProvidedArgument(parameter, providedArgs);
            if (args[i] != null) {
                continue;
            }
            if (this.argumentResolvers.supportsParameter(parameter)) {
                try {
                    args[i] = this.argumentResolvers.resolveArgument(
                            parameter, mavContainer, request, this.dataBinderFactory);
                    continue;
                }
                catch (Exception ex) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(getArgumentResolutionErrorMessage("Failed to resolve", i), ex);
                    }
                    throw ex;
                }
            }
            if (args[i] == null) {
                throw new IllegalStateException("Could not resolve method parameter at index " +
                        parameter.getParameterIndex() + " in " + parameter.getMethod().toGenericString() +
                        ": " + getArgumentResolutionErrorMessage("No suitable resolver for", i));
            }
        }
        return args;
    }


    /**
     * @see InvocableHandlerMethod#getArgumentResolutionErrorMessage(String,int)
     */
    private String getArgumentResolutionErrorMessage(String text, int index) {
        Class<?> paramType = getMethodParameters()[index].getParameterType();
        return text + " argument " + index + " of type '" + paramType.getName() + "'";
    }

    /**
     * Attempt to resolve a method parameter from the list of provided argument values.
     * @see InvocableHandlerMethod#resolveProvidedArgument(MethodParameter,Object...)
     */
    private Object resolveProvidedArgument(MethodParameter parameter, Object... providedArgs) {
        if (providedArgs == null) {
            return null;
        }
        for (Object providedArg : providedArgs) {
            if (parameter.getParameterType().isInstance(providedArg)) {
                return providedArg;
            }
        }
        return null;
    }


    //  ---------------------setter---------------------------
    /**
     * Set {@link HandlerMethodArgumentResolver}s to use to use for resolving method argument values.
     */
    @Override
    public void setHandlerMethodArgumentResolvers(HandlerMethodArgumentResolverComposite argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
        super.setHandlerMethodArgumentResolvers(argumentResolvers);
    }
    /**
     * Register {@link HandlerMethodReturnValueHandler} instances to use to
     * handle return values.
     */
    @Override
    public void setHandlerMethodReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
        this.returnValueHandlers = returnValueHandlers;
        super.setHandlerMethodReturnValueHandlers(returnValueHandlers);
    }

    /**
     * Set the {@link WebDataBinderFactory} to be passed to argument resolvers allowing them to create
     * a {@link WebDataBinder} for data binding and type conversion purposes.
     * @param dataBinderFactory the data binder factory.
     */
    @Override
    public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory) {
        this.dataBinderFactory = dataBinderFactory;
        super.setDataBinderFactory(dataBinderFactory);
    }
    /**
     * Set the ParameterNameDiscoverer for resolving parameter names when needed
     * (e.g. default request attribute name).
     * <p>Default is a {@link org.springframework.core.DefaultParameterNameDiscoverer}.
     */
    @Override
    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
        super.setParameterNameDiscoverer(parameterNameDiscoverer);
    }

    /**
     *   设置调用拦截器执行链
     * @param invokeExecutionChain
     */
    public void setInvokeExecutionChain(InvokeExecutionChain invokeExecutionChain) {
        this.invokeExecutionChain = invokeExecutionChain;
    }
}

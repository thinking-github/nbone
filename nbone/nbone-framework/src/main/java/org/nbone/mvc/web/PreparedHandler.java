package org.nbone.mvc.web;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * HandlerInterceptor
 *
 * @author thinking
 * @version 1.0
 * @since 2019-11-22
 */
public interface PreparedHandler<T>  {

    /**
     * 当前Controller方法执行之前预处理
     *
     * @param entityRequest
     * @param request
     */
    void preHandle(T entityRequest, HttpServletRequest request);
    /**
     * 保存更新之前处理操作(参数验证)
     *
     * <li>参数检验不通过直接抛异常</li>
     * <li>默认值填充</li>
     *
     * @param entityRequest
     * @param request
     */
    void saveOrUpdateBefore(T entityRequest, HttpServletRequest request);

    /**
     * 更新之前处理(参数验证)
     *
     * <li>参数检验不通过直接抛异常</li>
     * <li>默认值填充</li>
     *
     * @param entityRequest
     * @param request
     */
    void updateBefore(T entityRequest, HttpServletRequest request);

    /**
     * 保存之前处理(参数验证)
     * <li>参数检验不通过直接抛异常</li>
     * <li>默认值填充</li>
     *
     * @param entityRequest
     * @param request
     */
    void saveBefore(T entityRequest, HttpServletRequest request);

    /**
     * 查询之前处理(参数验证)
     * <li>参数检验不通过直接抛异常</li>
     * <li>默认值填充</li>
     *
     * @param entityRequest
     * @param request
     */
    void queryBefore(T entityRequest, HttpServletRequest request);

}

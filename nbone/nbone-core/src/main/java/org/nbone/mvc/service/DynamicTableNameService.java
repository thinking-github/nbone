package org.nbone.mvc.service;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019-11-17
 */
public interface DynamicTableNameService<T, Id extends Serializable> extends SuperService<T, Id> {


    void delete(Id id, HttpServletRequest request);

    T get(Id id, HttpServletRequest request);


}

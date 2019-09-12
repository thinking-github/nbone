package org.nbone.persistence;

import org.springframework.data.domain.Page;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-09-09
 */
public interface RequestQuery {


    /**
     * 根据Servlet ServletRequest parameter 进行查询 支持limit
     *
     * @param request
     * @param sqlConfig
     * @param <T>
     * @return
     */
    <T> List<T> requestQuery(ServletRequest request, SqlConfig sqlConfig);

    /**
     * 根据Servlet ServletRequest parameter 进行查询分页查询
     *
     * @param request
     * @param sqlConfig
     * @param <T>
     * @return
     */
    <T> Page<T> requestQueryPage(ServletRequest request, SqlConfig sqlConfig);
}

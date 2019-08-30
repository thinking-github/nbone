package org.nbone.mvc.service;

import org.nbone.persistence.SqlConfig;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-08-27
 */
public interface BaseService<T,IdType extends Serializable> extends SuperService<T, IdType>  {

    public List<T> getForList(T object, SqlConfig sqlConfig);

    public List<T> queryForList(T object, SqlConfig sqlConfig);

    public List<T> getForLimit(T object,SqlConfig sqlConfig,int limit);

    public List<T> queryForLimit(T object,SqlConfig sqlConfig,int limit);


    public Page<T> getForPage(T object, SqlConfig sqlConfig, int pageNum, int pageSize);

    public Page<T> queryForPage(T object,SqlConfig sqlConfig,int pageNum,int pageSize);

    /**
     * 按照实体中不为空的参数作为参数统计行数
     * @param object 参数实体
     * @param sqlConfig 增加条件语句 如: and id in(1,2,3)
     * @return
     */
    public long count(T object,SqlConfig sqlConfig);



}

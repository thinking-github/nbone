package org.nbone.persistence.util;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 *
 * @author chenyicheng
 * @version 1.0
 * @since 2018/11/20
 */
public class JpaAnnotationUtils {

    /**
     * 获取配置的表名称
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> String  getTableName(Class<T> entityClass){
        if(entityClass.isAnnotationPresent(Table.class) && entityClass.isAnnotationPresent(Entity.class)){
            Table an  = entityClass.getAnnotation(Table.class);
            return  an.name() != null ? an.name() : null;
        }
        return  null;
    }

}

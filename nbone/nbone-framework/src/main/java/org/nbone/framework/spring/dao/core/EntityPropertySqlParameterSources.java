package org.nbone.framework.spring.dao.core;

import java.util.Collection;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019/2/27
 */
public class EntityPropertySqlParameterSources {

    private EntityPropertySqlParameterSource[] propertySqlParameterSources;


    public EntityPropertySqlParameterSource[] getPropertySqlParameterSources() {
        return propertySqlParameterSources;
    }

    public void setPropertySqlParameterSources(EntityPropertySqlParameterSource[] propertySqlParameterSources) {
        this.propertySqlParameterSources = propertySqlParameterSources;
    }






    public static EntityPropertySqlParameterSource[] setPropertySqlParameterSources(Collection<?> objects) {
        EntityPropertySqlParameterSource[]  parameterSources = new EntityPropertySqlParameterSource[objects.size()];
        int index = 0 ;
        for (Object object : objects) {
            parameterSources[index] = new EntityPropertySqlParameterSource(object);
            index ++;
        }
        return  parameterSources;
    }

    public static EntityPropertySqlParameterSource[] setPropertySqlParameterSources(Object[] objects) {
        EntityPropertySqlParameterSource[]  parameterSources = new EntityPropertySqlParameterSource[objects.length];
        int index = 0 ;
        for (Object object : objects) {
            parameterSources[index] = new EntityPropertySqlParameterSource(object);
            index ++;
        }
        return  parameterSources;
    }


}

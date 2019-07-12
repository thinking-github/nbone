package org.nbone.mvc.dao;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.nbone.framework.spring.dao.config.JdbcComponentConfig;
import org.nbone.persistence.BaseSqlBuilder;
import org.nbone.persistence.annotation.ResultMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 自定义注解 @ResultMapping 实现mybatis 映射文件自动加入nbone jdbc
 *
 * 使用方式在NboneConfig  @Import({BeanPostProcessorDao.class})
 * @author chenyicheng
 * @version 1.0
 * @since 2019-06-29
 */
@Configuration
public class BeanPostProcessorDao extends InstantiationAwareBeanPostProcessorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BeanPostProcessorDao.class);

    @Resource
    private JdbcComponentConfig jdbcConfig;

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (MapperFactoryBean.class.isAssignableFrom(beanClass)) {
            MapperFactoryBean factoryBean = (MapperFactoryBean) bean;
            Class<?> objectType = factoryBean.getObjectType();

            if (objectType.isAnnotationPresent(ResultMapping.class)) {
                ResultMapping resultMapping = objectType.getAnnotation(ResultMapping.class);
                String id = resultMapping.id();
                if (StringUtils.isEmpty(id)) {
                    id = jdbcConfig.getMybatisMapperId();

                }
                BaseSqlBuilder.buildTableMapper(objectType.getName(), id);
            }


        }
        return true;
    }


}
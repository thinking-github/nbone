package org.nbone.mybatis.util;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.nbone.persistence.annotation.FieldProperty;
import org.nbone.persistence.annotation.MappedBy;
import org.nbone.persistence.enums.QueryType;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.nbone.persistence.enums.QueryType.LIKE;

/**
 * @author chenyicheng
 * @version 1.0
 * @since 2019/6/19
 */
@Slf4j
@SuppressWarnings(value = "unchecked")
public class ExampleUtils {

    private static final List<String> IGNORE_PROPERTY = ImmutableList.of("class", "serialVersionUID");

    public static Example buildSimpleExample(Object model, String... ignoreNames) {

        Class<? extends Object> clazz = model.getClass();

        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();

        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(clazz);

        Arrays.asList(descriptors).forEach(item -> {

            String name = item.getDisplayName();

            if (IGNORE_PROPERTY.contains(name)) {
                return;
            }
            // javax.persistence.Transient
            Field field = FieldUtils.getDeclaredField(clazz, name, true);
            if (field != null && field.isAnnotationPresent(Transient.class)) {
                return;
            }

            // 忽略字段
            if (ignoreNames != null && ignoreNames.length > 0) {
                for (String ignoreName : ignoreNames) {
                    if (ignoreName.equals(name)) {
                        return;
                    }
                }
            }

            try {
                Object oValue = item.getReadMethod().invoke(model);
                if (StringUtils.isEmpty(oValue)) {
                    return;
                }

                criteria.andEqualTo(name, oValue);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("Build example error, property[{}]", name);
            }
        });
        return example;
    }

    public static Example buildExample(Object model, String... ignoreNames) {

        Class<? extends Object> clazz = model.getClass();

        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();

        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(clazz);

        Arrays.asList(descriptors).forEach(item -> {

            String name = item.getDisplayName();

            if (IGNORE_PROPERTY.contains(name)) {
                return;
            }
            // javax.persistence.Transient
            Field field = FieldUtils.getDeclaredField(clazz, name, true);
            if (field != null && field.isAnnotationPresent(Transient.class) && !field.isAnnotationPresent(MappedBy.class)) {
                return;
            }

            // 忽略字段
            if (ignoreNames != null && ignoreNames.length > 0) {
                for (String ignoreName : ignoreNames) {
                    if (ignoreName.equals(name)) {
                        return;
                    }
                }
            }

            try {
                Object oValue = item.getReadMethod().invoke(model);
                if (StringUtils.isEmpty(oValue)) {
                    return;
                }
                fieldOperationType(field, oValue, criteria);

            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                log.error("Build example error, property[{}]", name);
            }
        });
        return example;
    }

    /**
     * 根据条件names 构建参数条件
     *
     * @param object         实体对象
     * @param conditionNames 参数条件数组
     * @return
     */
    public static Example buildExampleNames(Object object, String[] conditionNames) {

        Class<? extends Object> clazz = object.getClass();
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();

        for (String name : conditionNames) {
            Field field = FieldUtils.getDeclaredField(clazz, name, true);
            Object value = ReflectionUtils.getField(field, object);
            if (ObjectUtils.isEmpty(value)) {
                continue;
            }
            fieldOperationType(field, value, criteria);
        }

        return example;
    }

    private static void fieldOperationType(Field field, Object value, Example.Criteria criteria) {
        if (field.isAnnotationPresent(FieldProperty.class)) {
            FieldProperty fieldProperty = field.getAnnotation(FieldProperty.class);
            QueryType queryType = fieldProperty.queryType();
            whereQueryType(queryType, criteria, field.getName(), value);
        } else if (field.isAnnotationPresent(MappedBy.class)) {
            MappedBy mappedBy = field.getAnnotation(MappedBy.class);
            String fieldName = mappedBy.name();
            QueryType queryType = mappedBy.queryType();
            whereQueryType(queryType, criteria, fieldName, value);
        } else {
            criteria.andEqualTo(field.getName(), value);
        }
    }

    public static void andBetween(Example example, String property, Object[] vaules) {
        if (vaules != null && vaules.length == 2) {
            andBetween(example, property, vaules[0], vaules[1]);
        }
    }

    public static void andBetween(Example example, String property, Object value1, Object value2) {
        if (value1 != null && value2 != null) {
            Example.Criteria criteria = example.and();
            criteria.andBetween(property, value1, value2);
        }
    }


    public static void andIn(Example example, String property, Object[] values) {
        if (values != null && values.length > 0) {
            Example.Criteria criteria = example.and();
            criteria.andIn(property, Arrays.asList(values));
        }
    }

    public static void andIn(Example example, String property, Iterable values) {
        Example.Criteria criteria = example.and();
        criteria.andIn(property, values);
    }


    /**
     * @param queryType
     */

    public static void whereQueryType(QueryType queryType, Example.Criteria criteria, String name, Object value) {
        if (value == null) {
            return;
        }
        Iterable iterable = null;
        switch (queryType) {
            case EQ:
                criteria.andEqualTo(name, value);
                break;
            case NE:
                criteria.andNotEqualTo(name, value);
                break;
            case GT:
                criteria.andGreaterThan(name, value);
                break;
            case GTE:
                criteria.andGreaterThanOrEqualTo(name, value);
                break;
            case LT:
                criteria.andLessThan(name, value);
                break;
            case LTE:
                criteria.andLessThanOrEqualTo(name, value);
                break;
            case IN:
                if (value.getClass().isArray()) {
                    iterable = Arrays.asList((Object[]) value);
                } else {
                    iterable = (Iterable) value;
                }
                criteria.andIn(name, iterable);

                break;
            case NOT_IN:
                if (value.getClass().isArray()) {
                    iterable = Arrays.asList((Object[]) value);
                } else {
                    iterable = (Iterable) value;
                }
                criteria.andNotIn(name, iterable);
                break;

            case BETWEEN:
                if (value.getClass().isArray()) {
                    Object[] values = (Object[]) value;
                    if (values.length == 2 && values[0] != null && values[1] != null) {
                        criteria.andBetween(name, values[0], values[1]);
                    }
                } else if (value instanceof List) {
                    List<Object> list = (List<Object>) value;
                    if (list.size() == 2 && list.get(0) != null && list.get(1) != null) {
                        criteria.andBetween(name, list.get(0), list.get(1));
                    }
                }
                break;
            case NOT_BETWEEN:
                if (value.getClass().isArray()) {
                    Object[] values = (Object[]) value;
                    if (values.length == 2 && values[0] != null && values[1] != null) {
                        criteria.andNotBetween(name, values[0], values[1]);
                    }
                } else if (value instanceof List) {
                    List<Object> list = (List<Object>) value;
                    if (list.size() == 2 && list.get(0) != null && list.get(1) != null) {
                        criteria.andNotBetween(name, list.get(0), list.get(1));
                    }
                }
                break;

            case LIKE:
                criteria.andLike(name, LIKE.getValuePrefix() + value + LIKE.getValueSuffix());
                break;
            case LEFT_LIKE:
                criteria.andLike(name, LIKE.getValuePrefix() + value + LIKE.getValueSuffix());
                break;
            case RIGHT_LIKE:
                criteria.andLike(name, LIKE.getValuePrefix() + value + LIKE.getValueSuffix());
                break;
            case IS_NULL:
                criteria.andIsNull(name);
                break;
            case IS_NOT_NULL:
                criteria.andIsNotNull(name);
                break;
            default:
                break;
        }
    }
}

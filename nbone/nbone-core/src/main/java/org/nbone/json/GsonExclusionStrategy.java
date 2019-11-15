package org.nbone.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import org.springframework.util.ObjectUtils;

/**
 * Gson 字段属性排除策略使用jackson annotation JsonIgnoreProperties
 *
 * @author thinking
 * @version 1.0
 * @since 2019-11-08
 */
public class GsonExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        JsonIgnoreProperties ignoreProperties = f.getDeclaringClass().getAnnotation(JsonIgnoreProperties.class);
        if (ignoreProperties != null) {
            String[] names = ignoreProperties.value();
            if (ObjectUtils.isEmpty(names)) {
                return false;
            }

            for (String name : names) {
                if (f.getName().equals(name)) {
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(JsonIgnoreType.class);
    }
}

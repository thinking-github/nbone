package org.nbone.util.lang;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * object to String  format
 *
 * @author thinking
 * @version 1.0
 * @see org.apache.commons.lang3.builder.ToStringBuilder
 * @see org.apache.commons.lang3.builder.ReflectionToStringBuilder
 * @since 2016年4月4日
 */
public class ToStringUtils {
    /**
     * 多行模式格式化
     *
     * @param object
     * @return
     */
    public static String toStringMultiLine(Object object) {
        return ToStringBuilder.reflectionToString(object, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
     * 单行模式格式化
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        return ToStringBuilder.reflectionToString(object);
    }


    /**
     * object to String  format with filter value  null
     *
     * @param object
     * @param style
     * @return
     */
    public static String toString(Object object, ToStringStyle style, boolean excludeNullValues) {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(object, style) {
            @Override
            protected void appendFieldsIn(Class<?> clazz) {
                if (clazz.isArray()) {
                    this.reflectionAppendArray(this.getObject());
                    return;
                }
                final Field[] fields = clazz.getDeclaredFields();
                AccessibleObject.setAccessible(fields, true);
                for (final Field field : fields) {
                    final String fieldName = field.getName();
                    if (this.accept(field)) {
                        try {
                            // Warning: Field.get(Object) creates wrappers objects
                            // for primitive types.
                            final Object fieldValue = this.getValue(field);
                            if (!excludeNullValues || fieldValue != null) {
                                this.append(fieldName, fieldValue);
                            }
                        } catch (final IllegalAccessException ex) {
                            //this can't happen. Would get a Security exception
                            // instead
                            //throw a runtime exception in case the impossible
                            // happens.
                            throw new InternalError("Unexpected IllegalAccessException: " + ex.getMessage());
                        }
                    }
                }
            }
        };
        return builder.toString();
    }


}

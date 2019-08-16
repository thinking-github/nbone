package org.nbone.core.beans;


import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * @author thinking
 * @version 1.0
 * @since 2019-08-09
 */
@SuppressWarnings("unused")
public class BasicDynaBeanCollection extends BasicDynaBean implements DynaBeanCollection, Serializable {

    @Override
    public boolean contains(final String name, final String key) {
        final Object value = values.get(name);
        if (value == null) {
            throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
        } else if (value instanceof Map) {
            return (((Map<?, ?>) value).containsKey(key));
        } else {
            throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
        }

    }

    @Override
    public Object get(final String name, final String key) {

        final Object value = values.get(name);
        if (value == null) {
            throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
        } else if (value instanceof Map) {
            return (((Map<?, ?>) value).get(key));
        } else {
            throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
        }

    }

    @Override
    public void set(final String name, final String key, final Object value) {

        final Object prop = values.get(name);
        if (prop == null) {
            throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
        } else if (prop instanceof Map) {
            @SuppressWarnings("unchecked") final
            // This is safe to cast because mapped properties are always
                    // maps of types String -> Object
                    Map<String, Object> map = (Map<String, Object>) prop;
            map.put(key, value);
        } else {
            throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
        }

    }

    @Override
    public void remove(final String name, final String key) {

        final Object value = values.get(name);
        if (value == null) {
            throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
        } else if (value instanceof Map) {
            ((Map<?, ?>) value).remove(key);
        } else {
            throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
        }

    }

    @Override
    public Object get(final String name, final int index) {

        final Object value = values.get(name);
        if (value == null) {
            throw new NullPointerException("No indexed value for '" + name + "[" + index + "]'");
        } else if (value.getClass().isArray()) {
            return (Array.get(value, index));
        } else if (value instanceof List) {
            return ((List<?>) value).get(index);
        } else {
            throw new IllegalArgumentException("Non-indexed property for '" + name + "[" + index + "]'");
        }

    }

    @Override
    public void set(final String name, final int index, final Object value) {

        final Object prop = values.get(name);
        if (prop == null) {
            throw new NullPointerException("No indexed value for '" + name + "[" + index + "]'");
        } else if (prop.getClass().isArray()) {
            Array.set(prop, index, value);
        } else if (prop instanceof List) {
            @SuppressWarnings("unchecked") final List<Object> list = (List<Object>) prop;
            list.set(index, value);
        } else {
            throw new IllegalArgumentException("Non-indexed property for '" + name + "[" + index + "]'");
        }

    }
}

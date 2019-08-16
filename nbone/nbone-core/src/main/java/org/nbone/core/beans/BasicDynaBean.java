package org.nbone.core.beans;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author thinking
 * @version 1.0
 * @see org.apache.commons.beanutils.BasicDynaBean
 * @since 2019-08-09
 */
@SuppressWarnings("unused")
public class BasicDynaBean implements DynaBean, Serializable {

    /**
     * The set of property values for this DynaBean, keyed by property name.
     * properties /callables
     */
    protected HashMap<String, Object> values = new HashMap<String, Object>();

    @Override
    public boolean contains(String name) {
        return values.containsKey(name);
    }

    @Override
    public Object get(String name) {
        return values.get(name);
    }

    @Override
    public void set(String name, Object value) {
        values.put(name, value);
    }

    public Map<String, Object> getValues() {
        return values;
    }
}

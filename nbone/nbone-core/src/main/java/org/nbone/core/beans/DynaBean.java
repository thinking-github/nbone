package org.nbone.core.beans;

/**
 * @author thinking
 * @version 1.0
 * @see org.apache.commons.beanutils.DynaBean
 * @since 2019-08-09
 */
public interface DynaBean {


    /**
     * Does the specified mapped property contain a value for the specified
     * key value?
     *
     * @param name Name of the property to check
     * @return <code>true<code> if the mapped property contains a value for
     * the specified key, otherwise <code>false</code>
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     */
    public boolean contains(String name);

    /**
     * Return the value of a simple property with the specified name.
     *
     * @param name Name of the property whose value is to be retrieved
     * @return The property's value
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     */
    public Object get(String name);


    /**
     * Set the value of a simple property with the specified name.
     *
     * @param name  Name of the property whose value is to be set
     * @param value Value to which this property is to be set
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     * @throws NullPointerException     if an attempt is made to set a
     *                                  primitive property to null
     */
    public void set(String name, Object value);

}

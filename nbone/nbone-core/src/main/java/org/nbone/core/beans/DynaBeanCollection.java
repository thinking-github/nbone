package org.nbone.core.beans;


/**
 * 支持map 和 Collection操作
 *
 * @author thinking
 * @version 1.0
 * @since 2019-08-09
 */
public interface DynaBeanCollection extends DynaBean {


    /**
     * Does the specified mapped property contain a value for the specified
     * key value?
     *
     * @param name Name of the property to check
     * @param key  Name of the key to check
     * @return <code>true<code> if the mapped property contains a value for
     * the specified key, otherwise <code>false</code>
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     */
    public boolean contains(String name, String key);

    /**
     * Return the value of a mapped property with the specified name,
     * or <code>null</code> if there is no value for the specified key.
     *
     * @param name Name of the property whose value is to be retrieved
     * @param key  Key of the value to be retrieved
     * @return The mapped property's value
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     * @throws IllegalArgumentException if the specified property
     *                                  exists, but is not mapped
     */
    public Object get(String name, String key);

    /**
     * Set the value of a mapped property with the specified name.
     *
     * @param name  Name of the property whose value is to be set
     * @param key   Key of the property to be set
     * @param value Value to which this property is to be set
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     * @throws IllegalArgumentException if the specified property
     *                                  exists, but is not mapped
     */
    public void set(String name, String key, Object value);


    /**
     * Remove any existing value for the specified key on the
     * specified mapped property.
     *
     * @param name Name of the property for which a value is to
     *             be removed
     * @param key  Key of the value to be removed
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     */
    public void remove(String name, String key);


    /**
     * Return the value of an indexed property with the specified name.
     *
     * @param name  Name of the property whose value is to be retrieved
     * @param index Index of the value to be retrieved
     * @return The indexed property's value
     * @throws IllegalArgumentException  if there is no property
     *                                   of the specified name
     * @throws IllegalArgumentException  if the specified property
     *                                   exists, but is not indexed
     * @throws IndexOutOfBoundsException if the specified index
     *                                   is outside the range of the underlying property
     * @throws NullPointerException      if no array or List has been
     *                                   initialized for this property
     */
    public Object get(String name, int index);

    /**
     * Set the value of an indexed property with the specified name.
     *
     * @param name  Name of the property whose value is to be set
     * @param index Index of the property to be set
     * @param value Value to which this property is to be set
     * @throws IllegalArgumentException  if there is no property
     *                                   of the specified name
     * @throws IllegalArgumentException  if the specified property
     *                                   exists, but is not indexed
     * @throws IndexOutOfBoundsException if the specified index
     *                                   is outside the range of the underlying property
     */
    public void set(String name, int index, Object value);


}

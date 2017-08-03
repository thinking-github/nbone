package org.nbone.lang;

/**
 * 
 * @author thinking
 * @version 1.0
 * @see java.lang.Enum
 *
 */
public interface IEnum<T> {
	/**
	 * 枚举 标识 可以是String/int
	 * @return
	 */
	 T getCode();
	
	/**
	 * Enum 名称
	 * @return
	 */
	String getName();
	
	/**
     * 获取枚举描述
     * 
     * @return
     */
    public String getDescription();

}

package org.nbone.lang;

/**
 * 
 * @author thinking
 * @see 
 * @version 1.0
 *
 */
public interface IEnum {
	/**
	 * long type 标识
	 * @return
	 */
	int getId();
	/**
	 * String type 标识
	 * @return
	 */
	String getCode();
	
	/**
	 * Enum 名称
	 * @return
	 */
	String getName();

}

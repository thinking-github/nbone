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

	/**
	 * 根据code 获取枚举对象
	 * @param enumClass
	 * @param code String / int
	 * @param <E>
	 * @return
	 */
	public static <E extends Enum<?> & IEnum> E codeOf(Class<E> enumClass, Object code) {
		E[] enumConstants = enumClass.getEnumConstants();
		for (E e : enumConstants) {
			if (e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 根据code 获取枚举名称信息
	 * @param enumClass
	 * @param code
	 * @param <E>
	 * @return
	 */
	public static <E extends Enum<?> & IEnum> String getName(Class<E> enumClass, Object code) {
		E eenum = codeOf(enumClass, code);
		return null == eenum ? "" : eenum.getName();
	}

}

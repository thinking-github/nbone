package org.nbone.framework.spring.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.core.util.EnumUtils;
import org.nbone.lang.IEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * 
 * @author thinking
 * @version 1.0.1
 * @since spring 3.0
 * @since 2016-09-02
 * @see org.springframework.core.convert.support.StringToEnumConverterFactory
 * @see org.nbone.lang.IEnum
 * @see org.nbone.core.util.EnumUtils
 *
 */
public class IEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

	protected Log logger = LogFactory.getLog(getClass());

	@Override
	public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
		if (IEnum.class.isAssignableFrom(targetType)) {
			return new StringToEnum<T>(targetType);
		}

		logger.error(targetType + " targetType must implements IEnum .thinking");
		return null;
	}

	/**
	 * 枚举标识编码转化成枚举
	 * 
	 * @author thinking
	 *
	 * @param <T>
	 */
	private class StringToEnum<T extends Enum<?>> implements Converter<String, T> {

		private final Class<T> enumType;

		public StringToEnum(Class<T> enumType) {
			this.enumType = enumType;
		}

		@Override
		public T convert(String source) {

			if (source.length() == 0) {
				// It's an empty enum identifier: reset the enum value to null.
				return null;
			}
			T target = null;
			// 转换成数字 
			//当枚举使用数字编码时
			try {
				int ordinal = Integer.valueOf(source);
				if (ordinal == Integer.MIN_VALUE) {
					return null;
				}
				target = EnumUtils.getEnum(enumType, ordinal);

				// 使用字符的方式转换
				if (target == null) {
					target = EnumUtils.getEnum(enumType, source);
				}

			} catch (NumberFormatException e) {
				// 使用字符的方式转换
				target = EnumUtils.getEnum(enumType, source);
			}

			return target;
		}

	}
}
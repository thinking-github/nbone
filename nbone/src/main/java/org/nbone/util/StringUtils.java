package org.nbone.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class StringUtils {

	private static abstract class WordTokenizer {

		public String parse(String str) {
			if (StringUtils.isEmpty(str))
				return str;
			int length = str.length();
			StringBuffer buffer = new StringBuffer(length);
			for (int index = 0; index < length; index++) {
				char ch;
				label0: {
					int wordIndex;
					label1: {
						ch = str.charAt(index);
						if (Character.isWhitespace(ch))
							continue;
						if (!Character.isUpperCase(ch))
							break label0;
						wordIndex = index + 1;
						char wordChar;
						do {
							if (wordIndex >= length)
								break label1;
							wordChar = str.charAt(wordIndex);
							if (!Character.isUpperCase(wordChar))
								break;
							wordIndex++;
						} while (true);
						if (Character.isLowerCase(wordChar))
							wordIndex--;
					}
					if (wordIndex == length || wordIndex > index)
						index = parseUpperCaseWord(buffer, str, index,
								wordIndex);
					else
						index = parseTitleCaseWord(buffer, str, index);
					continue;
				}
				if (Character.isLowerCase(ch)) {
					index = parseLowerCaseWord(buffer, str, index);
					continue;
				}
				if (Character.isDigit(ch))
					index = parseDigitWord(buffer, str, index);
				else
					inDelimiter(buffer, ch);
			}

			return buffer.toString();
		}

		private int parseUpperCaseWord(StringBuffer buffer, String str,
				int index, int length) {
			char ch = str.charAt(index++);
			if (buffer.length() == 0)
				startSentence(buffer, ch);
			else
				startWord(buffer, ch);
			for (; index < length; index++) {
				ch = str.charAt(index);
				inWord(buffer, ch);
			}

			return index - 1;
		}

		private int parseLowerCaseWord(StringBuffer buffer, String str,
				int index) {
			char ch = str.charAt(index++);
			if (buffer.length() == 0)
				startSentence(buffer, ch);
			else
				startWord(buffer, ch);
			int length = str.length();
			do {
				if (index >= length)
					break;
				ch = str.charAt(index);
				if (!Character.isLowerCase(ch))
					break;
				inWord(buffer, ch);
				index++;
			} while (true);
			return index - 1;
		}

		private int parseTitleCaseWord(StringBuffer buffer, String str,
				int index) {
			char ch = str.charAt(index++);
			if (buffer.length() == 0)
				startSentence(buffer, ch);
			else
				startWord(buffer, ch);
			int length = str.length();
			do {
				if (index >= length)
					break;
				ch = str.charAt(index);
				if (!Character.isLowerCase(ch))
					break;
				inWord(buffer, ch);
				index++;
			} while (true);
			return index - 1;
		}

		private int parseDigitWord(StringBuffer buffer, String str, int index) {
			char ch = str.charAt(index++);
			if (buffer.length() == 0)
				startDigitSentence(buffer, ch);
			else
				startDigitWord(buffer, ch);
			int length = str.length();
			do {
				if (index >= length)
					break;
				ch = str.charAt(index);
				if (!Character.isDigit(ch))
					break;
				inDigitWord(buffer, ch);
				index++;
			} while (true);
			return index - 1;
		}

		protected boolean isDelimiter(char ch) {
			return !Character.isUpperCase(ch) && !Character.isLowerCase(ch)
					&& !Character.isDigit(ch);
		}

		protected abstract void startSentence(StringBuffer stringbuffer, char c);

		protected abstract void startWord(StringBuffer stringbuffer, char c);

		protected abstract void inWord(StringBuffer stringbuffer, char c);

		protected abstract void startDigitSentence(StringBuffer stringbuffer,
				char c);

		protected abstract void startDigitWord(StringBuffer stringbuffer, char c);

		protected abstract void inDigitWord(StringBuffer stringbuffer, char c);

		protected abstract void inDelimiter(StringBuffer stringbuffer, char c);

		protected static final char UNDERSCORE = 95;

		private WordTokenizer() {
		}

	}

	public StringUtils() {
	}

	public static boolean isNullOrEmpty(String s) {
		return s == null || isBlank(s);
	}

	public static boolean equal(String s1, String s2, boolean p_ignoreCase) {
		if (s1 == null || s2 == null)
			return false;
		if (p_ignoreCase)
			return s1.equalsIgnoreCase(s2);
		else
			return s1.equals(s2);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return str != null && str.length() > 0;
	}

	public static boolean isBlank(String str) {
		int length;
		if (str == null || (length = str.length()) == 0)
			return true;
		for (int i = 0; i < length; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return false;

		return true;
	}

	public static boolean isNotBlank(String str) {
		int length;
		if (str == null || (length = str.length()) == 0)
			return false;
		for (int i = 0; i < length; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return true;

		return false;
	}

	public static String defaultIfNull(String str) {
		return str != null ? str : "";
	}

	public static String defaultIfNull(String str, String defaultStr) {
		return str != null ? str : defaultStr;
	}

	public static String defaultIfEmpty(String str) {
		return str != null ? str : "";
	}

	public static String defaultIfEmpty(String str, String defaultStr) {
		return str != null && str.length() != 0 ? str : defaultStr;
	}

	public static String defaultIfBlank(String str) {
		return isBlank(str) ? "" : str;
	}

	public static String defaultIfBlank(String str, String defaultStr) {
		return isBlank(str) ? defaultStr : str;
	}

	public static String trimAll(String str) {
		if (isNullOrEmpty(str))
			return "";
		str = trim(str);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			String ch = substring(str, i, i + 1);
			if (isNotBlank(ch))
				buf.append(ch);
		}

		return buf.toString();
	}

	public static String trim(String str) {
		return trim(str, null, 0);
	}

	public static String trim(String str, String stripChars) {
		return trim(str, stripChars, 0);
	}

	public static String trimStart(String str) {
		return trim(str, null, -1);
	}

	public static String trimStart(String str, String stripChars) {
		return trim(str, stripChars, -1);
	}

	public static String trimEnd(String str) {
		return trim(str, null, 1);
	}

	public static String trimEnd(String str, String stripChars) {
		return trim(str, stripChars, 1);
	}

	public static String trimToNull(String str) {
		return trimToNull(str, null);
	}

	public static String trimToNull(String str, String stripChars) {
		String result = trim(str, stripChars);
		if (result == null || result.length() == 0)
			return null;
		else
			return result;
	}

	public static String trimToEmpty(String str) {
		return trimToEmpty(str, null);
	}

	public static String trimToEmpty(String str, String stripChars) {
		String result = trim(str, stripChars);
		if (result == null)
			return "";
		else
			return result;
	}

	private static String trim(String str, String stripChars, int mode) {
		if (str == null)
			return null;
		int length = str.length();
		int start = 0;
		int end = length;
		if (mode <= 0)
			if (stripChars == null) {
				for (; start < end && Character.isWhitespace(str.charAt(start)); start++)
					;
			} else {
				if (stripChars.length() == 0)
					return str;
				for (; start < end
						&& stripChars.indexOf(str.charAt(start)) != -1; start++)
					;
			}
		if (mode >= 0)
			if (stripChars == null) {
				for (; start < end
						&& Character.isWhitespace(str.charAt(end - 1)); end--)
					;
			} else {
				if (stripChars.length() == 0)
					return str;
				for (; start < end
						&& stripChars.indexOf(str.charAt(end - 1)) != -1; end--)
					;
			}
		if (start > 0 || end < length)
			return str.substring(start, end);
		else
			return str;
	}

	public static boolean equals(String str1, String str2) {
		if (str1 == null)
			return str2 == null;
		else
			return str1.equals(str2);
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		if (str1 == null)
			return str2 == null;
		else
			return str1.equalsIgnoreCase(str2);
	}

	public static boolean isAlpha(String str) {
		if (str == null)
			return false;
		int length = str.length();
		for (int i = 0; i < length; i++)
			if (!Character.isLetter(str.charAt(i)))
				return false;

		return true;
	}

	public static boolean isAlphaSpace(String str) {
		if (str == null)
			return false;
		int length = str.length();
		for (int i = 0; i < length; i++)
			if (!Character.isLetter(str.charAt(i)) && str.charAt(i) != ' ')
				return false;

		return true;
	}

	public static boolean isAlphanumeric(String str) {
		if (str == null)
			return false;
		int length = str.length();
		for (int i = 0; i < length; i++)
			if (!Character.isLetterOrDigit(str.charAt(i)))
				return false;

		return true;
	}

	public static boolean isAlphanumericSpace(String str) {
		if (str == null)
			return false;
		int length = str.length();
		for (int i = 0; i < length; i++)
			if (!Character.isLetterOrDigit(str.charAt(i))
					&& str.charAt(i) != ' ')
				return false;

		return true;
	}

	public static boolean isNumeric(String str) {
		if (str == null)
			return false;
		int length = str.length();
		for (int i = 0; i < length; i++)
			if (!Character.isDigit(str.charAt(i)))
				return false;

		return true;
	}

	public static boolean isNumericSpace(String str) {
		if (str == null)
			return false;
		int length = str.length();
		for (int i = 0; i < length; i++)
			if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != ' ')
				return false;

		return true;
	}

	public static boolean isWhitespace(String str) {
		if (str == null)
			return false;
		int length = str.length();
		for (int i = 0; i < length; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return false;

		return true;
	}

	public static String toUpperCase(String str) {
		if (str == null)
			return null;
		else
			return str.toUpperCase();
	}

	public static String toLowerCase(String str) {
		if (str == null)
			return null;
		else
			return str.toLowerCase();
	}

	public static String capitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return str;
		else
			return (new StringBuffer(strLen))
					.append(Character.toTitleCase(str.charAt(0)))
					.append(str.substring(1)).toString();
	}

	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return str;
		else
			return (new StringBuffer(strLen))
					.append(Character.toLowerCase(str.charAt(0)))
					.append(str.substring(1)).toString();
	}

	public static String swapCase(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return str;
		StringBuffer buffer = new StringBuffer(strLen);
		char ch = '\0';
		for (int i = 0; i < strLen; i++) {
			ch = str.charAt(i);
			if (Character.isUpperCase(ch))
				ch = Character.toLowerCase(ch);
			else if (Character.isTitleCase(ch))
				ch = Character.toLowerCase(ch);
			else if (Character.isLowerCase(ch))
				ch = Character.toUpperCase(ch);
			buffer.append(ch);
		}

		return buffer.toString();
	}

	public static String toCamelCase(String str) {
		return CAMEL_CASE_TOKENIZER.parse(str);
	}

	public static String toPascalCase(String str) {
		return PASCAL_CASE_TOKENIZER.parse(str);
	}

	public static String toUpperCaseWithUnderscores(String str) {
		return UPPER_CASE_WITH_UNDERSCORES_TOKENIZER.parse(str);
	}

	public static String toLowerCaseWithUnderscores(String str) {
		return LOWER_CASE_WITH_UNDERSCORES_TOKENIZER.parse(str);
	}

	public static String[] split(String str) {
		return split(str, null, -1);
	}

	public static String[] split(String str, char separatorChar) {
		if (str == null)
			return null;
		int length = str.length();
		if (length == 0)
			return new String[0];
		List list = new ArrayList();
		int i = 0;
		int start = 0;
		boolean match = false;
		while (i < length)
			if (str.charAt(i) == separatorChar) {
				if (match) {
					list.add(str.substring(start, i));
					match = false;
				}
				start = ++i;
			} else {
				match = true;
				i++;
			}
		if (match)
			list.add(str.substring(start, i));
		return (String[]) (String[]) list.toArray(new String[list.size()]);
	}

	public static String[] split(String str, String separatorChars) {
		return split(str, separatorChars, -1);
	}

	public static String[] split(String str, String separatorChars, int max) {
		if (str == null)
			return null;
		int length = str.length();
		if (length == 0)
			return new String[0];
		List list = new ArrayList();
		int sizePlus1 = 1;
		int i = 0;
		int start = 0;
		boolean match = false;
		if (separatorChars == null)
			while (i < length)
				if (Character.isWhitespace(str.charAt(i))) {
					if (match) {
						if (sizePlus1++ == max)
							i = length;
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
				} else {
					match = true;
					i++;
				}
		else if (separatorChars.length() == 1) {
			char sep = separatorChars.charAt(0);
			while (i < length)
				if (str.charAt(i) == sep) {
					if (match) {
						if (sizePlus1++ == max)
							i = length;
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
				} else {
					match = true;
					i++;
				}
		} else {
			while (i < length)
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match) {
						if (sizePlus1++ == max)
							i = length;
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
				} else {
					match = true;
					i++;
				}
		}
		if (match)
			list.add(str.substring(start, i));
		return (String[]) (String[]) list.toArray(new String[list.size()]);
	}

	public static String join(Object array[]) {
		return join(array, ((String) (null)));
	}

	public static String join(Object array[], char separator) {
		if (array == null)
			return null;
		int arraySize = array.length;
		int bufSize = arraySize != 0 ? ((array[0] != null ? array[0].toString()
				.length() : 16) + 1) * arraySize : 0;
		StringBuffer buf = new StringBuffer(bufSize);
		for (int i = 0; i < arraySize; i++) {
			if (i > 0)
				buf.append(separator);
			if (array[i] != null)
				buf.append(array[i]);
		}

		return buf.toString();
	}

	public static String join(Object array[], String separator) {
		if (array == null)
			return null;
		if (separator == null)
			separator = "";
		int arraySize = array.length;
		int bufSize = arraySize != 0 ? arraySize
				* ((array[0] != null ? array[0].toString().length() : 16) + (separator == null ? 0
						: separator.length()))
				: 0;
		StringBuffer buf = new StringBuffer(bufSize);
		for (int i = 0; i < arraySize; i++) {
			if (separator != null && i > 0)
				buf.append(separator);
			if (array[i] != null)
				buf.append(array[i]);
		}

		return buf.toString();
	}

	public static String joinWithoutSpace(String array[], String sep) {
		if (array == null || array.length == 0)
			return null;
		if (isNullOrEmpty(sep))
			sep = "";
		StringBuffer buf = new StringBuffer(256);
		for (int i = 0; i < array.length; i++) {
			if (isNullOrEmpty(array[i]))
				continue;
			if (i == 0)
				buf.append(array[i]);
			else
				buf.append(sep).append(array[i]);
		}

		return buf.toString();
	}

	public static String join(Iterator iterator, char separator) {
		if (iterator == null)
			return null;
		StringBuffer buf = new StringBuffer(256);
		do {
			if (!iterator.hasNext())
				break;
			Object obj = iterator.next();
			if (obj != null)
				buf.append(obj);
			if (iterator.hasNext())
				buf.append(separator);
		} while (true);
		return buf.toString();
	}

	public static String join(Iterator iterator, String separator) {
		if (iterator == null)
			return null;
		StringBuffer buf = new StringBuffer(256);
		do {
			if (!iterator.hasNext())
				break;
			Object obj = iterator.next();
			if (obj != null)
				buf.append(obj);
			if (separator != null && iterator.hasNext())
				buf.append(separator);
		} while (true);
		return buf.toString();
	}

	public static int indexOf(String str, char searchChar) {
		if (str == null || str.length() == 0)
			return -1;
		else
			return str.indexOf(searchChar);
	}

	public static int indexOf(String str, char searchChar, int startPos) {
		if (str == null || str.length() == 0)
			return -1;
		else
			return str.indexOf(searchChar, startPos);
	}

	public static int indexOf(String str, String searchStr) {
		if (str == null || searchStr == null)
			return -1;
		else
			return str.indexOf(searchStr);
	}

	public static int indexOf(String str, String searchStr, int startPos) {
		if (str == null || searchStr == null)
			return -1;
		if (searchStr.length() == 0 && startPos >= str.length())
			return str.length();
		else
			return str.indexOf(searchStr, startPos);
	}

	public static int indexOfAny(String str, char searchChars[]) {
		if (str == null || str.length() == 0 || searchChars == null
				|| searchChars.length == 0)
			return -1;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			for (int j = 0; j < searchChars.length; j++)
				if (searchChars[j] == ch)
					return i;

		}

		return -1;
	}

	public static int indexOfAny(String str, String searchChars) {
		if (str == null || str.length() == 0 || searchChars == null
				|| searchChars.length() == 0)
			return -1;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			for (int j = 0; j < searchChars.length(); j++)
				if (searchChars.charAt(j) == ch)
					return i;

		}

		return -1;
	}

	public static int indexOfAny(String str, String searchStrs[]) {
		if (str == null || searchStrs == null)
			return -1;
		int sz = searchStrs.length;
		int ret = 2147483647;
		int tmp = 0;
		for (int i = 0; i < sz; i++) {
			String search = searchStrs[i];
			if (search == null)
				continue;
			tmp = str.indexOf(search);
			if (tmp != -1 && tmp < ret)
				ret = tmp;
		}

		return ret != 2147483647 ? ret : -1;
	}

	public static int indexOfAnyBut(String str, char searchChars[]) {
		if (str == null || str.length() == 0 || searchChars == null
				|| searchChars.length == 0)
			return -1;
		int i = 0;
		label0: do {
			label1: {
				if (i >= str.length())
					break label0;
				char ch = str.charAt(i);
				for (int j = 0; j < searchChars.length; j++)
					if (searchChars[j] == ch)
						break label1;

				return i;
			}
			i++;
		} while (true);
		return -1;
	}

	public static int indexOfAnyBut(String str, String searchChars) {
		if (str == null || str.length() == 0 || searchChars == null
				|| searchChars.length() == 0)
			return -1;
		for (int i = 0; i < str.length(); i++)
			if (searchChars.indexOf(str.charAt(i)) < 0)
				return i;

		return -1;
	}

	public static int lastIndexOf(String str, char searchChar) {
		if (str == null || str.length() == 0)
			return -1;
		else
			return str.lastIndexOf(searchChar);
	}

	public static int lastIndexOf(String str, char searchChar, int startPos) {
		if (str == null || str.length() == 0)
			return -1;
		else
			return str.lastIndexOf(searchChar, startPos);
	}

	public static int lastIndexOf(String str, String searchStr) {
		if (str == null || searchStr == null)
			return -1;
		else
			return str.lastIndexOf(searchStr);
	}

	public static int lastIndexOf(String str, String searchStr, int startPos) {
		if (str == null || searchStr == null)
			return -1;
		else
			return str.lastIndexOf(searchStr, startPos);
	}

	public static int lastIndexOfAny(String str, String searchStrs[]) {
		if (str == null || searchStrs == null)
			return -1;
		int searchStrsLength = searchStrs.length;
		int index = -1;
		int tmp = 0;
		for (int i = 0; i < searchStrsLength; i++) {
			String search = searchStrs[i];
			if (search == null)
				continue;
			tmp = str.lastIndexOf(search);
			if (tmp > index)
				index = tmp;
		}

		return index;
	}

	public static boolean contains(String str, char searchChar) {
		if (str == null || str.length() == 0)
			return false;
		else
			return str.indexOf(searchChar) >= 0;
	}

	public static boolean contains(String str, String searchStr) {
		if (str == null || searchStr == null)
			return false;
		else
			return str.indexOf(searchStr) >= 0;
	}

	public static boolean containsOnly(String str, char valid[]) {
		if (valid == null || str == null)
			return false;
		if (str.length() == 0)
			return true;
		if (valid.length == 0)
			return false;
		else
			return indexOfAnyBut(str, valid) == -1;
	}

	public static boolean containsOnly(String str, String valid) {
		if (str == null || valid == null)
			return false;
		else
			return containsOnly(str, valid.toCharArray());
	}

	public static boolean containsNone(String str, char invalid[]) {
		if (str == null || invalid == null)
			return true;
		int strSize = str.length();
		int validSize = invalid.length;
		for (int i = 0; i < strSize; i++) {
			char ch = str.charAt(i);
			for (int j = 0; j < validSize; j++)
				if (invalid[j] == ch)
					return false;

		}

		return true;
	}

	public static boolean containsNone(String str, String invalidChars) {
		if (str == null || invalidChars == null)
			return true;
		else
			return containsNone(str, invalidChars.toCharArray());
	}

	public static int countMatches(String str, String subStr) {
		if (str == null || str.length() == 0 || subStr == null
				|| subStr.length() == 0)
			return 0;
		int count = 0;
		for (int index = 0; (index = str.indexOf(subStr, index)) != -1; index += subStr
				.length())
			count++;

		return count;
	}

	public static String substring(String str, int start) {
		if (str == null)
			return null;
		if (start < 0)
			start = str.length() + start;
		if (start < 0)
			start = 0;
		if (start > str.length())
			return "";
		else
			return str.substring(start);
	}

	public static String substring(String str, int start, int end) {
		if (str == null)
			return null;
		if (end < 0)
			end = str.length() + end;
		if (start < 0)
			start = str.length() + start;
		if (end > str.length())
			end = str.length();
		if (start > end)
			return "";
		if (start < 0)
			start = 0;
		if (end < 0)
			end = 0;
		return str.substring(start, end);
	}

	public static String left(String str, int len) {
		if (str == null)
			return null;
		if (len < 0)
			return "";
		if (str.length() <= len)
			return str;
		else
			return str.substring(0, len);
	}

	public static String right(String str, int len) {
		if (str == null)
			return null;
		if (len < 0)
			return "";
		if (str.length() <= len)
			return str;
		else
			return str.substring(str.length() - len);
	}

	public static String mid(String str, int pos, int len) {
		if (str == null)
			return null;
		if (len < 0 || pos > str.length())
			return "";
		if (pos < 0)
			pos = 0;
		if (str.length() <= pos + len)
			return str.substring(pos);
		else
			return str.substring(pos, pos + len);
	}

	public static String substringBefore(String str, String separator) {
		if (str == null || separator == null || str.length() == 0)
			return str;
		if (separator.length() == 0)
			return "";
		int pos = str.indexOf(separator);
		if (pos == -1)
			return str;
		else
			return str.substring(0, pos);
	}

	public static String substringAfter(String str, String separator) {
		if (str == null || str.length() == 0)
			return str;
		if (separator == null)
			return "";
		int pos = str.indexOf(separator);
		if (pos == -1)
			return "";
		else
			return str.substring(pos + separator.length());
	}

	public static String substringBeforeLast(String str, String separator) {
		if (str == null || separator == null || str.length() == 0
				|| separator.length() == 0)
			return str;
		int pos = str.lastIndexOf(separator);
		if (pos == -1)
			return str;
		else
			return str.substring(0, pos);
	}

	public static String substringAfterLast(String str, String separator) {
		if (str == null || str.length() == 0)
			return str;
		if (separator == null || separator.length() == 0)
			return "";
		int pos = str.lastIndexOf(separator);
		if (pos == -1 || pos == str.length() - separator.length())
			return "";
		else
			return str.substring(pos + separator.length());
	}

	public static String substringBetween(String str, String tag) {
		return substringBetween(str, tag, tag, 0);
	}

	public static String substringBetween(String str, String open, String close) {
		return substringBetween(str, open, close, 0);
	}

	public static String substringBetween(String str, String open,
			String close, int fromIndex) {
		if (str == null || open == null || close == null)
			return null;
		int start = str.indexOf(open, fromIndex);
		if (start != -1) {
			int end = str.indexOf(close, start + open.length());
			if (end != -1)
				return str.substring(start + open.length(), end);
		}
		return null;
	}

	public static String deleteWhitespace(String str) {
		if (str == null)
			return null;
		int sz = str.length();
		StringBuffer buffer = new StringBuffer(sz);
		for (int i = 0; i < sz; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				buffer.append(str.charAt(i));

		return buffer.toString();
	}

	public static String replaceOnce(String text, String repl, String with) {
		return replace(text, repl, with, 1);
	}

	public static String replace(String text, String repl, String with) {
		return replace(text, repl, with, -1);
	}

	public static String replace(String text, String repl, String with, int max) {
		if (text == null || repl == null || with == null || repl.length() == 0
				|| max == 0)
			return text;
		StringBuffer buf = new StringBuffer(text.length());
		int start = 0;
		int end = 0;
		do {
			if ((end = text.indexOf(repl, start)) == -1)
				break;
			buf.append(text.substring(start, end)).append(with);
			start = end + repl.length();
		} while (--max != 0);
		buf.append(text.substring(start));
		return buf.toString();
	}

	public static String replaceChars(String str, char searchChar,
			char replaceChar) {
		if (str == null)
			return null;
		else
			return str.replace(searchChar, replaceChar);
	}

	public static String replaceChars(String str, String searchChars,
			String replaceChars) {
		if (str == null || str.length() == 0 || searchChars == null
				|| searchChars.length() == 0)
			return str;
		char chars[] = str.toCharArray();
		int len = chars.length;
		boolean modified = false;
		int i = 0;
		for (int isize = searchChars.length(); i < isize; i++) {
			char searchChar = searchChars.charAt(i);
			if (replaceChars == null || i >= replaceChars.length()) {
				int pos = 0;
				for (int j = 0; j < len; j++)
					if (chars[j] != searchChar)
						chars[pos++] = chars[j];
					else
						modified = true;

				len = pos;
				continue;
			}
			for (int j = 0; j < len; j++)
				if (chars[j] == searchChar) {
					chars[j] = replaceChars.charAt(i);
					modified = true;
				}

		}

		if (!modified)
			return str;
		else
			return new String(chars, 0, len);
	}

	public static String overlay(String str, String overlay, int start, int end) {
		if (str == null)
			return null;
		if (overlay == null)
			overlay = "";
		int len = str.length();
		if (start < 0)
			start = 0;
		if (start > len)
			start = len;
		if (end < 0)
			end = 0;
		if (end > len)
			end = len;
		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
		return (new StringBuffer(((len + start) - end) + overlay.length() + 1))
				.append(str.substring(0, start)).append(overlay)
				.append(str.substring(end)).toString();
	}

	public static String chomp(String str) {
		if (str == null || str.length() == 0)
			return str;
		if (str.length() == 1) {
			char ch = str.charAt(0);
			if (ch == '\r' || ch == '\n')
				return "";
			else
				return str;
		}
		int lastIdx = str.length() - 1;
		char last = str.charAt(lastIdx);
		if (last == '\n') {
			if (str.charAt(lastIdx - 1) == '\r')
				lastIdx--;
		} else if (last != '\r')
			lastIdx++;
		return str.substring(0, lastIdx);
	}

	public static String chomp(String str, String separator) {
		if (str == null || str.length() == 0 || separator == null)
			return str;
		if (str.endsWith(separator))
			return str.substring(0, str.length() - separator.length());
		else
			return str;
	}

	public static String chop(String str) {
		if (str == null)
			return null;
		int strLen = str.length();
		if (strLen < 2)
			return "";
		int lastIdx = strLen - 1;
		String ret = str.substring(0, lastIdx);
		char last = str.charAt(lastIdx);
		if (last == '\n' && ret.charAt(lastIdx - 1) == '\r')
			return ret.substring(0, lastIdx - 1);
		else
			return ret;
	}

	public static String repeat(String str, int repeat) {
		if (str == null)
			return null;
		if (repeat <= 0)
			return "";
		int inputLength = str.length();
		if (repeat == 1 || inputLength == 0)
			return str;
		int outputLength = inputLength * repeat;
		switch (inputLength) {
		case 1: // '\001'
			char ch = str.charAt(0);
			char output1[] = new char[outputLength];
			for (int i = repeat - 1; i >= 0; i--)
				output1[i] = ch;

			return new String(output1);

		case 2: // '\002'
			char ch0 = str.charAt(0);
			char ch1 = str.charAt(1);
			char output2[] = new char[outputLength];
			for (int i = repeat * 2 - 2; i >= 0; i--) {
				output2[i] = ch0;
				output2[i + 1] = ch1;
				i--;
			}

			return new String(output2);
		}
		StringBuffer buf = new StringBuffer(outputLength);
		for (int i = 0; i < repeat; i++)
			buf.append(str);

		return buf.toString();
	}

	public static String alignLeft(String str, int size) {
		return alignLeft(str, size, ' ');
	}

	public static String alignLeft(String str, int size, char padChar) {
		if (str == null)
			return null;
		int pads = size - str.length();
		if (pads <= 0)
			return str;
		else
			return alignLeft(str, size, String.valueOf(padChar));
	}

	public static String alignLeft(String str, int size, String padStr) {
		if (str == null)
			return null;
		if (padStr == null || padStr.length() == 0)
			padStr = " ";
		int padLen = padStr.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0)
			return str;
		if (pads == padLen)
			return str.concat(padStr);
		if (pads < padLen)
			return str.concat(padStr.substring(0, pads));
		char padding[] = new char[pads];
		char padChars[] = padStr.toCharArray();
		for (int i = 0; i < pads; i++)
			padding[i] = padChars[i % padLen];

		return str.concat(new String(padding));
	}

	public static String alignRight(String str, int size) {
		return alignRight(str, size, ' ');
	}

	public static String alignRight(String str, int size, char padChar) {
		if (str == null)
			return null;
		int pads = size - str.length();
		if (pads <= 0)
			return str;
		else
			return alignRight(str, size, String.valueOf(padChar));
	}

	public static String alignRight(String str, int size, String padStr) {
		if (str == null)
			return null;
		if (padStr == null || padStr.length() == 0)
			padStr = " ";
		int padLen = padStr.length();
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0)
			return str;
		if (pads == padLen)
			return padStr.concat(str);
		if (pads < padLen)
			return padStr.substring(0, pads).concat(str);
		char padding[] = new char[pads];
		char padChars[] = padStr.toCharArray();
		for (int i = 0; i < pads; i++)
			padding[i] = padChars[i % padLen];

		return (new String(padding)).concat(str);
	}

	public static String center(String str, int size) {
		return center(str, size, ' ');
	}

	public static String center(String str, int size, char padChar) {
		if (str == null || size <= 0)
			return str;
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str;
		} else {
			str = alignRight(str, strLen + pads / 2, padChar);
			str = alignLeft(str, size, padChar);
			return str;
		}
	}

	public static String center(String str, int size, String padStr) {
		if (str == null || size <= 0)
			return str;
		if (padStr == null || padStr.length() == 0)
			padStr = " ";
		int strLen = str.length();
		int pads = size - strLen;
		if (pads <= 0) {
			return str;
		} else {
			str = alignRight(str, strLen + pads / 2, padStr);
			str = alignLeft(str, size, padStr);
			return str;
		}
	}

	public static String reverse(String str) {
		if (str == null || str.length() == 0)
			return str;
		else
			return (new StringBuffer(str)).reverse().toString();
	}

	public static String abbreviate(String str, int maxWidth) {
		return abbreviate(str, 0, maxWidth);
	}

	public static String abbreviate(String str, int offset, int maxWidth) {
		if (str == null)
			return null;
		if (maxWidth < 4)
			maxWidth = 4;
		if (str.length() <= maxWidth)
			return str;
		if (offset > str.length())
			offset = str.length();
		if (str.length() - offset < maxWidth - 3)
			offset = str.length() - (maxWidth - 3);
		if (offset <= 4)
			return (new StringBuilder()).append(str.substring(0, maxWidth - 3))
					.append("...").toString();
		if (maxWidth < 7)
			maxWidth = 7;
		if (offset + (maxWidth - 3) < str.length())
			return (new StringBuilder()).append("...")
					.append(abbreviate(str.substring(offset), maxWidth - 3))
					.toString();
		else
			return (new StringBuilder()).append("...")
					.append(str.substring(str.length() - (maxWidth - 3)))
					.toString();
	}

	public static String difference(String str1, String str2) {
		if (str1 == null)
			return str2;
		if (str2 == null)
			return str1;
		int index = indexOfDifference(str1, str2);
		if (index == -1)
			return "";
		else
			return str2.substring(index);
	}

	public static int indexOfDifference(String str1, String str2) {
		if (str1 == null || str2 == null || str1.equals(str2))
			return -1;
		int i;
		for (i = 0; i < str1.length() && i < str2.length()
				&& str1.charAt(i) == str2.charAt(i); i++)
			;
		if (i < str2.length() || i < str1.length())
			return i;
		else
			return -1;
	}

	public static int getLevenshteinDistance(String s, String t) {
		s = defaultIfNull(s);
		t = defaultIfNull(t);
		int n = s.length();
		int m = t.length();
		if (n == 0)
			return m;
		if (m == 0)
			return n;
		int d[][] = new int[n + 1][m + 1];
		for (int i = 0; i <= n; i++)
			d[i][0] = i;

		for (int j = 0; j <= m; j++)
			d[0][j] = j;

		for (int i = 1; i <= n; i++) {
			char s_i = s.charAt(i - 1);
			for (int j = 1; j <= m; j++) {
				char t_j = t.charAt(j - 1);
				int cost;
				if (s_i == t_j)
					cost = 0;
				else
					cost = 1;
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
						+ cost);
			}

		}

		return d[n][m];
	}

	private static int min(int a, int b, int c) {
		if (b < a)
			a = b;
		if (c < a)
			a = c;
		return a;
	}

	public static final String EMPTY_STRING = "";
	private static final WordTokenizer CAMEL_CASE_TOKENIZER = new WordTokenizer() {

		protected void startSentence(StringBuffer buffer, char ch) {
			buffer.append(Character.toLowerCase(ch));
		}

		protected void startWord(StringBuffer buffer, char ch) {
			if (!isDelimiter(buffer.charAt(buffer.length() - 1)))
				buffer.append(Character.toUpperCase(ch));
			else
				buffer.append(Character.toLowerCase(ch));
		}

		protected void inWord(StringBuffer buffer, char ch) {
			buffer.append(Character.toLowerCase(ch));
		}

		protected void startDigitSentence(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

		protected void startDigitWord(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

		protected void inDigitWord(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

		protected void inDelimiter(StringBuffer buffer, char ch) {
			if (ch != '_')
				buffer.append(ch);
		}

	};
	private static final WordTokenizer PASCAL_CASE_TOKENIZER = new WordTokenizer() {

		protected void startSentence(StringBuffer buffer, char ch) {
			buffer.append(Character.toUpperCase(ch));
		}

		protected void startWord(StringBuffer buffer, char ch) {
			buffer.append(Character.toUpperCase(ch));
		}

		protected void inWord(StringBuffer buffer, char ch) {
			buffer.append(Character.toLowerCase(ch));
		}

		protected void startDigitSentence(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

		protected void startDigitWord(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

		protected void inDigitWord(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

		protected void inDelimiter(StringBuffer buffer, char ch) {
			if (ch != '_')
				buffer.append(ch);
		}

	};
	private static final WordTokenizer UPPER_CASE_WITH_UNDERSCORES_TOKENIZER = new WordTokenizer() {

		protected void startSentence(StringBuffer buffer, char ch) {
			buffer.append(Character.toUpperCase(ch));
		}

		protected void startWord(StringBuffer buffer, char ch) {
			if (!isDelimiter(buffer.charAt(buffer.length() - 1)))
				buffer.append('_');
			buffer.append(Character.toUpperCase(ch));
		}

		protected void inWord(StringBuffer buffer, char ch) {
			buffer.append(Character.toUpperCase(ch));
		}

		protected void startDigitSentence(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

		protected void startDigitWord(StringBuffer buffer, char ch) {
			if (!isDelimiter(buffer.charAt(buffer.length() - 1)))
				buffer.append('_');
			buffer.append(ch);
		}

		protected void inDigitWord(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

		protected void inDelimiter(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

	};
	private static final WordTokenizer LOWER_CASE_WITH_UNDERSCORES_TOKENIZER = new WordTokenizer() {

		protected void startSentence(StringBuffer buffer, char ch) {
			buffer.append(Character.toLowerCase(ch));
		}

		protected void startWord(StringBuffer buffer, char ch) {
			if (!isDelimiter(buffer.charAt(buffer.length() - 1)))
				buffer.append('_');
			buffer.append(Character.toLowerCase(ch));
		}

		protected void inWord(StringBuffer buffer, char ch) {
			buffer.append(Character.toLowerCase(ch));
		}

		protected void startDigitSentence(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

		protected void startDigitWord(StringBuffer buffer, char ch) {
			if (!isDelimiter(buffer.charAt(buffer.length() - 1)))
				buffer.append('_');
			buffer.append(ch);
		}

		protected void inDigitWord(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

		protected void inDelimiter(StringBuffer buffer, char ch) {
			buffer.append(ch);
		}

	};

}

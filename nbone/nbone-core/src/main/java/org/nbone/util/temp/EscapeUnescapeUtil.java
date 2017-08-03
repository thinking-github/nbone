package org.nbone.util.temp;

/**
 * <li>Title: EscapeUnescapeUtil.java</li>
 * <li>Project: 050009-bx_gd</li>
 * <li>Description: </li>
 * 
 * @version 1.0 download from internet
 */
public class EscapeUnescapeUtil
{
	/**
	 * 转码
	 * 
	 * @param src
	 * @return
	 */
	public static String escape ( String src )
	{
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		// 遍历，对源字符串每一位进行转码
		for (i = 0; i < src.length(); i++)
		{
			j = src.charAt(i);
			// 数字，字符不需要转码
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			// ascil码转码
			else if (j < 256)
			{
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else
			// 其他转码
			{
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * 解码
	 * 
	 * @param src
	 * @return
	 */
	public static String unescape ( String src )
	{
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		// 查找%,进行解码
		while (lastPos < src.length())
		{
			pos = src.indexOf("%", lastPos);
			// 是经过转玛的字符
			if (pos == lastPos)
			{
				if (src.charAt(pos + 1) == 'u')// 是中文
				{
					ch = (char) Integer.parseInt(src
							.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else
				// 是ascil码
				{
					ch = (char) Integer.parseInt(src
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else
			// 不需要解码
			{
				if (pos == -1)
				{
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else
				{
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static void main ( String[] args )
	{
		String tmp = "%u4e2d";
		System.out.println("testing escape : " + tmp);
		tmp = escape(tmp);
		System.out.println(tmp);
		System.out.println("testing unescape :" + tmp);
		System.out.println(unescape(tmp));
	}
}
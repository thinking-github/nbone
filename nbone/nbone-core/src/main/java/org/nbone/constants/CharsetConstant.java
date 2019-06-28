package org.nbone.constants;

import java.nio.charset.Charset;
/**
 * 字符编码常量
 * @author thinking
 * @version 1.0
 * @since 2013-12-12
 * @see com.google.common.base.Charsets
 *
 */
public interface CharsetConstant {
	
	public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset ASCII = Charset.forName("US-ASCII");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    
    public static final Charset GBK = Charset.forName("GBK");
    public static final Charset GB2312 = Charset.forName("GB2312");

	/**
	 * UTF-8-BOM 头编码
	 */
	public static final byte[] UTF_8_BOM_HEAD = new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF };
    
    /**
	 * 字符编码
	 */
	public final static String CHARSET_ISO_8859_1 ="ISO-8859-1";
	public final static String CHARSET_UTF8 ="UTF-8";
	public final static String CHARSET_UTF8_BOM ="UTF-8-BOM";
	public final static String CHARSET_GBK = "GBK";
	public final static String CHARSET_GB2312 = "GB2312";

}

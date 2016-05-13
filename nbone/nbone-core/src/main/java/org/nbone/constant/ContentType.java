package org.nbone.constant;

/**
 * 
 * @author thinking 2014-10-10
 * @since  2014-10-10
 * @see org.springframework.http.MediaType
 * @see org.springframework.http.HttpHeaders  
 * @see org.springframework.util.MimeTypeUtils
 * @see com.google.common.net.MediaType
 * @see com.google.common.net.HttpHeaders
 *
 */
public interface ContentType extends CharsetConstant {
	
	/**
	 * 图片文件
	 */
	public final static String JPG ="image/jpeg";
	public final static String GIF ="image/gif";
	public final static String PNG ="image/png";
	public final static String BMP ="image/bmp";
	
	/**
	 * 普通的文本文件
	 */
	public final static String TXT  ="text/plain";
	public final static String JAVA ="text/plain";
	
	/**
	 * 办公文件
	 */
	public final static String PDF ="application/pdf";
	public final static String DOC ="application/msword";
	public final static String XLS ="application/x-msexcel";
	public final static String PPT ="application/powerpoint";
	
	/**
	 * WEB 文件
	 */
	public final static String APPLICATION_XML ="application/xml";
	public final static String APPLICATION_JSON_VALUE = "application/json";
	
	public final static String TEXT_HTML ="text/html";
	public final static String TEXT_JSON ="text/json";
	public final static String TEXT_XML  = "text/xml";
	
	/**
	 * 字节流
	 */
	public final static  String BIN = "application/octet-stream";
	
	public final static  String ZIP = "application/zip";
	
	
	
	public final static String SUB_CHARSET_PREFIX = ";charset=";
	
	/**
	 * "text/json;charset=utf-8"
	 */
	public final static String TEXT_JSON_UTF_8        =new StringBuilder(TEXT_JSON).append(SUB_CHARSET_PREFIX).append(CHARSET_UTF8).toString();
	public final static String APPLICATION_JSON_UTF_8 =new StringBuilder(APPLICATION_JSON_VALUE).append(SUB_CHARSET_PREFIX).append(CHARSET_UTF8).toString();
	

}

package org.nbone.util;

import java.io.*;
import java.sql.Date;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.constants.ContentType;

/**
 * 
 * @author thinking  
 * @since 2014-10-24
 *
 */
public abstract class WebIOUtils extends IOUtils implements ContentType{
	
	protected Log logger = LogFactory.getLog(WebIOUtils.class);
	
	public final static String DEFAULT_ECODING =  CHARSET_UTF8;
	
	public final static String SUB_CHARSET =";charset";
	
	/**
	 * 设置  response 
	 * @param response {@link ServletResponse}
	 * @param contentType  {@link ContentType}  默认为{@link ContentType#TEXT_HTML}
	 * @param encoding     编码  默认为UTF-8
	 * @return
	 */
	public static ServletResponse setResponse(ServletResponse response,String contentType,String encoding){
		if(response == null){
			return null;
		}
		response.reset();
		String type = contentType;
		String charset = encoding;
		if(contentType == null){
			type = TEXT_HTML;
		}
		if(encoding == null){
			charset = DEFAULT_ECODING;
		}
		if(type.indexOf(SUB_CHARSET) == -1){
			type = type + ";charset=" + charset;
		}
		response.setContentType(type);
		response.setCharacterEncoding(charset);
		return response;
	}

	/**
	 * 设置附件名称和内容类型
	 * @param response
	 * @param contentType
	 * @param encoding
	 * @param fileName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static HttpServletResponse attachmentName(HttpServletResponse response,String contentType,String encoding,String fileName) throws UnsupportedEncodingException {
		if(response == null){
			return null;
		}
		setResponse(response,contentType,encoding);
		if(encoding == null){
			encoding = DEFAULT_ECODING;
		}
		//HttpServletResponse 添加 文件头
		//展现下载另存在
		if(fileName != null){
		    // response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("gb2312"),CHARSET_ISO_8859_1));
			response.addHeader("Content-Disposition", "attachment;filename=" +  new String(fileName.getBytes(),encoding));
		}

		return response;
	}


	/**
	 * 输出资源（下载）
	 * @param response ServletResponse
	 * @param resourceType {@link ContentType}
	 * @param ins 输入流
	 * @see #exportResource(ServletResponse,String,InputStream, String)
	 */
	public static void exportResource(ServletResponse response,String resourceType,InputStream ins){
		exportResource(response,resourceType,ins, null);
	}
	/**
	 * 输出资源（下载）
	 * @param response ServletResponse
	 * @param resourceType {@link ContentType}
	 * @param bin 字节数组
	 */
	public static void exportResource(ServletResponse response,String resourceType,byte[] bin){
		setResponse(response,resourceType, DEFAULT_ECODING);
		OutputStream os = null ;
		try {
			os = response.getOutputStream();
			os.write(bin);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(os);
		}
		
	}
	
	/**
	 * 输出资源
	 * @param response {@link ServletResponse}
	 * 
	 * @param resourceType {@link ContentType}
	 * 
	 * @param ins      {@link InputStream}
	 * 
	 * @param fileName   文件名称(包含扩展名),可为null 当为null 时输出方案不同
	 */
	public static void exportResource(ServletResponse response,String resourceType,InputStream ins,String fileName) {
		HttpServletResponse response1 = (HttpServletResponse) response;
		
		OutputStream os = null ;
		try {
			attachmentName(response1,resourceType,DEFAULT_ECODING,fileName);
			os = response1.getOutputStream();
			saveWrite(ins,os);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close(os);
		}
		
	}
	
	/**
	 * 根据资源类型获取相应的输出流
	 * @param response {@link ServletResponse}
	 * @param resourceType {@link ContentType}
	 * @param fileName 文件名称(包含扩展名),可为null 当为null 时输出方案不同
	 * @return
	 */
	public static OutputStream getOutputStream(HttpServletResponse response,String resourceType,String fileName) throws IOException {
        attachmentName(response,resourceType,DEFAULT_ECODING,fileName);
		return response.getOutputStream();
		
	}

    /**
     * 根据资源类型获取相应的字符输出流
     * @param response
     * @param resourceType
     * @param fileName
     * @return
     * @throws IOException
     */
	public static Writer getWriter(HttpServletResponse response, String resourceType, String fileName) throws IOException {
	    attachmentName(response,resourceType,DEFAULT_ECODING,fileName);
		return response.getWriter();

	}
	/**
	 * 
	 * @param response
	 * @return
	 * @see #getOutputStream(HttpServletResponse, String, String)
	 */
	public static OutputStream getImageOutputStream(HttpServletResponse response) throws IOException{
		OutputStream os = getOutputStream(response, JPG, null);
		return os;
		
	}
	
	/**
	 * 
	 * @param response
	 * @param fileName
	 * @return
	 * @see #getOutputStream(HttpServletResponse, String, String)
	 */
	public static OutputStream getExcelOutputStream(HttpServletResponse response,String fileName) throws IOException{
		OutputStream os = getOutputStream(response, XLS, fileName);
		return os;
		
	}
	
	/**
	 * 
	 * @param response
	 * @param fileName
	 * @return
	 * @see #getOutputStream(HttpServletResponse, String, String)
	 */
	public static OutputStream getTxtOutputStream(HttpServletResponse response,String fileName) throws IOException{
		OutputStream os = getOutputStream(response, TXT, fileName);;
		
		return os;
		
	}
	
	

}

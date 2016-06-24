package org.nbone.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.nbone.constants.ContentType;

/**
 * 
 * @author Thinking  2014-10-24
 *
 */
public abstract class WebIOUtils extends IOUtils implements ContentType{
	
	public final static String DEFAULT_ECODING ="UTF-8";
	
	public final static String SUB_CHARSET =";charset";
	
	/**
	 * 设置  response 
	 * @param response {@link ServletResponse}
	 * @param contentType  {@link ContentType}
	 * @param encoding     编码
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
	 * 输出资源（下载）
	 * @param response ServletResponse
	 * @param resourceType {@link ContentType}
	 * @param ins 输入流
	 * @see #exportResource(ServletResponse, InputStream, String, String)
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
		response = setResponse(response,resourceType,null);
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
	public static void exportResource(ServletResponse response,String resourceType,InputStream ins,String fileName){
		
		response = setResponse(response,resourceType,null);
		
		HttpServletResponse response1 = (HttpServletResponse) response;
		
		OutputStream os = null ;
		
		try {
			
			if(fileName != null){
				//HttpServletResponse 添加 文件头
				//展现下载另存在
				response1.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(),CHARSET_ISO_8859_1));
			}
			
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
	public static OutputStream getOutputStream(ServletResponse response,String resourceType,String fileName){
		response = setResponse(response, resourceType,null);
		HttpServletResponse response1 = (HttpServletResponse) response;
		OutputStream os = null;
		try {
			if(fileName != null){
				//HttpServletResponse 添加 文件头
				//展现下载另存在
				//response1.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(),CHARSET_ISO_8859_1));
				response1.addHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("gb2312"), "iso8859-1") + ".xls");
			}
			os = response1.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os;
		
	}
	/**
	 * 
	 * @param response
	 * @return
	 * @see #getOutputStream(ServletResponse, String, String)
	 */
	public static OutputStream getImageOutputStream(ServletResponse response){
		OutputStream os = getOutputStream(response, JPG, null);
		return os;
		
	}
	
	/**
	 * 
	 * @param response
	 * @param fileName
	 * @return
	 * @see #getOutputStream(ServletResponse, String, String)
	 */
	public static OutputStream getExcelOutputStream(ServletResponse response,String fileName){
		OutputStream os = getOutputStream(response, XLS, fileName);
		return os;
		
	}
	
	/**
	 * 
	 * @param response
	 * @param fileName
	 * @return
	 * @see #getOutputStream(ServletResponse, String, String)
	 */
	public static OutputStream getTxtOutputStream(ServletResponse response,String fileName){
		OutputStream os = getOutputStream(response, TXT, fileName);;
		
		return os;
		
	}
	
	

}

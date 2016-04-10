package org.nbone.util.http;



import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * HttpClient3系列集成使用<br>
 * 
 *  1、GET方式
	第一步、创建一个客户端，类似于你用浏览器打开一个网页
	HttpClient httpClient = new HttpClient();
	第二步、创建一个GET方法，用来获取到你需要抓取的网页URL
	GetMethod getMethod = new GetMethod("http://www.baidu.com");
	第三步、获得网址的响应状态码，200表示请求成功
	int statusCode = httpClient.executeMethod(getMethod);
	第四步、获取网页的源码
	byte[] responseBody = getMethod.getResponseBody();	
	
	
	
 * @author Thinking  2014-11-20
 * @since  2014-11-20
 * 
 */
public class HttpC3Utils {
	
	private static Log logger = LogFactory.getLog(HttpC3Utils.class);
	public static final String URL_NET_SERVER_ERROR = "目标地址错误/网络异常/目标服务停止,请及时检查.";
	
	public static HttpClient getClient(int timeout){
		 HttpClient client = new HttpClient();
		 client.setConnectionTimeout(timeout);
		return client;
	}
	
	public static HttpClient getClient(){
		 HttpClient client = new HttpClient();
		return client;
	}
	public static  boolean isServerStart(String url){
		return isServerStart(url, 5000);
	}
	public static  boolean isServerStart(String url,int timeout){
       long start = System.currentTimeMillis();
	   HttpClient client = getClient();
	   
	   if(timeout > 0 ){
		   client.setConnectionTimeout(timeout);
	   }
	   HttpMethod method = doGet(client,url);
       //状态码
       int  statusCode = 0 ;
	    try {
	    	statusCode = method.getStatusCode();
		} catch (Exception e) {
			logger.error(URL_NET_SERVER_ERROR, e);
		}
       
	    close(method);
	    close(client);
	    long end = System.currentTimeMillis();
	    long XX  = end - start;
	    logger.info(new StringBuilder().append("[").append(url).append("]").append(" 访问时间:").append(XX).append("ms"));
	   return statusCode == HttpStatus.SC_OK;
	  }
	
	//************************************GET 集成********************************************
	public static HttpMethod  doGet(HttpClient client,String url){
		if(client == null){
			throw new NullPointerException("HttpClient is not null.");
		}
		HttpMethod method = new GetMethod(url);
		try {
			int statusCode = client.executeMethod(method);
			if(statusCode != HttpStatus.SC_OK){
				logger.error(method.getStatusLine());
				logger.error(new StringBuilder("此服务不能访问:").append(statusCode).append("错误!URL:").append(url));
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return method;
	}
	
	/**
	 * get集成调用方法
	 * @param url 请求地址
	 * @param isClose 为true时手动关闭连接，否则自动关闭连接
	 * @return
	 */
	public static String  doGetAsString(String url,boolean isClose){
		String responseText = null;
		HttpClient client = getClient();
		HttpMethod method = doGet(client, url);
		try {
			//系统自动获取  response headr charset 进行转化
			responseText = method.getResponseBodyAsString();
			if(isClose){
				close(method);
				close(client);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return responseText;
	}
	/**
	 * @see #doGetAsString(String, boolean)
	 */
	public static String  doGetAsString(String url){
		return doGetAsString(url, false);
		
	}
	
	//***********************************POST集成*************************************************
	
	public static HttpMethod  doPost(HttpClient client,String url,NameValuePair[] params){
		if(client == null){
			throw new NullPointerException("HttpClient is not null.");
		}
		PostMethod method = new PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());  
		method.setRequestBody(params);
		try {
			int statusCode = client.executeMethod(method);
			if(statusCode != HttpStatus.SC_OK){
				logger.error(method.getStatusLine());
				logger.error(new StringBuilder("此服务不能访问:").append(statusCode).append("错误!URL:").append(url));
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return method;
	}
	
	/**
	 * post集成调用方法
	 * @param url
	 * @param params 参数数组
	 * @param isClose 为true时手动关闭连接，否则自动关闭连接
	 * @return
	 */
	public static String  doPostAsString(String url,NameValuePair[] params,boolean isClose){
		String responseText = null;
		HttpClient client = getClient();
		HttpMethod method = doPost(client, url, params);
		try {
			responseText = method.getResponseBodyAsString();
			if(isClose){
				close(method);
				close(client);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return responseText;
	}
	/**
	 * @see #doPostAsString(String, NameValuePair[], boolean)
	 */
	public static String  doPostAsString(String url,NameValuePair[] params){
		
		return doPostAsString(url, params, false);
	}
	
	//
	public static void  close(HttpMethod method){
		 if (method != null){
	    	  method.releaseConnection();
	      }
	}
	public static void  close(HttpClient client){
		 if (client != null){
			 //((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		       client.getHttpConnectionManager().closeIdleConnections(0);
	      }
	}
		

}

package org.nbone.util.http;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * HttpClient4系列集成使用<br><br>
 * 
 *  1、GET方式
	第一步、创建一个客户端，类似于你用浏览器打开一个网页
	 HttpClient client = new DefaultHttpClient();
	第二步、创建一个GET方法，用来获取到你需要抓取的网页URL
	HttpGet get = new HttpGet("http://www.baidu.com");  
	第三步、获得网址的响应状态码，200表示请求成功
	response = client.execute(get);
	第四步、获取返回实体
	HttpEntity entity = response.getEntity(); 
	第五步、获取返回实体
	String responseText = EntityUtils.toString(entity,"UTF-8");
	第六步、销毁资源
	 EntityUtils.consume(entity);
 * @author Thinking  2014-11-20
 * @since 2014-11-20
 *
 */
public class HttpC4Utils {
	
	private static Log logger = LogFactory.getLog(HttpC4Utils.class);
	public static final String URL_NET_SERVER_ERROR = "目标地址错误/网络异常/目标服务停止,请及时检查.";
	public static String charset = "UTF-8";
	
	/**
	 * 设置http请求参数  例如连接超时 
	 */
	public static RequestConfig requestConfig;
	
	public static CloseableHttpClient getClient(){
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 //当使用InternalHttpClient 创建客户端实例时不能使用 client.getParams()，此类 未实现此方法
		 //httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,timeout);
		return httpclient;
	}
	
	public static RequestConfig customRequestConfig(){
		
		RequestConfig config = RequestConfig.custom()
				.setSocketTimeout(1000)
				.setConnectTimeout(5000)
				.build();
		return config;
	}
	
	public static void setRequestConfig(RequestConfig config){
		requestConfig = config;
	}
	
	
	public static  boolean isServerStart(String url){
		return isServerStart(url,5000);
	}
	
	public static  boolean isServerStart(String url,int timeout){
		   long start = System.currentTimeMillis();
		   CloseableHttpClient client = getClient();
		   CloseableHttpResponse response = null;
		   int  statusCode = 0 ;
		   try {
			   if(timeout > 0 ){
				   
					RequestConfig config = RequestConfig.custom()
							.setConnectTimeout(timeout)
							.build();
					requestConfig = config;
			   }
			   
		       response = doGet(client,url);
		       statusCode = getStatusCode(response);
			
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				  HttpClientUtils.closeQuietly(response);
			      HttpClientUtils.closeQuietly(client);
			      requestConfig = null;
			}
	     
		    long end = System.currentTimeMillis();
		    long XX  = end - start;
		    logger.info(new StringBuilder().append("[").append(url).append("]").append(" 访问时间:").append(XX).append("ms"));
		    
		   return statusCode == HttpStatus.SC_OK;
		  }
	//************************************GET 集成********************************************
	
	public static CloseableHttpResponse  doGet(CloseableHttpClient client, String url){
		if(client == null){
			throw new NullPointerException("HttpClient is not null.");
		}
		HttpGet httpget = new HttpGet(url);  
		if(requestConfig != null){
			httpget.setConfig(requestConfig);
		}
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpget);
			int statusCode = getStatusCode(response);
			if(statusCode != HttpStatus.SC_OK){
				logger.error(response.getStatusLine());
				logger.error(new StringBuilder("此服务不能访问:").append(statusCode).append("错误!URL:").append(url));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * get集成调用方法 
	 * @param url     请求地址
	 * @param charset 设置转化字符串的字符集，可为null,为null时使用 默认编码
	 * @param isClose 为true时手动关闭连接，否则自动关闭连接
	 * @return
	 */
	public static String  doGetAsString(String url,String charset,boolean isClose){
		String responseText = null;
		CloseableHttpClient client = getClient();
		CloseableHttpResponse response = doGet(client, url);
		  try {
			// 获取响应实体
			HttpEntity entity = response.getEntity(); 
		    if(entity != null){
				 long   responseLength  = entity.getContentLength();
				 
				    //1.系统优先使用  response headr charset 进行转化
				    //2.次之 使用 用户设置
				    //3.最后使用默认编码
				    responseText = EntityUtils.toString(entity,charset);
				    //销毁资源
				    EntityUtils.consume(entity);
		    }
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}finally{
				 HttpClientUtils.closeQuietly(response);
			   if(isClose){
				 HttpClientUtils.closeQuietly(client);
			   }
			}
		
		return responseText;
	}
	
	/**
	 * @see #doGetAsString(String, String, boolean)
	 */
	public static String  doGetAsString(String url,String charset){
		return doGetAsString(url, charset, false);
	}
	
	/**
	 * @see #doGetAsString(String, String, boolean)
	 */
	public static String  doGetAsString(String url,boolean isClose){
		return doGetAsString(url, charset, isClose);
	}
	
	/**
	 * @see #doGetAsString(String, String, boolean)
	 */
	public static String  doGetAsString(String url){
		return doGetAsString(url, charset, false);
	}
	

	
	
	//************************************POST 集成********************************************
	
	public static CloseableHttpResponse  doPost(CloseableHttpClient client,String url,List<NameValuePair> formParams,String charset){
		if(client == null){
			throw new NullPointerException("HttpClient is not null.");
		}
		HttpPost httppost = new HttpPost(url);  
		
		if(requestConfig != null){
			httppost.setConfig(requestConfig);
		}
		CloseableHttpResponse response = null;
		try {
			//formParams.add(new BasicNameValuePair("user","user"));案例
		    //1.charset 优先使用 用户设置
		    //2.最后使用默认编码
			httppost.setEntity(new UrlEncodedFormEntity(formParams, charset));
			response = client.execute(httppost);
			int statusCode = getStatusCode(response);
			if(statusCode != HttpStatus.SC_OK){
				logger.error(response.getStatusLine());
				logger.error(new StringBuilder("此服务不能访问:").append(statusCode).append("错误!URL:").append(url));
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return response;
	}
	
	
	/**
	 *  post集成调用方法 
	 * @param url 请求地址
	 * @param formParams  请求参数数组
	 * @param charset     设置转化字符串的字符集，可为null,为null时使用 默认编码
	 * @param isClose     为true时手动关闭连接，否则自动关闭连接
	 * @return
	 */
	public static String  doPostAsString(String url,List<NameValuePair> formParams,String charset,boolean isClose){
		String responseText = null;
		CloseableHttpClient client = getClient();
		CloseableHttpResponse response = doPost(client, url, formParams, charset);
		  try {
			// 获取响应实体
			HttpEntity entity = response.getEntity(); 
		    if(entity != null){
				 long   responseLength  = entity.getContentLength();
				    //1.系统优先使用  response headr charset 进行转化
				    //2.次之 使用 用户设置
				    //3.最后使用默认编码
				    responseText = EntityUtils.toString(entity,charset);
				    //销毁资源
				    EntityUtils.consume(entity);
		    }
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}finally{
				 HttpClientUtils.closeQuietly(response);
			   if(isClose){
				 HttpClientUtils.closeQuietly(client);
			   }
			}
		
		return responseText;
	}
	
	/**
	 *@see #doPostAsString(String, List, String, boolean) 
	 */
	public static String  doPostAsString(String url,List<NameValuePair> formParams,String charset){
		return doPostAsString(url, formParams, charset, false);
	}
	/**
	 * @see #doPostAsString(String, List, String, boolean) 
	 */
	public static String  doPostAsString(String url,List<NameValuePair> formParams,boolean isClose){
		return doPostAsString(url, formParams, charset, isClose);
	}
	/**
	 *@see #doPostAsString(String, List, String, boolean)  
	 */
	public static String  doPostAsString(String url,List<NameValuePair> formParams){
		return doPostAsString(url, formParams, charset, false);
	}
	
	//************************************集成********************************************
	/**
	 * 返回状态代码 例如:200  404
	 * @param response
	 * @return
	 */
	public static int  getStatusCode(HttpResponse response){
		 if (response == null){
			 logger.error(URL_NET_SERVER_ERROR);
			 return -1;
	      }
		 int sc = response.getStatusLine().getStatusCode();
		return sc;
	}
	//
	public static void  close1(HttpClient client){
		 if (client == null){
			 return ;
	      }
		 
		 client.getConnectionManager().shutdown();
	}

	
	public static void main(String[] args) {
		
		 System.out.println(isServerStart("http://192.168.0.250:9000/hst"));
		
	}

}

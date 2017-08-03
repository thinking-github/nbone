package org.nbone.message;

import org.nbone.message.mail.exception.MailException;
import org.nbone.message.mail.vo.EmailVo;

/**
 * 
 * 当要发送通知时需实现此接口（邮件通知和短信通知）
 * @author Thinking  2013-09-30
 *
 */
public interface IUseCallback<T,V> {
	
	/**
	 * 发送通知前置回调函数(可以用于参数判断等)
	 * @param vo
	 * @return
	 * {@link EmailVo}
	 */
	public boolean preHandle (T vo);
	
	/**
	 * 发送通知后置回调函数(可以用于保存数据库等)
	 * @param vo
	 * @return
	 * @throws MailException
	 */
	public T postHandle (T vo,V result);
	
	

}

package org.nbone.core.task;


/**
 * 
 * 服务组件（供任务使用的服务组件） 
 * @author thinking
 * @version 1.1
 * @since 2016-12-29
 *
 */
public interface TaskExecutable  extends Runnable {
	
	
	public void execute() throws Exception;
	
	/**
	 * 任务是否可用  启用/禁用
	 * @return true 可用  false 不可用
	 */
	public boolean isAvailable();
	
	/**
	 * 返回任务名称
	 * @return
	 */
	public String getTaskName();

	

}

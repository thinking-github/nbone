package org.nbone.core.task;


/**
 * 调度任务
 * @author thinking
 * @version 1.1
 * @since 2016-12-29
 *
 */
public interface ScheduledExecutor {
	
	/**
	 * 定义周期任务，可以使用spring @Scheduled 定义任务
	 */
	public void schedule();

}

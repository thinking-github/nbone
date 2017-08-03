package org.nbone.core.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础任务抽象
 * <li>当使用Runnable任务时执行顺序 run->execute->doExecute->doExecuteInternal
 * <li>当使用普通任务(Spring task)时执行顺序 execute->doExecute->doExecuteInternal
 * 
 * @author thinking
 * @version 1.1
 * @since 2016-12-29
 *
 */
public abstract class AbstractTaskExecutable implements TaskExecutable {

	protected static final Logger logger = LoggerFactory.getLogger(AbstractTaskExecutable.class);

	@Override
	public void run() {
		try {
			this.execute();
		} catch (Exception e) {
			logger.error(getTaskName() + " -task execute exception.thinking", e);
		}
	}

	
	@Override
	public void execute() throws Exception {

		if (!isAvailable()) {
			logger.warn(getTaskName() + " -task is not available.thinking");
			return;
		}

		this.preExecute();
		try {
			long start = System.currentTimeMillis();
			doExecute();
			long end = System.currentTimeMillis();
			long xx = end - start;
			logger.debug(getTaskName()+ " -task execute time = " + xx);
		} catch (Exception e) {
			this.afterThrowing(e);
		} finally {
			this.afterFinally();
		}

		this.afterExecute();
	}

	/**
	 * 执行任务之前的操作
	 */
	protected abstract void preExecute();

	/**
	 * 执行任务之后（任务执行成功）
	 */
	protected abstract void afterExecute();

	/**
	 * 执行任务出现异常
	 * 
	 * @param ex
	 */
	protected abstract void afterThrowing(Exception ex);

	/**
	 * 执行任务 Finally操作
	 */
	protected void afterFinally() {

	}

	/**
	 * 提供Base Task 扩展 doExecute -> doExecuteInternal
	 */
	protected void doExecute() throws Exception {
		doExecuteInternal();
	}

	/**
	 * 用于扩展调用
	 * 
	 * @throws Exception
	 */
	protected abstract void doExecuteInternal() throws Exception;

}

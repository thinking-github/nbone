package org.nbone.component.logger;

import org.nbone.component.logger.vo.LogVo;

/**
 * 此类只是一个demo 使用这个日志组件时需要重写 <code>AbstractAopLogProcessor</code> class
 * @author Thinking  2014-8-8
 *
 */
public class DefaultAopLogProcessor extends AbstractAopLogProcessor {

	@Override
	public Object[] process(Object[] args,LogVo placeholderLogVo) {
		System.out.println("仅供参考>>>>>>>>>>>>>>>>DefaultAopLogProcessor");
		for (int i = 0; i < args.length; i++) {
			if(args[i] instanceof String){
				System.out.println("args:============"+args[i]);
			}
			
		}
		return args;
	}

	@Override
	public String processException(Throwable throwable) {
		if(throwable != null){
			
			if(throwable instanceof Exception){
				System.out.println("I am Exception");
				Exception e = (Exception) throwable;
				System.out.println(e.getMessage());
			}
			
		}
		return null;
	}

	@Override
	public void saveLog(LogVo logVo) {
		System.out.println("log Save here .");
		System.out.println("日志消息:>>>"+logVo.getDescription());
		
	}

}

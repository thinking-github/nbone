package org.nbone.component.logger;

import org.nbone.component.logger.vo.LogVo;

/**
 * Aop 拦截方法中的参数处理
 * @author Thinking  2014-8-8
 *
 */
public interface AopMethodParamsProcessor {
	
	
	public Object[] process(Object[] args,LogVo placeholderLogVo);

}

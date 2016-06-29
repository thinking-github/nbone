package org.nbone.component.logger;

import java.util.Date;

import org.nbone.component.logger.vo.LogVo;
import org.nbone.constants.NboneConstants;

/**
 * 
 * @author Thinking  2014-8-8
 *
 */
public class LogVoFactory {
	
	
	/**
	 * 返回默认的LogVo 操作成功的
	 * @return
	 */
	public static LogVo getDefaultNewLogVO() {
		
		LogVo logVo = new LogVo(new Date(),NboneConstants.OPERATE_RESULT_SUCCESS);
		
		return logVo;
		
	}
	public static LogVo getOperateFailedNewLogVO() {
		
		LogVo logVo = new LogVo(new Date(),NboneConstants.OPERATE_RESULT_FAILED);
		
		return logVo;
		
	}

}

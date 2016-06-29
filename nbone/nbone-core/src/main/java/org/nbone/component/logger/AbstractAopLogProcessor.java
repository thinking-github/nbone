package org.nbone.component.logger;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nbone.component.logger.config.LoggerConfiguration;
import org.nbone.component.logger.util.BizcLogPropsUtil;
import org.nbone.component.logger.util.LoggerUtils;
import org.nbone.component.logger.vo.LogVo;
import org.nbone.constants.NboneConstants;
import org.nbone.util.json.JSONOperUtils;

import net.sf.json.JSONObject;

/**
 * 日志核心处理器
 * @author Thinking  2014-8-8
 *
 */
public abstract class AbstractAopLogProcessor implements AopMethodParamsProcessor,LoggerStatic{
	
	protected Log logger =  LogFactory.getLog(getClass());
	
	private static  boolean isInited =  false;
	private static  boolean isLogOut =  false;
	protected LoggerContext loggerContext;
	
	//默认处理器
	public void  process(String fullClassName, String methodName, Object[] args,Throwable throwable) {
		if(!isInited){
		   init();	
		}
		if(isLogOut){
			System.out.println("=============================AOP 拦截开始(aop start)===============================");
			
		}
		String objectIdKey = fullClassName+ ID_SUFFIX;
		String objectDescKey = fullClassName+ DESC_SUFFIX;
		String cnmn = fullClassName+"."+methodName;
		
		if(isLogOut){
				System.out.println("拦截方法:"+cnmn);
				
		}
		
		String objectVal = BizcLogPropsUtil.getString(fullClassName);
		String methodVal = BizcLogPropsUtil.getString(cnmn);
		
		String methodIdKey    = cnmn+ ID_SUFFIX;
		String methodOperationTypeKey  = cnmn+ OPERATION_TYPE_SUFFIX;
		String methodDescKey  = cnmn+ DESC_SUFFIX;
		
		String objectIdVal = BizcLogPropsUtil.getString(objectIdKey);
		String objectDescVal = BizcLogPropsUtil.getString(objectDescKey);
		
		String methodIdVal = BizcLogPropsUtil.getString(methodIdKey);
		String methodOperationTypeVal = BizcLogPropsUtil.getString(methodOperationTypeKey);
		String methodDescVal = BizcLogPropsUtil.getString(methodDescKey);
		
		if(logger.isDebugEnabled()){
			logger.debug(">>>>>>>>>>>>>"+new StringBuilder(objectIdKey).append("=").append(objectIdVal));
			logger.debug(">>>>>>>>>>>>>"+new StringBuilder(objectDescKey).append("=").append(objectDescVal));
			logger.debug(">>>>>>>>>>>>>"+new StringBuilder(methodIdKey).append("=").append(methodIdVal));
			logger.debug(">>>>>>>>>>>>>"+new StringBuilder(methodOperationTypeKey).append("=").append(methodOperationTypeVal));
			logger.debug(">>>>>>>>>>>>>"+new StringBuilder(methodDescKey).append("=").append(methodDescVal));
		}
		
		LogVo logVo;
		if(throwable == null){
			logVo = LogVoFactory.getDefaultNewLogVO(); 
		}else{
			logVo = LogVoFactory.getOperateFailedNewLogVO();
		}
		
		//第一种加载方式
		//org.nbone.common.baseOrg.BaseOrgController.ID=001
		//org.nbone.common.baseOrg.BaseOrgController.DESC=组织单位

		if(objectDescVal != null && methodOperationTypeVal != null && methodDescVal != null){
			Map map =loggerContext.getMap(LoggerConstants.LOGGER_OPERATION_TYPE);
			logVo.setOperateType(OperateType.getOperateTypeByCode(LoggerUtils.getMapKeyByValue(map, methodOperationTypeVal)));
			finishCallBack(args, throwable,logVo);
		}else{
			//第二种加载方式
			//org.nbone.common.baseOrg.BaseOrgController=ID:'001',DESC:'组织单位'
			if(objectVal != null && methodVal !=null){
				String tempObjectVal = LoggerUtils.getStandardJSONObject(objectVal);
				String tempMethodVal = LoggerUtils.getStandardJSONObject(methodVal);
				
				Map objectMap = JSONObject.fromObject(tempObjectVal);
				Map methodMap = JSONObject.fromObject(tempMethodVal);
				
				objectIdVal = String.valueOf(objectMap.get(ID));
				objectDescVal = String.valueOf(objectMap.get(DESC));
				
				methodIdVal = String.valueOf(methodMap.get(ID));
				methodOperationTypeVal = String.valueOf(methodMap.get(OPERATION_TYPE));
				methodDescVal = String.valueOf(methodMap.get(DESC));
				//含有重复
				//FIXME:陈依成
				Map map =loggerContext.getMap(LoggerConstants.LOGGER_OPERATION_TYPE);
				logVo.setOperateType(OperateType.getOperateTypeByCode(LoggerUtils.getMapKeyByValue(map, methodOperationTypeVal)));
				finishCallBack(args, throwable,logVo);
				
				
			}else{
				logger.warn(new StringBuilder("此方法没有配置:[").append(cnmn).append("]."));
			}
			
		}
	   if(isLogOut){
			System.out.println("=============================AOP 拦截结束(aop end)===============================");
			
	   }
		
	}
	

  
	 //FIXME:此方法需要重写 获取业务系统的异常消息
    public abstract String processException(Throwable throwable);
    
    //FIXME:此方法需要重写 保存日志去向（一般我们保存至数据库）
    public abstract void saveLog(LogVo logVo );
    
    
    /**
     * 完成回调函数的执行
     * @param args
     * @param throwable
     * @see #process(Object[])
     * @see #processException(Throwable)
     * @see #saveLog(LogVo)
     */
    public  void finishCallBack(Object[] args,Throwable throwable,LogVo logVo){
    	//参数处理回调函数
		//FIXME:核心位置需要重新实现（此处理器处于中间位置）
		process(args,logVo);
		
		//异常处理回调函数
		String errMsg = processException(throwable);
		logVo = setFinalDescription(logVo,errMsg);
		
		//保存位置回调函数
		saveLog(logVo);
    	
    }
    //再一次修改操作消息（最终的）
    private LogVo  setFinalDescription(LogVo logVo,String errMsg){
    	if(logVo.getOperateResult()==NboneConstants.OPERATE_RESULT_SUCCESS){
    		logVo.setDescription(logVo.getDescription()+";操作成功.");
    	}else{
    		logVo.setDescription(logVo.getDescription()+";操作失败,原因: "+errMsg);
    	}
		return logVo;
    }
    
    public void  init(){
    	LoggerConfiguration configuration  = new LoggerConfiguration();
    	configuration.init();
    	loggerContext = LoggerContext.getCurrentLoggerContext();
    	isInited = true;
    	isLogOut = Boolean.valueOf(loggerContext.getString(LoggerConstants.LOGGER_IS_LOG_OUT));
    
    }
  
    

    

}

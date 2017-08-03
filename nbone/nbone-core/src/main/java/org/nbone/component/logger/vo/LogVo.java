package org.nbone.component.logger.vo;

import java.util.Date;

import org.nbone.component.logger.OperateType;
import org.nbone.util.lang.ToStringUtils;

/**
 * 此VO不用于存储到数据库而是用于业务中的处理,<br>
 * 如果要保存到数据库请转化为PO
 * @author Thinking  2014-8-8
 *
 */
public class LogVo {
	
	
	/**
	 * 唯一标识
	 */
	private String id;
	
	/**
	 * 操作人ID
	 */
	private String operateUserId;
	/**
	 * 操作人登陆的用户名称
	 */
	private String operateUserName;
	/**
	 * 操作人真实名称（例如名字）
	 */
	private String operateUserRealName;
	
	/**
	 * 操作人IP
	 */
	private String operateUserIp;
	/**
	 * 操作时间
	 */
	private Date operateTime;
	
	/**
	 * 应用模块名称
	 */
	private String moduleName;
	
	/**
	 * 操作类型<br>
	 *  默认:0:QUERY,1:CREATE,2:DELETE,3:UPDATE
	 */
	private OperateType  operateType;
	
	/**
	 * 操作结果 【1:成功,0:失败】 默认是成功:1
	 */
	private int operateResult = 1;
	
	/**
	 * 日志信息描述
	 */
	private String description;
	
	public LogVo() {
	
	}
	
	public LogVo(Date operateTime, int operateResult) {
		this.operateTime = operateTime;
		this.operateResult = operateResult;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(String operateUserId) {
		this.operateUserId = operateUserId;
	}

	public String getOperateUserName() {
		return operateUserName;
	}

	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}

	public String getOperateUserRealName() {
		return operateUserRealName;
	}

	public void setOperateUserRealName(String operateUserRealName) {
		this.operateUserRealName = operateUserRealName;
	}

	public String getOperateUserIp() {
		return operateUserIp;
	}

	public void setOperateUserIp(String operateUserIp) {
		this.operateUserIp = operateUserIp;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public OperateType  getOperateType() {
		return operateType;
	}

	public void setOperateType(OperateType  operateType) {
		this.operateType = operateType;
	}

	public int getOperateResult() {
		return operateResult;
	}

	

	public void setOperateResult(int operateResult) {
		this.operateResult = operateResult;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		
		this.description = description;
	}

	@Override
	public String toString() {
		return ToStringUtils.toStringMultiLine(this);
	}
	
	

}

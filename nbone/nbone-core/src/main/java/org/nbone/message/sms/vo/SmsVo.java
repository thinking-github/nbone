package org.nbone.message.sms.vo;

import java.util.List;


/**
 * 发送短信参数实体
 * @author Thinking  2015-08-01
 *           
 *           <p>
 *           实体使用说明：<br>
 *               SmsContent：（必填）短信内容  <br>
 *				 UserNumberList：（必填）接收号码列表 <br>  
 *				 ValidTime：（可选）存活有效期，为空表示一直有效。时间格式为 yyyy-MM-dd hh:mm:ss <br> 
 *				 AtTime：（可选）定时发送时间，为空表示立即发送。时间格式为 yyyy-MM-dd hh:mm:ss <br>
 *				 ReportFlag：（可选）是否要求返回状态确认报告：0：不需要；1：需要。 默认0  <br>
 *				 Priority：（可选）优先级，0-9从低到高，默认0  <br>
 *           
 *
 */
public class SmsVo {
	
	private  String smsId;
	private  String hRet;
	
	
	/**
	 * 手机号列表（必填）
	 */
	private  List<String> userNumberList;
	
	/**
	 * 短信内容(必填)
	 */
	private  String smsContent;
	
	/**
	 * （可选）存活有效期，为空表示一直有效。时间格式为 yyyy-MM-dd hh:mm:ss  
	 */
	private  String validTime;
	/**
	 * （可选）定时发送时间，为空表示立即发送。时间格式为 yyyy-MM-dd hh:mm:ss
	 */
	private  String atTime;
	/**
	 * （可选）是否要求返回状态确认报告：0：不需要；1：需要。 默认0 
	 */
	private  String reportFlag;
	/**
	 * （可选）优先级，0-9从低到高，默认0 
	 */
	private  String priority;
	
	
	public SmsVo(List<String> userNumberList, String smsContent) {
		this.userNumberList = userNumberList;
		this.smsContent = smsContent;
	}
	
	public SmsVo() {
	}
	
	


	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	public String gethRet() {
		return hRet;
	}

	public void sethRet(String hRet) {
		this.hRet = hRet;
	}

	public List<String> getUserNumberList() {
		return userNumberList;
	}
	public void setUserNumberList(List<String> userNumberList) {
		this.userNumberList = userNumberList;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	public String getAtTime() {
		return atTime;
	}
	public void setAtTime(String atTime) {
		this.atTime = atTime;
	}
	public String getReportFlag() {
		return reportFlag;
	}
	public void setReportFlag(String reportFlag) {
		this.reportFlag = reportFlag;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	
	

}

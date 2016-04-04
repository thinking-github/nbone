package org.nbone.mvc.dto;

/**
 * 流程信息
 * @author WENFAN
 * @author thinking
 *
 */
public class WorkflowInfo implements java.io.Serializable{

	/**
	 * serial
	 */
	private static final long serialVersionUID = 8475706197327776198L;

	private String wfId;//流程ID
	
	private String wfName;//流程名称

	
	/*************************get set 方法*********************************/
	public String getWfId() {
		return wfId;
	}

	public void setWfId(String wfId) {
		this.wfId = wfId;
	}

	public String getWfName() {
		return wfName;
	}

	public void setWfName(String wfName) {
		this.wfName = wfName;
	}
	/*************************get set 方法*********************************/
}

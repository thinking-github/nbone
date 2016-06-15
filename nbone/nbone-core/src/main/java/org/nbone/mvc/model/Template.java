package org.nbone.mvc.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Template Model
 * @author thinking
 * @version 1.0 
 */
public class Template implements Serializable {
	
	private static final long serialVersionUID = -4539312398631152933L;
	

	private String id ;

	private String templateName;
	/**
	 * 描述信息
	 */
	private String description;
	
	
	
	private Map<String,Object> extMap;
	
	private String extJson ;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Object> getExtMap() {
		return extMap;
	}

	public void setExtMap(Map<String, Object> extMap) {
		this.extMap = extMap;
	}

	public String getExtJson() {
		return extJson;
	}

	public void setExtJson(String extJson) {
		this.extJson = extJson;
	}
	

}

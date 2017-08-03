package org.nbone.util.json;


import net.sf.json.xml.XMLSerializer;
/**
 * 配置对象转化成XML时 数组标签的名字和对象标签的名字
 * @author Thinking
 * @version 1.0
 * @Date 2014-04-11
 *
 */
public class JSONXConfig extends XMLSerializer {
	
	
	public JSONXConfig() {
		super();
	}
	
	/**
	 * 构造方法
	 * @param arrayName 数组标签的名称
	 * @param elementName 数组中元素的标签名称
	 * @param objectName  对象的标签名称
	 */
	public JSONXConfig(String arrayName, String elementName, String objectName) {
		this.setArrayName(arrayName) ;
		this.setElementName(elementName) ;
		this.setObjectName(objectName);
	}

	

}

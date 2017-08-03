package org.nbone.mx.datacontrols.datatree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 实现extjs 树节点的封装
 * @author WENFAN
 * @author thinking
 * @version 1.0
 * @since   2016年4月4日
 *
 */
public class JsonTreeNode implements Serializable {
	/**
	 * serialVersionUID
	 */
	protected static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	protected String id;
	/**
	 * 节点显示
	 */
	protected String text;
	/**
	 * 图标
	 */
	protected String icon; 
	/**
	 * 图标样式
	 */
	protected String iconCls;
	/**
	 * 是否叶子
	 */
	protected boolean leaf;
	/**
	 * 链接
	 */
	protected String href;
	/**
	 * 链接指向
	 */
	protected String hrefTarget;
	/**
	 * 使节点带有复选框
	 */
	protected Object checked ;
	/**
	 * 设置节点能否被点选
	 */
	protected boolean disabled;
	/**
	 *是否弹出
	 */
	protected String isPop;
	/**
	 *是否记录日志
	 */
	protected String isLog;
	/**
	 * 业务编码
	 */
	protected String busiCode;
	/**
	 * 系统初始化标识
	 */
	protected String isInit;
	/**
	 * 节点类型
	 */
	protected String type = "";
	/**
	 * URL
	 */
	protected String url;
	/**
	 * 系统id
	 */
	protected String systemId;
	/**
	 * 保留字段1
	 */
	protected String content1;
	/**
	 * 保留字段2
	 */
	protected String content2;
	/**
	 * 保留字段3
	 */
	protected String content3;
	/**
	 * 是否被打印
	 */
	protected int print;
	/**
	 * 父节点ID
	 */
	protected String parent;
	/**
	 * 无参构造函数
	 */
	public JsonTreeNode() {
	    super();
	}

	/**
	 * 树节点构造方法
	 * @param id 树节点ID
	 * @param text 树节点名称
	 * @param parent 父节点ID
	 */
	public JsonTreeNode(String id,String text,String parent)
	    {
	     this.id = id;
	     this.text = text;
	     this.parent = parent;
	     this.print = 0;
	    }
		
	/**
	 * 将单个的map对象转换成树节点的json字符串
	 * @param m Map对象
	 * @param id 节点id
	 * @param text 节点文本
	 * @param leaf 叶子属性
	 * @return json字符串
	 */
	public static String getJsonNodeString(Map m, String id, String text,boolean leaf) {
		JsonTreeNode treeNode = new JsonTreeNode();
		treeNode.setId(m.get(id).toString());
		treeNode.setText(m.get(text).toString());
		treeNode.setLeaf(leaf);
		JSONObject jsonObject = JSONObject.fromObject(treeNode);
		return jsonObject.toString();
	}
	
	/**
	 * 将单个的map对象转换成树节点的json字符串
	 * @param m Map对象
	 * @param ids 节点id数组
	 * @param texts 节点文本数组
	 * @param leaf 叶子属性
	 * @return json字符串
	 */
	public static String getJsonNodeArray(Map m, String[] ids, String[] texts,boolean leaf) {
		JsonTreeNode treeNode = new JsonTreeNode();
		if(ids != null && ids.length >0){
			StringBuffer idStr = new StringBuffer();
			for(int i=0;i<ids.length;i++){
				if(i<ids.length-1){
					idStr.append(m.get(ids[i]).toString()).append(",");
				}else{
					idStr.append(m.get(ids[i]).toString());
				}
			} 
			treeNode.setId(idStr.toString());
		}
		if(texts != null && texts.length >0){
			StringBuffer textStr = new StringBuffer();
			for(int j=0;j<texts.length;j++){
				if(j<texts.length-1){
					textStr.append("(").append(m.get(texts[j]).toString()).append(")");
				}else{
					textStr.append(m.get(texts[j]).toString());
				}
			} 
			treeNode.setText(textStr.toString());
		}
		treeNode.setLeaf(leaf);
		JSONObject jsonObject = JSONObject.fromObject(treeNode); 
		return jsonObject.toString();
	}	
	
	/**
	 * 将单个的map对象转换成树节点的json字符串
	 * @param m Map对象
	 * @param id 节点id
	 * @param text 节点显示文本
	 * @param leaf 节点叶子属性
	 * @param icon  节点样式
	 * @return json字符串
	 */
	public static String getJsonNodeString(Map m, String id, String text,boolean leaf, String icon) {
		JsonTreeNode treeNode = new JsonTreeNode();
		treeNode.setId(m.get(id).toString());
		treeNode.setText(m.get(text).toString());
		treeNode.setLeaf(leaf);
		treeNode.setIcon(icon);
		JSONObject jsonObject = JSONObject.fromObject(treeNode);
		return jsonObject.toString();

	}
	
	/**
	 * 将list转换成对应树的json字符串
	 * @param list List&gt;Map&lt;集合
	 * @param id 节点id
	 * @param text 节点显示文本
	 * @param leaf 全局叶子属性
	 * @return json字符串
	 */
	public static String getJsonNodeString(List<Map> list, String id,String text, boolean leaf) {

		List<JsonTreeNode> nodeList = new ArrayList<JsonTreeNode>();
		for (int i = 0; i < list.size(); i++) {

			JsonTreeNode treeNode = new JsonTreeNode();
			treeNode.setId(list.get(i).get(id).toString());
			treeNode.setText(list.get(i).get(text).toString());
			treeNode.setLeaf(leaf);
			nodeList.add(treeNode);
		}
		JSONArray jsonArray = JSONArray.fromObject(nodeList); 

		return jsonArray.toString();
	}
	
	/**
	 * 将list转换成对应树的json字符串
	 * @param list List&gt;Map&lt;集合
	 * @param id 节点id
	 * @param text 节点显示文本
	 * @param leaf 全局叶子属性
	 * @param icon String
	 * @return json字符串
	 */
	public static String getJsonNodeString(List<Map> list, String id,String text, boolean leaf, String icon) {

		List<JsonTreeNode> nodeList = new ArrayList<JsonTreeNode>();
		for (int i = 0; i < list.size(); i++) {
			JsonTreeNode treeNode = new JsonTreeNode();
			treeNode.setId(list.get(i).get(id).toString());
			treeNode.setText(list.get(i).get(text).toString());
			treeNode.setLeaf(leaf);
			treeNode.setIcon(icon);
			nodeList.add(treeNode);
		}
		JSONArray jsonArray = JSONArray.fromObject(nodeList); 
		return jsonArray.toString();
	}
	
	/**
	 * 将list转换成对应树的json字符串
	 * @param list List&gt;Map&lt;集合
	 * @param id 节点id
	 * @param text 节点显示文本
	 * @return json字符串
	 */
	public static String getJsonNodeString(List<Map> list, String id,String text) {
		List<JsonTreeNode> nodeList = new ArrayList<JsonTreeNode>();
		for (int i = 0; i < list.size(); i++) {
			JsonTreeNode treeNode = new JsonTreeNode();
			treeNode.setId(list.get(i).get(id).toString());
			treeNode.setText(list.get(i).get(text).toString());
			nodeList.add(treeNode);
		}
		JSONArray jsonArray = JSONArray.fromObject(nodeList);

		return jsonArray.toString();
	}
	
	/**
	 * 将list转换成对应树的json字符串
	 * @param checked boolean复选框
	 * @param list List&gt;Map&lt;集合
	 * @param id 节点id
	 * @param text 节点显示文本
	 * @return json字符串
	 */
	public static String getJsonNodeString(boolean checked, List<Map> list,String id, String text) {
		List<JsonTreeNode> nodeList = new ArrayList<JsonTreeNode>();
		for (int i = 0; i < list.size(); i++) {
			JsonTreeNode treeNode = new JsonTreeNode();
			treeNode.setId(list.get(i).get(id).toString());
			treeNode.setText(list.get(i).get(text).toString());
			treeNode.setChecked(checked);
			nodeList.add(treeNode);
		}
		JSONArray jsonArray = JSONArray.fromObject(nodeList); 
		return jsonArray.toString();
	}


	/**
	 * 返回ID
	 * @return ID 
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置ID
	 * @param id ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回文本
	 * @return 文本
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * 设置文本
	 * @param text 文本
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * 返回图标
	 * @return 图标
	 */
	public String getIcon() {
		return icon;
	}
	
	/**
	 * 设置图标
	 * @param icon 图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * 返回图标的样式
	 * @return 图标的样式
	 */
	public String getIconCls() {
		return iconCls;
	}
	
	/**
	 * 设置图标的样式
	 * @param iconCls 图标的样式
	 */
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
	/**
	 * 是否叶子
	 * @return boolean
	 */
	public boolean isLeaf() {
		return leaf;
	}
	
	/**
	 * 设置是否叶子
	 * @param leaf boolean
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	/**
	 * 返回节点的链接
	 * @return 节点的链接
	 */
	public String getHref() {
		return href;
	}
	
	/**
	 * 设置节点的链接
	 * @param href 节点的链接
	 */
	public void setHref(String href) {
		this.href = href;
	}
	
	/**
	 * 返回节点链接的目标
	 * @return 节点链接的目标
	 */
	public String getHrefTarget() {
		return hrefTarget;
	}
	
	/**
	 * 设置节点链接的目标
	 * @param hrefTarget 节点链接的目标
	 */
	public void setHrefTarget(String hrefTarget) {
		this.hrefTarget = hrefTarget;
	}
	
	/**
	 * 返回选中对象
	 * @return 选中对象
	 */
	public Object getChecked() {
		return checked;
	}
	
	/**
	 * 设置选中对象
	 * @param checked 选中对象
	 */
	public void setChecked(Object checked) {
		this.checked = checked;
	}
	
	/**
	 * 返回类型
	 * @return 类型
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * 设置类型
	 * @param type 类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * 返回业务编码
	 * @return 业务编码
	 */
	public String getBusiCode() {
		return busiCode;
	}
	
	/**
	 * 设置业务编码
	 * @param busiCode 业务编码
	 */
	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}
    
	/**
     * 获取能否选取
     * @return boolean 能否选取
     */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * 设置能否选取
	 * @param disabled 能否选取
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	/**
	 * 获取是否弹出
	 * @return String 是否弹出
	 */
	public String getIsPop() {
		return isPop;
	}
	
	/**
	 * 设置是否弹出
	 * @param isPop 是否弹出
	 */
	public void setIsPop(String isPop) {
		this.isPop = isPop;
	}
	
	/**
	 * 获取系统初始化标识
	 * @return 系统初始化标识
	 */
	public String getIsInit() {
		return isInit;
	}
	
	/**
	 * 设置系统初始化标识
	 * @param isInit 系统初始化标识
	 */
	public void setIsInit(String isInit) {
		this.isInit = isInit;
	}
	
	/**
	 * 获取是否记录日志
	 * @return 是否记录日志
	 */
	public String getIsLog() {
		return isLog;
	}
	
	/**
	 * 设置是否记录日志
	 * @param isLog 是否记录日志
	 */
	public void setIsLog(String isLog) {
		this.isLog = isLog;
	}
	
	/**
	 * 获取保留字段1
	 * @return 保留字段1
	 */
	public String getContent1() {
		return content1;
	}
	
	/**
	 * 设置保留字段1
	 * @param content1 保留字段1
	 */
	public void setContent1(String content1) {
		this.content1 = content1;
	}
	
	/**
	 * 获取保留字段2
	 * @return 保留字段2
	 */
	public String getContent2() {
		return content2;
	}
	
	/**
	 * 设置保留字段2
	 * @param content2 保留字段2
	 */
	public void setContent2(String content2) {
		this.content2 = content2;
	}
	
	/**
	 * 获取保留字段3
	 * @return 保留字段3
	 */
	public String getContent3() {
		return content3;
	}
	
	/**
	 * 设置保留字段3
	 * @param content3 保留字段3
	 */
	public void setContent3(String content3) {
		this.content3 = content3;
	}
	
	/**
	 * 获取url
	 * @return url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * 设置url
	 * @param url url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * 获取系统id
	 * @return 系统id
	 */
	public String getSystemId() {
		return systemId;
	}
	
	/**
	 * 设置系统id
	 * @param systemId 系统id
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	/**
	 * 返回是否打印
	 * @return int 打印
	 */
	public int getPrint() {
	    return print;
	}
	/**
	 * 设置是否打印
	 * @param print 打印
	 */
	public void setPrint(int print) {
	    this.print = print;
	}
	/**
	 * 返回父节点id
	 * @return String id
	 */
	public String getParent() {
	    return parent;
	}
	/**
	 * 设置父节点id
	 * @param parent id
	 */
	public void setParent(String parent) {
	    this.parent = parent;
	}	
}
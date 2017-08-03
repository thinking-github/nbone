package org.nbone.mx.datacontrols.datatree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author thinking 
 * @since 2012-9-1 
 * @version 1.0
 * 
 */
public class ZtreeNode  implements Serializable{

	private static final long serialVersionUID = 7598358324659309185L;
	
	private String id;
	private String pId;
	private String name;
	private String title;
	private boolean isParent;
	private String url;
	private String target;
	private boolean checked;
	private boolean open;
	
	private String  icon;
	/**
	 * ztree 默认含有level属性会自动赋值，故不要设置此属性，新增属性如下
	 */
	private int nodeLevel;
	
	private List<ZtreeNode> children;
	/**
	 * 设置节点是否隐藏checkbox/radio 当settiong.check.enable = true有效
	 * true/false 不显示/显示
	 */
	private boolean nocheck;
	
	public boolean isNocheck() {
		return nocheck;
	}
	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the pId
	 */
	public String getpId() {
		return pId;
	}
	/**
	 * @param pId the pId to set
	 */
	public void setpId(String pId) {
		this.pId = pId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the isParent
	 */
	public boolean getIsParent() {
		return isParent;
	}
	/**
	 * @param isParent the isParent to set
	 */
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	/**
	 * @return the open
	 */
	public boolean isOpen() {
		return open;
	}
	/**
	 * @param open the open to set
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getNodeLevel() {
		return nodeLevel;
	}
	public void setNodeLevel(int nodeLevel) {
		this.nodeLevel = nodeLevel;
	}
	/**
	 * @return the children
	 */
	public List<ZtreeNode> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<ZtreeNode> children) {
		this.children = children;
	}
	
	public void addChildren(ZtreeNode node){
		if(children == null){
			children = new ArrayList<ZtreeNode>();
		}
		children.add(node);
	}
}

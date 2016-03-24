package org.nbone.web.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 树节点实体 Bean
 * @author Thinking
 * @version 1.0  2014-05-06
 */
public class TreeNode implements Serializable {
	
	private static final long serialVersionUID = -6579637041206786704L;
	private String id;
	private String text;
	private String imageUrl;
	private boolean hasChildren;
	private String itemType;
	private String displayMode;
	private boolean expanded;
	private List<TreeNode> childNodes;
	private Map customProps;
	
	//兼容easyUI的参数
	/**
	 * iconCls:设置显示图标
	 * checked:是否选中 true为选中
	 * children:子节点列表
	 * state:值为closed:为文件夹样式; open:打开,如果含有子节点则是打开的文件夹;否则是文件样式
	 */
	private String  iconCls;
	private boolean  checked = false;
	private List<TreeNode> children;
	/**
	 * 节点状态， 'open' 或 'closed'，默认是 'open'。当设为 'closed' 时，此节点有子节点，并且将从远程站点加载它们
	 */
	private String state="closed";	 
	
	
	private final static String STATE_OPEN="open";
	private final static String STATE_CLOSED="closed";

	public TreeNode() {
		hasChildren = true;
	}

	public TreeNode(String id, String text) {
		this.id = id;
		this.text = text;
		hasChildren = true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
		if(hasChildren){
			this.state = STATE_CLOSED;
		}else{
			this.state = STATE_OPEN;
		}
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public List<TreeNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<TreeNode> childNodes) {
		this.childNodes = childNodes;
	}

	public Map getCustomProps() {
		return customProps;
	}

	public void setCustomProps(Map customProps) {
		this.customProps = customProps;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


}

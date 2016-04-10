package org.nbone.mx.datacontrols.datatree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author thinking 2012-9-1
 */
public class TreeNode<T> implements Serializable{

	private String id;
	private String text;
	private String state;
	private String iconCls;
	private String name;
	private String click;
	private boolean checked;
	private boolean isParent = true; 
	private T attributes;
	private List<TreeNode<T>> children;
	
	
	public String getClick()
	{
		return click;
	}

	public void setClick(String click)
	{
		this.click = click;
	}

	public boolean getIsParent()
	{
		return isParent;
	}

	public void setIsParent(boolean isParent)
	{
		this.isParent = isParent;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void addChildren(TreeNode<T> child) {
		if (children == null) {
			children = new ArrayList<TreeNode<T>>();
		}
		children.add(child);
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}

	public T getAttributes() {
		return attributes;
	}

	public void setAttributes(T attributes) {
		this.attributes = attributes;
	}

}

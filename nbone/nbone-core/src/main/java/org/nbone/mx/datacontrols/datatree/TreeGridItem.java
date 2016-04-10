package org.nbone.mx.datacontrols.datatree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author thinking 2012-9-1
 */
public class TreeGridItem implements Serializable
{
	private Integer id;
	private String myselfId;
	private String name;
	private String type;
	private Integer parentId;
	private String state;
	private String isApp;
	private String editOrgRoleId;
	private List<TreeGridItem> children;
	private String isToSelect;
	private String selectIds;
	private String selectSelfIds;
	private String iconCls;
	private String appId;
	private String userId;
	private String url;
	private String typeName;
	private boolean checked;
	private String bizRoleId;
	/**
	* myUserId : 登陆者信息
	*/
	private String myUserId;
	
	/**
	 * get myUserId
	 * @return myUserId
	 */
	public String getMyUserId()
	{
		return myUserId;
	}
	/** 
	 * set myUserId
	 * @param myUserId the myUserId to set
	 */
	public void setMyUserId(String myUserId)
	{
		this.myUserId = myUserId;
	}
	/**
	* TODO
	*/
	public TreeGridItem()
	{
		children = new ArrayList<TreeGridItem>();
	}
	/**
	 * get typeName
	 * @return typeName
	 */
	public String getTypeName()
	{
		return typeName;
	}
	/** 
	 * set typeName
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}
	public boolean getChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	/**
	 * get state
	 * @return state
	 */
	public String getState()
	{
		return state;
	}
	/** 
	 * set state
	 * @param state the state to set
	 */
	public void setState(String state)
	{
		this.state = state;
	}
	/**
	 * get type
	 * @return type
	 */
	public String getType()
	{
		return type;
	}
	/** 
	 * set type
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	/**
	 * get id
	 * @return id
	 */
	public Integer getId()
	{
		return id;
	}
	/** 
	 * set id
	 * @param id the id to set
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}
	/**
	 * get name
	 * @return name
	 */
	public String getName()
	{
		return name;
	}
	/** 
	 * set name
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * get parentId
	 * @return parentId
	 */
	public Integer getParentId()
	{
		return parentId;
	}
	/** 
	 * set parentId
	 * @param parentId the parentId to set
	 */
	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}
	public String getMyselfId() {
		return myselfId;
	}
	public void setMyselfId(String myselfId) {
		this.myselfId = myselfId;
	}
	public String getIsApp() {
		return isApp;
	}
	public void setIsApp(String isApp) {
		this.isApp = isApp;
	}
	public String getEditOrgRoleId() {
		return editOrgRoleId;
	}
	public void setEditOrgRoleId(String editOrgRoleId) {
		this.editOrgRoleId = editOrgRoleId;
	}
	public List<TreeGridItem> getChildren() {
		return children;
	}
	public void setChildren(List<TreeGridItem> children) {
		this.children = children;
	}
	public String getIsToSelect() {
		return isToSelect;
	}
	public void setIsToSelect(String isToSelect) {
		this.isToSelect = isToSelect;
	}
	public String getSelectIds() {
		return selectIds;
	}
	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}
	public String getSelectSelfIds() {
		return selectSelfIds;
	}
	public void setSelectSelfIds(String selectSelfIds) {
		this.selectSelfIds = selectSelfIds;
	}
	public String getBizRoleId() {
		return bizRoleId;
	}
	public void setBizRoleId(String bizRoleId) {
		this.bizRoleId = bizRoleId;
	}
	
}

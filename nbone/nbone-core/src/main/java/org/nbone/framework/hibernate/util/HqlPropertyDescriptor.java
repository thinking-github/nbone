package org.nbone.framework.hibernate.util;

import java.util.HashSet;
import java.util.Set;

import org.nbone.persistence.criterion.QueryOperator;

/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class HqlPropertyDescriptor  implements QueryOperator{
    
	
	
	public static  Set<String>  OperTypeSet =  new HashSet<String>();
	private String fieldName;
	/**
	 * 如果是where子句 请设置为true 否则为false
	 */
	private boolean hasWhere = true;
	/**
	 * String Type Default is  like/number Type Default  is =
	 */
	private String  operType;
	
	private boolean  between = false;
	
	
	private Object  beginValue;
	private Object  endValue;
	
	private String  beginValueMark = gt_eq;
	private String  endValueMark = lt_eq;
	
	/**
	 *特殊值
	 */
	private Object  specialValue;
	

	public HqlPropertyDescriptor(String fieldName) {
		this.fieldName = fieldName;
	}

	public HqlPropertyDescriptor(String fieldName, String operType) {
		this.fieldName = fieldName;
		this.operType = operType;
	}
	
   public HqlPropertyDescriptor(String fieldName,Object beginValue,Object endValue) {
	    this.fieldName = fieldName;
		this.beginValue = beginValue;
		this.endValue = endValue;
		this.between = true;
	}


public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isHasWhere() {
		return hasWhere;
	}

	public void setHasWhere(boolean hasWhere) {
		this.hasWhere = hasWhere;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public boolean isBetween() {
		return between;
	}

	public void setBetween(boolean between) {
		this.between = between;
	}

	public Object getBeginValue() {
		return beginValue;
	}

	public void setBeginValue(Object beginValue) {
		this.beginValue = beginValue;
	}

	public Object getEndValue() {
		return endValue;
	}

	public void setEndValue(Object endValue) {
		this.endValue = endValue;
	}

	public String getBeginValueMark() {
		return beginValueMark;
	}

	public void setBeginValueMark(String beginValueMark) {
		this.beginValueMark = beginValueMark;
	}

	public String getEndValueMark() {
		return endValueMark;
	}

	public void setEndValueMark(String endValueMark) {
		this.endValueMark = endValueMark;
	}

	public Object getSpecialValue() {
		return specialValue;
	}


	public void setSpecialValue(Object specialValue) {
		this.specialValue = specialValue;
	}


	//------------------------------------------------------------------
	@Override
	public String transfer(String s) {
		return null;
	}
	
	static{
		OperTypeSet.add(eq);
		OperTypeSet.add(not_eq);
		OperTypeSet.add(lt);
		OperTypeSet.add(gt);
		OperTypeSet.add(lt_eq);
		OperTypeSet.add(gt_eq);
	}

}

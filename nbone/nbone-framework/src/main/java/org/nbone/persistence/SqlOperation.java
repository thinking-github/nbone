package org.nbone.persistence;

import java.util.HashSet;
import java.util.Set;

import org.nbone.persistence.criterion.QueryOperator;

/**
 * 
 * @author thinking
 * @version 1.0 
 */
public class SqlOperation implements QueryOperator {
	
	
	public static  Set<String>  OperTypeSet =  new HashSet<String>();
	private String fieldName;
	/**
	 * 实体属性类型
	 */
	private Class<?> propertyType;
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
	 *特殊值 in  object[] / List / String List 1,2,3,4,5
	 */
	private Object  specialValue;
	

	public SqlOperation(String fieldName) {
		this.fieldName = fieldName;
	}

	public SqlOperation(String fieldName, String operType) {
		this.fieldName = fieldName;
		this.operType = operType;
	}
	
   public SqlOperation(String fieldName, Object beginValue, Object endValue) {
	    this.fieldName = fieldName;
		this.beginValue = beginValue;
		this.endValue = endValue;
		this.between = true;
	}
   
   public SqlOperation(String fieldName, Object[] values) {
	    this.fieldName = fieldName;
	    this.specialValue = values;
	    this.operType = in;
   }

	public SqlOperation(String fieldName, String operType, Object specialValue) {
		this.fieldName = fieldName;
		this.operType = operType;
		this.specialValue = specialValue;
	}



	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Class<?> getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
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

	/**
	 * 是否属于 in 查询
	 * @return
	 */
	public boolean isIn() {

		return SqlOperation.in.equalsIgnoreCase(operType) || SqlOperation.not_in.equalsIgnoreCase(operType);
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

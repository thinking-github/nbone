package org.nbone.persistence;

import org.nbone.persistence.criterion.QueryOperator;

/**eg: beginTime <= value <= endTime
 * 
 * @author thinking
 * @version 1.0 
 */
public class SqlPropertyRange implements QueryOperator{
	
	private String leftField;
	
	private String rightField;
	
	private String dbLeftField;
	
	private String dbRightField;
	
	private Object value;
	
	private String  leftValueMark = lt_eq;
	private String  rightValueMark = gt_eq;

	
	public SqlPropertyRange() {
	}

	public SqlPropertyRange(String leftField, String rightField, Object value) {
		this.leftField = leftField;
		this.rightField = rightField;
		this.value = value;
	}

	
	
	public String getLeftField() {
		return leftField;
	}

	public void setLeftField(String leftField) {
		this.leftField = leftField;
	}

	public String getRightField() {
		return rightField;
	}

	public void setRightField(String rightField) {
		this.rightField = rightField;
	}

	public String getDbLeftField() {
		return dbLeftField;
	}

	public void setDbLeftField(String dbLeftField) {
		this.dbLeftField = dbLeftField;
	}

	public String getDbRightField() {
		return dbRightField;
	}

	public void setDbRightField(String dbRightField) {
		this.dbRightField = dbRightField;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getLeftValueMark() {
		return leftValueMark;
	}

	public void setLeftValueMark(String leftValueMark) {
		this.leftValueMark = leftValueMark;
	}

	public String getRightValueMark() {
		return rightValueMark;
	}

	public void setRightValueMark(String rightValueMark) {
		this.rightValueMark = rightValueMark;
	}

	@Override
	public String transfer(String s) {
		return null;
	}
	

}

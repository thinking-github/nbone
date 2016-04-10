package org.nbone.util.file.excel.jxl;

import java.io.Serializable;

/**
 * 用于Excel导出的辅助类，映射数据结果集(ResultSet)内列名的元数据和Excel内的显示列名
 * <p/>
 * 
 * @author Thinking  2014-10-29
 * @version Date: 2013-2-1 下午7:59:54
 * @serial 1.0
 */
public class ExcelColumn implements Serializable {
	/**
	 * 数据库的字段名称
	 */
	private String columnName;
	/**
	 * 实际显示的名称
	 */
	private String columnTitle;
	/**
	 * 单元格的宽度
	 */
	private int width = 0;

	public ExcelColumn() {
	}

	public ExcelColumn(String columnName, String columnTitle) {
		this.columnName = columnName;
		this.columnTitle = columnTitle;
	}

	public ExcelColumn(String columnName, String columnTitle, int width) {
		this.columnName = columnName;
		this.columnTitle = columnTitle;
		this.width = width;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnTitle() {
		return columnTitle;
	}

	public void setColumnTitle(String columnTitle) {
		this.columnTitle = columnTitle;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}

package org.nbone.mx.datacontrols.datatree;

import java.io.Serializable;
import java.util.List;

/**
  * @author thinking 2012-9-1
  */
public class TreeGrid implements Serializable
{
	private Integer total;
	/**
	 * get total
	 * @return total
	 */
	public Integer getTotal()
	{
		return total;
	}
	/** 
	 * set total
	 * @param total the total to set
	 */
	public void setTotal(Integer total)
	{
		this.total = total;
	}
	/**
	 * get rows
	 * @return rows
	 */
	public List<?> getRows()
	{
		return rows;
	}
	/** 
	 * set rows
	 * @param rows the rows to set
	 */
	public void setRows(List<?> rows)
	{
		this.rows = rows;
	}
	private List<?> rows;

}

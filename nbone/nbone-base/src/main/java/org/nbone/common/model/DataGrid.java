package org.nbone.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>Description: [DataGrid模型  存放查询的数量和list]</p>
 * Created on 2016-3-24
 * @author chenyicheng
 * @version 1.0 
 *
 */
public class DataGrid<T> implements Serializable{
	
	private static final long serialVersionUID = -6534177227713614502L;
	private Long total = 0L;
	private List<T> rows = new ArrayList<T>();

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}

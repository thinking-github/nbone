package org.nbone.mx.datacontrols.datapage;
import java.io.Serializable;
import java.util.List;

/**
 * 页面获取分页对象的变量
 * 
 * @version v1.0 2014-04-09
 * @author Thinking
 * 
 */
public class PagerModel<T> implements Serializable {
	
    /**
     * 当前页码
     */
	private int pageNow;
	/**
	 * 单页的大小  默认为20行   
	 */
	private int pageSize = 20;
	/**
	 * 总页数
	 */
	private int pageCount;
	
	/**
	 * 记录总数
	 */
	private int total;
	/**
	 * 当前页对象列表
	 */
	private List<T> rows;
	
	
	/**
	 * 构造函数
	 */
	public PagerModel() {
	}
	
	public PagerModel(int pageSize) {
		this.pageSize = pageSize;
	}

	public PagerModel(List<T> rows) {
		this.rows = rows;
		this.total = rows == null ? 0 : rows.size();
	}

	public int getPageNow() {
		return pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
 
}

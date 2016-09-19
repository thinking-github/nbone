package org.nbone.mx.datacontrols.datapage;
import java.io.Serializable;
import java.util.List;

/**
 * 分页对象. 包含当前页数据及分页信息如总记录数.
 * 
 * @author thinking
 * @since 2014-04-09
 * @version v1.0 
 * 
 */
public class PagerModel<T> implements Serializable {
	
	private static final long serialVersionUID = 864785535881496596L;

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
	private long pageCount;
	/**
	 * 总页数
	 */
	private long totalPages;
	
	/**
	 * 记录总数
	 */
	private long total;
	/**
	 * 当前页对象列表
	 */
	private List<T> rows;
	
	/**
	 * 获取任一页第一条数据在数据集的位置.
	 * 返回结果开始位置索引  默认0
	 */
	private int firstResult = 0;
	
	/**
	 * 是否有下一页
	 */
	private boolean hasNextPage ;
	/**
	 * 是否有上一页
	 */
	private boolean hasPreviousPage ;
	
	private boolean autoCount = true;
	
	
	
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
	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 */
	public void setPageNow(int pageNow) {
		if (pageNow < 1) {
			pageNow = 1;
		}
		this.pageNow = pageNow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getPageCount() {
		pageCount = getTotalPages();
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为-1.
	 * @return
	 */
	public long getTotalPages() {
		if (total < 0) {
			return -1;
		}
		long count = total / pageSize;
		if (total % pageSize == 0)
			totalPages =  count;
		else
			totalPages = count+ 1;
		
		return totalPages;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从0开始.
     * @return
     */
	public int getFirstResult() {
		firstResult = (pageNow-1) * pageSize;
		
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	/**
	 * 该页是否有下一页.
	 */
	public boolean isHasNextPage() {
		hasNextPage = this.getPageNow() < this.getTotalPages();
		return hasNextPage;
	}
	/**
	 * 该页是否有上一页.
	 */
	public boolean isHasPreviousPage() {
		hasPreviousPage = this.getPageNow() > 1;
		return  hasPreviousPage;
	}
	
	/**
	 * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
	 */
	public int getNextPage() {
		if (isHasNextPage()) {
			return pageNow + 1;
		} else {
			return pageNow;
		}
	}

	/**
	 * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
	 */
	public int getPrePage() {
		if (isHasPreviousPage()) {
			return pageNow - 1;
		} else {
			return pageNow;
		}
	}
	
	/**
	 * 获得查询对象时是否先自动执行count查询获取总记录数, 默认为true.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 设置查询对象时是否自动先执行count查询获取总记录数.
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

 
}

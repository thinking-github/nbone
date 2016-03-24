package org.nbone.mx.datacontrols.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

/**
 * 用于分页支持
 * @author thinking
 */
public class PaginationSupport implements Serializable {
	
	private static final long serialVersionUID = -4113165474296861224L;
	public static final int DEFAULT_PAGE_SIZE = 20;

	// -- 分页参数 --//
	/**
	 * @Fields pageSize : 每页条数
	 */
	private int pageSize;
	/**
	 * @Fields pageNumber : 第几页
	 */
	private int pageNumber;
	/**
	 * @Fields start :
	 */
	private Long start;
	/**
	 * @Fields end :
	 */
	private Long end;
	protected boolean autoCount = true;

	protected String orderBy;
	protected String order;
	protected Object expression;
	private Map<String, Object> params = null;

	// -- 返回结果 --//
	protected List<?> rows = Collections.emptyList();
	protected long total = -1;

	// -- 构造函数 --//
	public PaginationSupport() {
	}

	/**
	 * 
	 * 处理参数传递的问题 
	 * for jquery easyui datagrid
	 * @author chen ze
	 * @param request
	 */
	public PaginationSupport(HttpServletRequest request) {
		String pageNo = WebUtils.findParameterValue(request, "page");
		String pageSize = WebUtils.findParameterValue(request, "rows");
		this.pageNumber=(pageNo == null ? 1 : Integer.parseInt(pageNo));
		this.pageSize=(pageSize == null ? DEFAULT_PAGE_SIZE : Integer.parseInt(pageSize));
	}

	public PaginationSupport(int pageSize, int pageNumber) {
		super();
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}

	// -- 访问查询参数函数 --//

	/**
	 * get pageNumber
	 * 
	 * @return pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * set pageNumber
	 * 
	 * @param pageNumber
	 *            the pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		if (pageNumber < 1) {
			this.pageNumber = 1;
		}
	}

	public PaginationSupport pageNo(final int pageNumber) {
		setPageNumber(pageNumber);
		return this;
	}

	/**
	 * 获得每页的记录数量,默认为1.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量,低于1时自动调整为1.
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1) {
			this.pageSize = 10;
		}
	}

	public PaginationSupport pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	 */
	// public int getFirst() {
	// return ((pageNumber - 1) * pageSize) + 1;
	// }
	/**
	 * 查询对象时是否自动另外执行count查询获取总记录数, 默认为false.
	 */
	public boolean isAutoCount() {
		return autoCount;
	}

	/**
	 * 查询对象时是否自动另外执行count查询获取总记录数.
	 */
	public void setAutoCount(final boolean autoCount) {
		this.autoCount = autoCount;
	}

	public PaginationSupport autoCount(final boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	// -- 访问查询结果函数 --//

	/**
	 * get start
	 * 
	 * @return start
	 */
	public Long getStart() {
		return start;
	}

	/**
	 * get rows
	 * 
	 * @return rows
	 */
	public List<?> getRows() {
		return rows;
	}

	/**
	 * set rows
	 * 
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	/**
	 * set start
	 * 
	 * @param start
	 *            the start to set
	 */
	public void setStart(Long start) {
		this.start = start;
	}

	/**
	 * get end
	 * 
	 * @return end
	 */
	public Long getEnd() {
		return end;
	}

	/**
	 * set end
	 * 
	 * @param end
	 *            the end to set
	 */
	public void setEnd(Long end) {
		this.end = end;
	}

	/**
	 * get total
	 * 
	 * @return total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * set total
	 * 
	 * @param total
	 *            the total to set
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为-1.
	 */
	// public long getTotalPages() {
	// if (total < 0) {
	// return -1;
	// }
	//
	// long count = total / pageSize;
	// if (total % pageSize > 0) {
	// count++;
	// }
	// return count;
	// }
	/**
	 * 是否还有下一页.
	 */
	// public boolean isHasNext() {
	// return (pageNumber + 1 <= getTotalPages());
	// }
	/**
	 * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
	 */
	// public int getNextPage() {
	// if (isHasNext()) {
	// return pageNumber + 1;
	// } else {
	// return pageNumber;
	// }
	// }
	/**
	 * 是否还有上一页.
	 */
	// public boolean isHasPre() {
	// return (pageNumber - 1 >= 1);
	// }
	/**
	 * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
	 */
	// public int getPrePage() {
	// if (isHasPre()) {
	// return pageNumber - 1;
	// } else {
	// return pageNumber;
	// }
	// }
	public Object getExpression() {
		return expression;
	}

	public void setExpression(Object expression) {
		this.expression = expression;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	/**
	 * @Title: calc
	 * @Description:
	 */
	public void calc() {
		if (getPageSize() == 0) {
			setPageSize(10);
		}
		if (getPageNumber() <= 0) {
			setPageNumber(1);
		}
		long start = (getPageNumber() - 1) * getPageSize();
		long end = getPageNumber() * getPageSize();
		setStart(start);
		setEnd(end);
	}
}

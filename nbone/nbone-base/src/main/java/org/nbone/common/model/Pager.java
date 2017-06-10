package org.nbone.common.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenyicheng
 * @version 1.0
 * 
 */
public class Pager<T> implements Serializable {

	private static final long serialVersionUID = 8889600290988138041L;

	/** 默认每页记录数为10条 */
	public static final int DEFAULT_PAGESIZE = 10;

	
	private int pageNow = 1;// 当前页
	private int pageSize = 10;// 每页显示记录数
	private int pageOffset;// 当前页起始记录
	private List<T> records; // 分页数据
	
	private int totalPage;// 总页数
	private int totalCount = 0;// 总记录数
	
	
	private String sort;// 排序字段（如有多个字段 例 "columnA,columnB desc" ）
	private String order;// asc/desc
	
	private int startPageIndex;// 开始页
	private int endPageIndex;// 结束页
	private int pageCode = 5; // 页码数量：翻页条工显示多少页的索引
	private int previewPage = 1;// 上一页
	private int nextPage = 1; // 下一页
	private String pageMethod;// 分页方法
	
	

	public Pager() {
		if (pageNow > 0 && pageSize > 0) {
		}
	}

	/**
	 * @param page
	 *            当前页
	 * @rows 每页记录数大小
	 */
	public Pager(int pageNow, int pageSize) {
		this.pageNow = pageNow;
		this.pageSize = pageSize;
	}

	/**
	 * @param page
	 *            当前页
	 */
	public Pager(int pageNow) {
		this.pageNow = pageNow;
		this.pageSize = DEFAULT_PAGESIZE;
	}

	public int getPageOffset() {
		pageOffset = (pageNow - 1) * pageSize;
		return pageOffset;
	}

	public void setPageOffset(int pageOffset) {
		this.pageOffset = pageOffset;
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

	
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
		// mybatis 排序
		if (sort != null && sort.length() > 0) {
		}
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;

		this.startPageIndex = this.pageNow - (pageCode % 2 == 0 ? pageCode / 2 - 1 : pageCode / 2);
		this.endPageIndex = this.pageNow + pageCode / 2;
		if (this.startPageIndex < 1) {
			this.startPageIndex = 1;
			if (totalPage >= pageCode) {
				this.endPageIndex = pageCode;
			} else {
				this.endPageIndex = totalPage;
			}
		}
		if (this.endPageIndex > totalPage) {
			this.endPageIndex = totalPage;
			if ((this.endPageIndex - pageCode) > 0) {
				this.startPageIndex = this.endPageIndex - pageCode + 1;
			} else
				this.startPageIndex = 1;
		}
		if (this.endPageIndex <= 1) {
			this.endPageIndex = 1;
		}
		this.previewPage = this.pageNow - 1;
		this.nextPage = this.pageNow + 1;
		if (this.pageNow <= 1) {
			this.previewPage = 1;
		}
		if (this.pageNow >= this.totalPage) {
			this.nextPage = this.totalPage;
		}
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		setTotalPage((int) (totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize + 1)));
	}

	public int getStartPageIndex() {
		return startPageIndex;
	}

	public void setStartPageIndex(int startPageIndex) {
		this.startPageIndex = startPageIndex;
	}

	public int getEndPageIndex() {
		return endPageIndex;
	}

	public void setEndPageIndex(int endPageIndex) {
		this.endPageIndex = endPageIndex;
	}

	public int getPageCode() {
		return pageCode;
	}

	public void setPageCode(int pageCode) {
		this.pageCode = pageCode;
	}

	public int getPreviewPage() {
		return previewPage;
	}

	public void setPreviewPage(int previewPage) {
		this.previewPage = previewPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public String getPageMethod() {
		return pageMethod;
	}

	public void setPageMethod(String pageMethod) {
		this.pageMethod = pageMethod;
	}

}

package org.nbone.framework.spring.data.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
/**
 *  添加默认构造方法
 * @author thinking
 * @see 2016-08-10
 * @since  spring data 
 * @param <T>
 */
public class PageImpl<T> implements Page<T>, Serializable {

	private static final long serialVersionUID = 867755909294344406L;

	private  List<T> content = new ArrayList<T>();
	private  Pageable pageable;
	private  long total;
	
	

	public PageImpl() {
		
	}

	/**
	 * Constructor of {@code PageImpl}.
	 * 
	 * @param content the content of this page, must not be {@literal null}.
	 * @param pageable the paging information, can be {@literal null}.
	 * @param total the total amount of items available
	 */
	public PageImpl(List<T> content, Pageable pageable, long total) {

		if (null == content) {
			throw new IllegalArgumentException("Content must not be null!");
		}

		this.content.addAll(content);
		this.total = total;
		this.pageable = pageable;
	}

	/**
	 * Creates a new {@link PageImpl} with the given content. This will result in the created {@link Page} being identical
	 * to the entire {@link List}.
	 * 
	 * @param content must not be {@literal null}.
	 */
	public PageImpl(List<T> content) {
		this(content, null, null == content ? 0 : content.size());
	}

	public PageImpl(List<T> content,long total) {
		this(content, null, total);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getNumber()
	 */
	@Override
	public int getNumber() {
		return pageable == null ? 0 : pageable.getPageNumber();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getSize()
	 */
	@Override
	public int getSize() {
		return pageable == null ? 0 : pageable.getPageSize();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getTotalPages()
	 */
	@Override
	public int getTotalPages() {
		return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getNumberOfElements()
	 */
	@Override
	public int getNumberOfElements() {
		return content.size();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getTotalElements()
	 */
	@Override
	public long getTotalElements() {
		return total;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#hasPreviousPage()
	 */
	public boolean hasPreviousPage() {
		return getNumber() > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#isFirstPage()
	 */
	public boolean isFirstPage() {
		return !hasPreviousPage();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#hasNextPage()
	 */
	public boolean hasNextPage() {
		return getNumber() + 1 < getTotalPages();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#isLastPage()
	 */
	public boolean isLastPage() {
		return !hasNextPage();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#nextPageable()
	 */
	@Override
	public Pageable nextPageable() {
		return hasNextPage() ? pageable.next() : null;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#previousOrFirstPageable()
	 */
	@Override
	public Pageable previousPageable() {

		if (hasPreviousPage()) {
			return pageable.previousOrFirst();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return content.iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getContent()
	 */
	@Override
	public List<T> getContent() {
		return Collections.unmodifiableList(content);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#hasContent()
	 */
	@Override
	public boolean hasContent() {
		return !content.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Page#getSort()
	 */
	@Override
	public Sort getSort() {
		return pageable == null ? null : pageable.getSort();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		String contentType = "UNKNOWN";

		if (content.size() > 0) {
			contentType = content.get(0).getClass().getName();
		}

		return String.format("Page %s of %d containing %s instances", getNumber(), getTotalPages(), contentType);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PageImpl<?>)) {
			return false;
		}

		PageImpl<?> that = (PageImpl<?>) obj;

		boolean totalEqual = this.total == that.total;
		boolean contentEqual = this.content.equals(that.content);
		boolean pageableEqual = this.pageable == null ? that.pageable == null : this.pageable.equals(that.pageable);

		return totalEqual && contentEqual && pageableEqual;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 17;

		result = 31 * result + (int) (total ^ total >>> 32);
		result = 31 * result + (pageable == null ? 0 : pageable.hashCode());
		result = 31 * result + content.hashCode();

		return result;
	}

	public Pageable getPageable() {
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
	
	
	//--------------------1.8------------------
	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Slice#hasPrevious()
	 */
	@Override
	public boolean hasPrevious() {
		return getNumber() > 0;
	}


	@Override
	public boolean isFirst() {
		return !hasPrevious();
	}


	@Override
	public boolean isLast() {
		return !hasNext();
	}


	@Override
	public boolean hasNext() {
		return hasNextPage();
	}

	@Override
	public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
		return null;
	}

	
	
}

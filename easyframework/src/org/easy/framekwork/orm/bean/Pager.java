package org.easy.framekwork.orm.bean;

import java.io.Serializable;
import java.util.List;

public class Pager<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pageNumber;
	private int pageSize;
	private long totalRecord;
	private long totalPage;
	private List<T> recordList;

	public Pager(int pageNumber, int pageSize, long totalRecord, long totalPage, List<T> recordList) {
		super();
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalRecord = totalRecord;
		this.totalPage = totalPage;
		this.recordList = recordList;
		if (pageSize != 0) {
			totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
		}
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public List<T> getRecordList() {
		return recordList;
	}

	public boolean isFirstPage() {
		return pageNumber == 1;
	}

	public boolean isLastPage() {
		return pageNumber == totalPage;
	}

	public boolean isPrevPage() {
		return pageNumber > 1 && pageNumber <= totalPage;
	}

	public boolean isNextPage() {
		return pageNumber < totalPage;
	}

}

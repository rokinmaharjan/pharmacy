package com.upachar.web.common.dto;

import java.util.List;

public class PageDto {
	private List<?> content;
	private long pageElementCount;
	private long totalElements;
	private long totalPages;

	public List<?> getContent() {
		return content;
	}

	public void setContent(List<?> content) {
		this.content = content;
	}

	public long getPageElementCount() {
		return pageElementCount;
	}

	public void setPageElementCount(long pageElementCount) {
		this.pageElementCount = pageElementCount;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

}

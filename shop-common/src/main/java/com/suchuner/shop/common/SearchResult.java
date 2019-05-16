package com.suchuner.shop.common;

import java.io.Serializable;
import java.util.List;

import com.suchuner.shop.common.SearchItemResult;

public class SearchResult implements Serializable{
	private List<SearchItemResult> itemList;
	private Long totalCount;
	private Long totalPages;

	public List<SearchItemResult> getItemList() {
		return itemList;
	}

	public void setItemList(List<SearchItemResult> itemList) {
		this.itemList = itemList;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	/**
	 * 此方法要求先把totalCount设置好
	 * 
	 * @param pageSize
	 *            每页显示的记录数
	 */
	public void setTotalPages(Long pageSize) {
		this.totalPages = getTotalCount() % pageSize == 0 ? getTotalCount() / pageSize
				: (getTotalCount() / pageSize) + 1;
	}
}

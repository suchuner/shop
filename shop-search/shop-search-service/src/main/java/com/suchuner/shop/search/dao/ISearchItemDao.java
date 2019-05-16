package com.suchuner.shop.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.suchuner.shop.common.SearchResult;

public interface ISearchItemDao {
	/**
	 * 此方法用于根据查询条件查询并且返回封装好的查询结果
	 * @param query  封装好的查询条件
	 * @return 封装好的查询结果
	 * @throws Exception
	 */
	public SearchResult search(SolrQuery query) throws Exception;
}

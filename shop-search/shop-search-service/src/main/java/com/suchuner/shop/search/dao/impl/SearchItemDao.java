package com.suchuner.shop.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.suchuner.shop.common.SearchItemResult;
import com.suchuner.shop.common.SearchResult;
import com.suchuner.shop.search.dao.ISearchItemDao;

@Repository
public class SearchItemDao implements ISearchItemDao {
	@Autowired
	private SolrServer solrServer;

	public SearchResult search(SolrQuery query) throws Exception {
		/*
		 * 需求:从索引根据查询条件查询并且返回封装好的结果 步骤: 1.根据查询条件,查询到结果返回对象, 2.根据返回的结果对象获取结果集
		 * 3.循环遍历该结果集,并且将该结果集内的对象一一封装到商品结果对象中 4.将商品结果对象添加道返回对象中,并设置总记录数 5.返回结果
		 */
		QueryResponse response = solrServer.query(query);
		SolrDocumentList solrDocumentList = response.getResults();
		List<SearchItemResult> itemResults = new ArrayList<>();
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItemResult itemResult = new SearchItemResult();
			itemResult.setId((String) solrDocument.get("id"));
			itemResult.setCategoryName((String) solrDocument.get("item_category_name"));
			String images = (String) solrDocument.get("item_image");
			if (images != null && !images.equals("")) {
				String[] arr = images.split(",");
				if (arr.length > 1) {
					images = arr[0];
				}
			}
			itemResult.setImage(images);
			itemResult.setSellPoint((String) solrDocument.get("item_sell_point"));
			itemResult.setPrice((long) solrDocument.get("item_price"));
			itemResult.setItemDesc((String) solrDocument.get("item_desc"));
			// 设置返回值的高亮
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get(solrDocument.get("item_title"));
			if (list != null && list.size() > 0) {
				itemResult.setTitle(list.get(0));
			} else {
				itemResult.setTitle((String) solrDocument.get("item_title"));
			}
			itemResults.add(itemResult);
		}
		SearchResult result = new SearchResult();
		result.setItemList(itemResults);
		result.setTotalCount(solrDocumentList.getNumFound());
		return result;
	}
}

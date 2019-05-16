package com.suchuner.shop.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suchuner.shop.common.SearchItemResult;
import com.suchuner.shop.common.SearchResult;
import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.search.dao.impl.SearchItemDao;
import com.suchuner.shop.search.mapper.SearchItemMapper;
import com.suchuner.shop.search.service.ISearchItemService;

@Service
public class SearchItemService implements ISearchItemService {
		
	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Autowired
	private SearchItemDao searchItemDao;
	
	@Autowired
	private SolrServer solrServer;
	
	public TaotaoResult importAllItems() throws Exception {
		/*需求:将从数据库中查询到的数据封装到索引库中,需要导入solrJ即solr系统的客户端
		 * 注意:最后一定需要commit()进行提交
		 * 发布服务
		 * */
		List<SearchItemResult> list = searchItemMapper.getItemList();
		for (SearchItemResult sr : list) {
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id",sr.getId() );
			document.addField("item_title",sr.getTitle() );
			document.addField("item_sell_point",sr.getSellPoint() );
			document.addField("item_price",sr.getPrice());
			document.addField("item_image",sr.getImage());
			document.addField("item_category_name",sr.getCategoryName());
			document.addField("item_desc",sr.getItemDesc() );
			solrServer.add(document);
		}
		solrServer.commit();
		return TaotaoResult.ok();
	}

	public SearchResult search(String queryString, Long currPage, Long pageSize) throws Exception {
		/*需求:将查询条件封装好,并且将返回的结果数据补全,即总页数
		 * 步骤:
		 * 1.创建查询对象,设置查询条件,本次设置的查询条件为:查询的关键字,分页,默认查询域,高亮,高亮需要添加的东西
		 * 2.调用dao进行查询,得到返回的结果,补全返回的结果,就是讲pageSize传入即可;
		 * 3.返回 返回的对象
		 * 4.发布服务
		 * */
		SolrQuery query = new SolrQuery(queryString);
		query.setStart((int)((currPage-1)*pageSize));
		query.setRows(pageSize.intValue());
		query.set("df", "item_keywords");
		query.setHighlight(true);
		query.setHighlightSimplePre("<font color='red' >");
		query.setHighlightSimplePost("</font>");
		SearchResult searchResult = searchItemDao.search(query);
		searchResult.setTotalPages(pageSize);
		return searchResult;
	}
 public TaotaoResult updateItemByItemId(Long itemId) throws Exception{
	 SearchItemResult itemResult = searchItemMapper.getItemByItemId(itemId);
	 SolrInputDocument document = new SolrInputDocument();
		document.addField("id",itemResult.getId() );
		document.addField("item_title",itemResult.getTitle() );
		document.addField("item_sell_point",itemResult.getSellPoint() );
		document.addField("item_price",itemResult.getPrice());
		document.addField("item_image",itemResult.getImage());
		document.addField("item_category_name",itemResult.getCategoryName());
		document.addField("item_desc",itemResult.getItemDesc() );
		solrServer.add(document);
		solrServer.commit();
	 return TaotaoResult.ok();
 }
}

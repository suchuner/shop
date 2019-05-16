package com.suchuner.shop.search.service;



import com.suchuner.shop.common.SearchResult;
import com.suchuner.shop.common.TaotaoResult;

public interface ISearchItemService {
	/**
	 * 导入所有的商品信息到索引库中
	 * @return 返回结果,是否成功
	 */
	public TaotaoResult importAllItems()throws Exception ;
	
	/**该方法用于根据传入的参数设置查询条件传入到dao中查询,并将页面信息封装进返回对象中
	 * @param queryString 查询关键字
	 * @param currPage 当前页
	 * @param pageSize 每页显示的记录数
	 * @return 封装好的结果
	 * @throws Exception
	 */
	public SearchResult search(String queryString,Long currPage,Long pageSize) throws Exception;
}

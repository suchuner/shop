package com.suchuner.shop.search.mapper;

import java.util.List;

import com.suchuner.shop.common.SearchItemResult;

public interface SearchItemMapper {
	/** 该方法用于得到需要导入到索引库的数据
	 * @return 该对象的集合
	 */
	public  List<SearchItemResult> getItemList();
	
	/**根据商品的id查询需要导入到索引库中的数据
	 * @param itemId 商品id
	 * @return 根绝id的到的商品对象
	 */
	public  SearchItemResult getItemByItemId(Long itemId);
}

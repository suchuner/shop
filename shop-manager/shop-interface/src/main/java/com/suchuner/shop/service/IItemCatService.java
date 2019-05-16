package com.suchuner.shop.service;

import java.util.List;

import com.suchuner.shop.common.EasyUITreeResult;

public interface IItemCatService {
	/**根据商品类目表的父节点id查询子结点,单表查询
	 * @param parentId
	 * @return 所有的子结点的集合
	 */
	public List<EasyUITreeResult> getTree(Long parentId);

}

package com.suchuner.shop.content.service;

import java.util.List;

import com.suchuner.shop.common.EasyUITreeResult;
import com.suchuner.shop.common.TaotaoResult;

public interface IContentCategoryService {
	/**
	 * @return 得到门户网站的内容分类模块的集合
	 */
	public List<EasyUITreeResult> getContentCategoryTree(Long parentId);

	/**
	 * @param parentId 当前被添加结点的父节点的id
	 * @param name 当前被添加节点的名称
	 * @return 返回结果
	 */
	public TaotaoResult addContentCategory(Long parentId, String name);

	/**
	 * @param id 更新当前节点的名称
	 * @param name 要被更新的名称
	 * @return 返回的结果
	 */
	public TaotaoResult updateContentCategory(Long id, String name);

	/**
	 * @param id 需要被删除的结点id,
	 * 如果当前节点是父节点,则不允许删除,
	 * 是叶子节点,则删除,并判断父节点下是否还有叶子节点,若没有,而将父节点改为叶子节点
	 * @return 返回结果
	 */
	public TaotaoResult deleteContentCategory(Long id);
}

package com.suchuner.shop.content.service;

import java.util.List;

import com.suchuner.shop.common.EasyUIDataResult;
import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.domain.TbContent;

public interface IContentService {

	/**
	 * 根据分类的id查询该分类下的内容
	 * @param categoryId分类的id
	 * @param page 当前页码
	 * @param rows 每页需要显示的记录数
	 * @return 封装好的数据
	 */
	public EasyUIDataResult getContent(Long categoryId, Integer page, Integer rows);

	/**
	 * @param tbContent 需要被保存的对象
	 * @return
	 */
	public TaotaoResult addContent(TbContent tbContent);
	
	/**本方法用于前台系统的数据查询
	 * @param categoryId 分离的id
	 * @return  该分类下的所有内容
	 */
	public List<TbContent> getContentByCategoryId(Long categoryId);
}

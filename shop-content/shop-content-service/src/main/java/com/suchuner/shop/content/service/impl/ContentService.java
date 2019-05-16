package com.suchuner.shop.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suchuner.shop.common.EasyUIDataResult;
import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.content.service.IContentService;
import com.suchuner.shop.content.service.jedis.JedisClient;
import com.suchuner.shop.domain.TbContent;
import com.suchuner.shop.domain.TbContentExample;
import com.suchuner.shop.mapper.TbContentMapper;
import com.suchuner.shop.utils.JsonUtils;

@Service
public class ContentService implements IContentService {
	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;

	public EasyUIDataResult getContent(Long categoryId, Integer page, Integer rows) {
		/*
		 * 需求:根据分类的id得到该分类下的内容,并且设置分页 步骤: 1.设置分页 2.获取数据 3.封装对象 4.发布服务,5.添加Redis缓存
		 */
		// PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		// PageInfo<TbContent> info = new PageInfo<>(list);
		// 内存分类,

		List<TbContent> pageList = new ArrayList<>();
		if (!list.isEmpty() || list.size() > 0) {
			for (int i = (page - 1) * rows; i < ((page - 1) * rows + rows > list.size() ? list.size()
					: (page - 1) * rows + rows); i++) {
				pageList.add(list.get(i));
			}
		}

		EasyUIDataResult result = new EasyUIDataResult();
		result.setTotal((long)list.size());
		result.setRows(pageList);
		return result;
	}

	public TaotaoResult addContent(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContent.setUpdated(tbContent.getCreated());
		contentMapper.insert(tbContent);
		return TaotaoResult.ok();
	}

	public List<TbContent> getContentByCategoryId(Long categoryId) {
		List<TbContent> list =null;
		try {
			list= JsonUtils.jsonToList(jedisClient.hget("categoryId_"+categoryId, categoryId+""), TbContent.class);
			if(!list.isEmpty()||list.size()>0){
				System.out.println("反馈>>>>>>>>从缓存中取值");
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example= new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		list= contentMapper.selectByExample(example);
		System.out.println("反馈>>>>>>>>从数据库中取值");
		try {
			jedisClient.hset("categoryId_"+categoryId, categoryId+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}

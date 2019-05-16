package com.suchuner.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suchuner.shop.common.EasyUIDataResult;
import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.content.service.IContentService;
import com.suchuner.shop.domain.TbContent;

@Controller
public class ContentController {
	@Autowired
	private IContentService contentService;

	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataResult getContent(Long categoryId,Integer page,Integer rows) {
		/*
		 * 根据分类的id得到该分类下内容 步骤: 1.引入服务 2.调用服务 3.返回数据
		 */
		return contentService.getContent(categoryId, page, rows);
	}
	
	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult addContent(TbContent tbContent) {
		/*
		 * 新增内容对象 步骤: 1.引入服务 2.调用服务 3.返回数据
		 */
		return contentService.addContent(tbContent);
	}
}

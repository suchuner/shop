package com.suchuner.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suchuner.shop.common.EasyUITreeResult;
import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.content.service.IContentCategoryService;

@Controller
public class ContentCategoryController {
	@Autowired
	private IContentCategoryService contentCategoryService;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeResult> getContentCategoryTree(@RequestParam(name="id",defaultValue="0")Long parentId) {
		return contentCategoryService.getContentCategoryTree(parentId);
	}
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult addContentCategory(Long parentId,String name){
		return contentCategoryService.addContentCategory( parentId, name);
	}
	@RequestMapping("/content/category/update")
	@ResponseBody
	public TaotaoResult updateContentCategory(Long id,String name){
		return contentCategoryService.updateContentCategory( id, name);
	}
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public TaotaoResult deleteContentCategory(Long id){
		return contentCategoryService.deleteContentCategory( id);
	}
}

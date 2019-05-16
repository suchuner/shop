package com.suchuner.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.search.service.ISearchItemService;

@Controller
public class SearchItemController {
	@Autowired
	private ISearchItemService searchItemService;
	@RequestMapping("/index/importall")
	@ResponseBody
	public TaotaoResult importAllItems(){
		/*需求:引入导入索引库的服务,返回结果
		 * */
		try {
			return searchItemService.importAllItems();
		} catch (Exception e) {
			e.printStackTrace();
			return  TaotaoResult.build(500,"索引库导入失败");
		}
	}
}

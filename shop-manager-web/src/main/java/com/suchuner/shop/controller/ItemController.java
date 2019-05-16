package com.suchuner.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suchuner.shop.common.EasyUIDataResult;
import com.suchuner.shop.common.TaotaoResult;
import com.suchuner.shop.domain.TbItem;
import com.suchuner.shop.service.IItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private IItemService itemService;
	@RequestMapping("/list")
	@ResponseBody
	public EasyUIDataResult findAll(Integer page,Integer rows){
		 EasyUIDataResult result = itemService.findAll(page, rows);
		return result;
	}
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult saveItem(TbItem item,String desc){
		/*获取服务层的方法步骤:
		 * 1.引入dubbo服务
		 * 2.调用方法
		 * */
		return itemService.saveItem( item, desc);
	}
	
}

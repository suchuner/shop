package com.suchuner.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suchuner.shop.common.EasyUITreeResult;
import com.suchuner.shop.service.IItemCatService;

@Controller
public class ItemCatController {
	@Autowired
	private IItemCatService itemCatService;

	@RequestMapping("/item/cat/list")
	public @ResponseBody List<EasyUITreeResult> getTree(@RequestParam(name="id",defaultValue="0")Long parentId){
		/*需求:从服务层获取数据,接收前端传递的参数,由于参数名不一致.,可以用@RequestParam来限定,和指定默认值,[因为第一次无值]
		 * 步骤:
		 * 0.指定URL
		 * 1.引入dubbo服务
		 * 2.调用方法得到数据
		 * 3.使用@ResponseBody返回json字符串
		 * */
		return itemCatService.getTree(parentId);
	}
}

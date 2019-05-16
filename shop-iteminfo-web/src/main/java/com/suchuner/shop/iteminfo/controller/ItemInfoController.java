package com.suchuner.shop.iteminfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.suchuner.shop.domain.TbItem;
import com.suchuner.shop.domain.TbItemDesc;
import com.suchuner.shop.iteminfo.pojo.Item;
import com.suchuner.shop.service.IItemService;

@Controller
public class ItemInfoController {
	@Autowired
	private IItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId,Model model){
		TbItem tbItem = itemService.findItemByItemId(itemId);
		Item item = new Item(tbItem);
		TbItemDesc itemDesc = itemService.findItemDescByItemId(itemId);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
}

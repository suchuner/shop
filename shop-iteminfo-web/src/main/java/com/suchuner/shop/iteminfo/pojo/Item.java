package com.suchuner.shop.iteminfo.pojo;

import org.springframework.beans.BeanUtils;

import com.suchuner.shop.domain.TbItem;

public class Item extends TbItem {
	public String[] getImages(){
		String images= getImage();
		if(images!=null&&!images.equals("")){
			return images.split(",");
		}
		return null;
	}
	public Item(TbItem tbItem){
		BeanUtils.copyProperties(tbItem, this);
	}
}

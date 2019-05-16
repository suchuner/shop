package com.suchuner.shop.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.suchuner.shop.content.service.IContentService;
import com.suchuner.shop.domain.TbContent;
import com.suchuner.shop.portal.pojo.Ad1Node;
import com.suchuner.shop.utils.JsonUtils;

@Controller
public class IndexController {
	@Autowired
	private IContentService contentService;
	
	@Value(value="${AD1_HEIGHT}")
	private String AD1_HEIGHT;
	@Value(value="${AD1_HEIGHT_B}")
	private String AD1_HEIGHT_B ;
	@Value(value="${AD1_WIDTH}")
	private String AD1_WIDTH;
	@Value(value="${AD1_WIDTH_B}")
	private String AD1_WIDTH_B;

	@Value(value="${AD1_CATEGORY_ID} ")
	private Long AD1_CATEGORY_ID;
	@RequestMapping("/index")
	public String showIndex(Model model){
		List<TbContent> list = contentService.getContentByCategoryId(AD1_CATEGORY_ID);
		List<Ad1Node> ad1s = new ArrayList<>();
		for (TbContent node : list) {
			Ad1Node ad1 = new Ad1Node();
			ad1.setAlt(node.getSubTitle());
			ad1.setSrc(node.getPic());
			ad1.setSrcB(node.getPic2());
			ad1.setHref(node.getUrl());
			ad1.setHeight(AD1_HEIGHT);
			ad1.setHeightB(AD1_HEIGHT_B);
			ad1.setWidth(AD1_WIDTH);
			ad1.setWidthB(AD1_WIDTH_B);
			ad1s.add(ad1);
		}
		model.addAttribute("ad1s", JsonUtils.objectToJson(ad1s));
		return "index";
	}
}

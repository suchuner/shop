package com.suchuner.shop.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.suchuner.shop.common.SearchResult;
import com.suchuner.shop.search.service.ISearchItemService;

@Controller
public class SearchController {
	@Autowired
	private ISearchItemService searchItemService;

	@Value("${SEARCH_SHOW_PAGESIZE}")
	private Long pageSize;

	@RequestMapping("/search")
	public String search(@RequestParam(name = "q") String queryString,
			@RequestParam(name="page",defaultValue = "1") Long currPage,Model model) throws Exception {
		queryString=new String(queryString.getBytes("iso8859-1"), "utf-8");
		SearchResult searchResult = searchItemService.search(queryString, currPage, pageSize);
		model.addAttribute("searchResult", searchResult);
		model.addAttribute("query", queryString);
		model.addAttribute("page", currPage);
		return "search";
	}
}

package cn.itcast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.itcast.domain.SearchResult;
import cn.itcast.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/list")
	public String SolrSearch(String queryString,String catalog_name,String price,
			@RequestParam(defaultValue="1") Integer page,
			@RequestParam(defaultValue="20") Integer rows,
			@RequestParam(defaultValue = "1") String sort,Model model) {
		SearchResult result = itemService.solrSearch(queryString, catalog_name, price, page, rows, sort);
		model.addAttribute("result", result);
		model.addAttribute("queryString", queryString);
		model.addAttribute("catalog_name",catalog_name);
		model.addAttribute("price",price);
		model.addAttribute("sort",sort);
		return "/product_list";
	}
}

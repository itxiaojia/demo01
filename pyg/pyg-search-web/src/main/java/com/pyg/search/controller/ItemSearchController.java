package com.pyg.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.service.search.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/11 16:16
 */
@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {

	@Reference
	private ItemSearchService itemSearchService;

	@RequestMapping("/search")
	public Map search(@RequestBody Map searchMap){
		return itemSearchService.search(searchMap);
	}
}

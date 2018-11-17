package com.pyg.protal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.content.service.ContentService;
import com.pyg.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/8 21:09
 */
@RestController
@RequestMapping("/content")
public class IndexController {

	//远程注入
	@Reference
	private ContentService contentService;

	@RequestMapping("/findContentCategoryById")
	public List<TbContent> findContentCategoryById(Long categoryId){
		return contentService.findContentCategoryById(categoryId);
	}
}

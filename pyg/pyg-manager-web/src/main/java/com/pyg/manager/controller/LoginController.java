package com.pyg.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/24 10:28
 */
@RestController
@RequestMapping("/login")
public class LoginController {

	@RequestMapping("/name")
	public Map name(){
		String username= SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new HashMap<>();
		map.put("loginName", username);
		return map;
	}
}

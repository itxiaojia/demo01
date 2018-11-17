package com.pyg.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.Cart;
import com.pyg.service.cart.CartService;
import com.pyg.utils.PygResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/18 16:18
 */
@RestController
@RequestMapping("/cart")
public class CartController {

	@Reference
	private CartService cartService;

	/**
	 * 用户未登录状态下
	 * @param session
	 * @return
	 */
	@RequestMapping("/addGoodsToCartList")
	@CrossOrigin(origins="http://item.pyg.com",allowCredentials="true")
	public PygResult addGoodsToCartList(HttpSession session, HttpServletResponse response,Long itemId, Integer num){
		try {

			//设置那台服务器可以访问
			//response.setHeader("Access-Control-Allow-Origin", "http://item.pyg.com");
			//response.setHeader("Access-Control-Allow-Credentials", "true");//允许传递cookie

			//1,获取sessionId,每一次请求过来都获取SessionID
			String sessionId = session.getId();

			//获取登陆名
			String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
			System.out.println(loginName);
			if (!"anonymousUser".equals(loginName)){
				sessionId=loginName;
			}
			//2,先获取在Redis中SessionID对应的集合
			List<Cart> cartList=cartService.findCartListFromRedis(sessionId);
			//3,把传递过来的商品放在购物车集合中
			cartList=cartService.addGoodsToCartList(cartList,itemId,num);
			//4,放回Redis中
			cartService.addCartListToRedis(sessionId,cartList);
			return new PygResult(true,"添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new PygResult(false,"添加失败");
		}
	}

	@RequestMapping("/findCartList")
	public List<Cart> findCartList(HttpSession session){
		//获取sessionid
		String sessionId = session.getId();
		List<Cart> cartList_session = cartService.findCartListFromRedis(sessionId);

		String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
		if("anonymousUser".equals(loginName)){
			return cartList_session;
		}
		//获取当前登录者对应的数据
		List<Cart> cartList = cartService.findCartListFromRedis(loginName);
		if(cartList_session.size() > 0){
			//合并购物车
			cartList=cartService.mergeCartList(cartList, cartList_session);
			//清除redis中sessionId的数据
			cartService.delCartListToRedis(sessionId);
			//将合并后的数据存入redis
			cartService.addCartListToRedis(loginName, cartList);

		}
		return cartList;

	}

}

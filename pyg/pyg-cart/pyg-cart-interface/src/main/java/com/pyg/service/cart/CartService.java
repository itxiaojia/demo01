package com.pyg.service.cart;

import com.pyg.pojo.Cart;

import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/18 16:25
 */
public interface CartService {
	/**
	 * 从redis中查询购物车
	 * @param sessionId
	 * @return
	 */
	List<Cart> findCartListFromRedis(String sessionId);

	/**
	 *根据商品信息把商品添加到购物车集合中
	 * @param cartList
	 * @param itemId
	 * @param num
	 * @return
	 */
	List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);

	/**
	 * 将购物车保存到redis
	 * @param sessionId
	 * @param cartList
	 */
	void addCartListToRedis(String sessionId, List<Cart> cartList);

	/**
	 * 合并购物车,并返回总集合数据
	 * @param cartList
	 * @param cartList_session
	 * @return
	 */
	List<Cart> mergeCartList(List<Cart> cartList, List<Cart> cartList_session);

	/**
	 * 根据sessionId 清空redis数据
	 * @param sessionId
	 */
	void delCartListToRedis(String sessionId);
}

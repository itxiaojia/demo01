package com.pyg.service.cart.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.Cart;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbOrderItem;
import com.pyg.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/18 16:42
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public List<Cart> findCartListFromRedis(String sessionId) {
		//从redis中获取购物车数据
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(sessionId);
		if (cartList != null) {
			//判断redis中是否有数据
			return cartList;
		}
		return new ArrayList<>();
	}

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		//1.根据商品SKU ID查询SKU商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//判断
		if (item == null) {
			throw new RuntimeException("商品不存在");
		}
		;
		if ("0".equals(item.getStatus())) {
			throw new RuntimeException("商品不在状态");
		}
		;

		//2.获取商家ID
		String sellerId = item.getSellerId();

		//3.根据商家ID判断购物车列表中是否存在该商家的购物车
		Cart cart = searchCartBySellerId(sellerId, cartList);

		//4.如果购物车列表中不存在该商家的购物车
		if (cart == null) {
			//4.1 新建购物车对象
			cart = new Cart();
			cart.setSellerId(sellerId);
			cart.setSellerName(item.getSeller());
			//创建新的商品对象
			TbOrderItem orderItem = createOrderItem(item, num);
			List orderItemList = new ArrayList();

			orderItemList.add(orderItem);
			cart.setOrderItemList(orderItemList);
			//4.2 将新建的购物车对象添加到购物车列表
			cartList.add(cart);
		} else {
			//5.如果购物车列表中存在该商家的购物车
			TbOrderItem orderItem = searchOrderItemByItemId(itemId, cart.getOrderItemList());
			// 查询购物车明细列表中是否存在该商品
			if (orderItem == null) {
				//5.1. 如果没有，新增购物车明细
				orderItem = createOrderItem(item, num);
				cart.getOrderItemList().add(orderItem);

			} else {
				//5.2. 如果有，在原购物车明细上添加数量，更改金额
				orderItem.setNum(orderItem.getNum() + num);
				orderItem.setTotalFee(new BigDecimal(orderItem.getNum() * orderItem.getPrice().doubleValue()));
				//如果数量操作后小于等于0，则移除
				if (orderItem.getNum() <= 0) {
					cart.getOrderItemList().remove(orderItem);//移除购物车明细
				}
				//如果移除后cart的明细数量为0，则将cart移除
				if (cart.getOrderItemList().size() == 0) {
					cartList.remove(cart);
				}

			}

		}

		return cartList;
	}

	/**
	 * 判断商品集合中是否存在传递来的商品
	 *
	 * @param itemId
	 * @param orderItemList
	 * @return
	 */
	private TbOrderItem searchOrderItemByItemId(Long itemId, List<TbOrderItem> orderItemList) {
		for (TbOrderItem orderItem : orderItemList) {
			if (itemId.longValue() == orderItem.getItemId().longValue()) {
				return orderItem;
			}
		}
		return null;
	}

	/**
	 * 创建商品对象
	 *
	 * @param item
	 * @param num
	 * @return
	 */
	private TbOrderItem createOrderItem(TbItem item, Integer num) {
		if (num <= 0) {
			throw new RuntimeException("数量非法");
		}

		TbOrderItem orderItem = new TbOrderItem();
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setItemId(item.getId());
		orderItem.setNum(num);
		orderItem.setPicPath(item.getImage());
		orderItem.setPrice(item.getPrice());
		orderItem.setSellerId(item.getSellerId());
		orderItem.setTitle(item.getTitle());
		orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue() * num));
		return orderItem;

	}

	/**
	 * 根据商家id查询购物车集合
	 *
	 * @param sellerId
	 * @param cartList
	 * @return
	 */
	private Cart searchCartBySellerId(String sellerId, List<Cart> cartList) {
		//循环遍历购物车集合
		for (Cart cart : cartList) {
			//判断 如果添加的商家id跟查出来的id一样
			if (sellerId.equals(cart.getSellerId())) {
				return cart;
			}
		}
		return null;
	}

	@Override
	public void addCartListToRedis(String sessionId, List<Cart> cartList) {
		redisTemplate.boundHashOps("cartList").put(sessionId,cartList );
	}

	@Override
	public List<Cart> mergeCartList(List<Cart> cartList, List<Cart> cartList_session) {

		for (Cart cart : cartList_session) {
			for (TbOrderItem orderItem : cart.getOrderItemList()) {
				cartList=addGoodsToCartList(cartList,orderItem.getItemId() ,orderItem.getNum() );
			}
		}
		return cartList;
	}

	@Override
	public void delCartListToRedis(String sessionId) {
		redisTemplate.boundHashOps("cartList").delete(sessionId);
	}
}

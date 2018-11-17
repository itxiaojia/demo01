package com.pyg.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/18 15:34
 */
//购物车实体类
public class Cart implements Serializable {

	private String sellerId;//商家id

	private String sellerName;//商家名称

	private List<TbOrderItem> orderItemList;//购物车集合

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public List<TbOrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<TbOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
}

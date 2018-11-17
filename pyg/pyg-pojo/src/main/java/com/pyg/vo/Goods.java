package com.pyg.vo;

import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/26 15:08
 */
public class Goods implements Serializable {

	//商品spu表
	private TbGoods goods;

	//商品描述
	private TbGoodsDesc goodsDesc;

	//商品sku表
	private List<TbItem> itemList;

	public TbGoods getGoods() {
		return goods;
	}

	public void setGoods(TbGoods goods) {
		this.goods = goods;
	}

	public TbGoodsDesc getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(TbGoodsDesc goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public List<TbItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<TbItem> itemList) {
		this.itemList = itemList;
	}
}

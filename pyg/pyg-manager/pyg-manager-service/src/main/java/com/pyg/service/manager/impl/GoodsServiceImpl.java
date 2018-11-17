package com.pyg.service.manager.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.GoodsService;
import com.pyg.mapper.*;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbGoodsExample;
import com.pyg.pojo.TbGoodsExample.Criteria;
import com.pyg.pojo.TbItem;
import com.pyg.utils.PageResult;
import com.pyg.vo.Goods;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper tbGoodsDescMapper;

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private TbBrandMapper brandMapper;

	@Autowired
	private TbSellerMapper sellerMapper;

	@Autowired
	private TbItemMapper itemMapper;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加商品
	 */
	@Override
	public void add(Goods goods) {
		//获取spu商品对象
		TbGoods goods1 = goods.getGoods();
		goods1.setAuditStatus("0");
		goods1.setIsMarketable("0");
		goodsMapper.insertSelective(goods1);
		//获取商品描述信息
		TbGoodsDesc goodsDesc = goods.getGoodsDesc();
		//设置描述表主键
		goodsDesc.setGoodsId(goods1.getId());
		tbGoodsDescMapper.insertSelective(goodsDesc);

		//保存sku表
		List<TbItem> itemList = goods.getItemList();
		for (TbItem tbItem : itemList) {
			//设置item的数据
			//title数据是spuname+规格
			String title = goods1.getGoodsName();
			Map<String, String> map = JSON.parseObject(tbItem.getSpec(), Map.class);
			for (String key : map.keySet()) {
				title += "  " + map.get(key);
			}
			tbItem.setTitle(title);
			tbItem.setCategoryid(goods1.getCategory3Id());
			tbItem.setCreateTime(new Date());
			tbItem.setUpdateTime(new Date());
			tbItem.setGoodsId(goods1.getId());
			tbItem.setSellerId(goods1.getSellerId());

			//通过分类id查询分类对象并把名赋值给tbitem
			String category = itemCatMapper.selectByPrimaryKey(tbItem.getCategoryid()).getName();
			tbItem.setCategory(category);

			//根据品牌id查询品牌数据并将name保存
			String brand = brandMapper.selectByPrimaryKey(goods1.getBrandId()).getName();
			tbItem.setBrand(brand);

			//根据商家id查询商家名称 并赋值
			String seller = sellerMapper.selectByPrimaryKey(tbItem.getSellerId()).getNickName();
			tbItem.setSeller(seller);

			//设置图片
			//先把goodsDesc中的图片取出来,然后判断是否存在,如果有之,获取默认的第一个图片
			List<Map> maps = JSON.parseArray(goodsDesc.getItemImages(), Map.class);
			if (maps.size() > 0) {
				String url = (String) maps.get(0).get("url");
				tbItem.setImage(url);
			}
			itemMapper.insertSelective(tbItem);
		}
	}


	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goodsDesc) {
		goodsMapper.updateByPrimaryKey(goodsDesc);
	}

	/**
	 * 根据ID获取实体
	 *
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id) {
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			goodsMapper.deleteByPrimaryKey(id);
		}
	}


	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbGoodsExample example = new TbGoodsExample();
		Criteria criteria = example.createCriteria();

		if (goods.getSellerId()!= null&&goods.getSellerId().length()>0) {
			criteria.andSellerIdEqualTo(goods.getSellerId());
		}

		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 查询商品列表
	 * @param sellerId
	 * @return
	 */
	public List<TbGoods> findGoodsBySellerId(String sellerId) {
		TbGoodsExample example = new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andSellerIdEqualTo(sellerId);
		List<TbGoods> goodsList = goodsMapper.selectByExample(example);
		return goodsList;
	}

	/**
	 * 需求:批量修改商品状态
	 * @param ids
	 * @param status
	 */
	public void updateStatus(Long[] ids, String status) {
		//循环
		for (Long id : ids) {
			//根据id查询出商品
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			//更改状态
			tbGoods.setAuditStatus(status);
			//更新
			goodsMapper.updateByPrimaryKeySelective(tbGoods);
		}
	}

	/**
	 * 修改商品上下架状态
	 * @param ids
	 * @param status
	 */
	public void updateMarketableStatus(Long[] ids, String status) {
		//循环
		for (Long id : ids) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setIsMarketable(status);
			goodsMapper.updateByPrimaryKeySelective(tbGoods);
		}
	}


}

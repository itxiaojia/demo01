package com.pyg.service.page.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.mapper.TbGoodsDescMapper;
import com.pyg.mapper.TbGoodsMapper;
import com.pyg.mapper.TbItemCatMapper;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.*;
import com.pyg.service.page.ItemPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/12 20:11
 */
@Service
public class ItemPageServiceImpl implements ItemPageService {

	//注入freemarker对象
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public void getHtml(Long id) {

		try {
			//1,创建freemarker的核心对象
			//2,指定模板路径
			//3,设置编码集
			Configuration configuration = freeMarkerConfig.getConfiguration();
			//4,获取模板
			Template template = configuration.getTemplate("item.ftl");
			//5,根据商品id获取商品数据
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);

			//获取面包屑数据
			TbItemCat itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id());
			TbItemCat itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id());
			TbItemCat itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());

			//根据商品id查询sku列表信息
			TbItemExample example = new TbItemExample();
			TbItemExample.Criteria criteria = example.createCriteria();
			//goodsId=id
			criteria.andGoodsIdEqualTo(id);
			//status=1
			criteria.andStatusEqualTo("1");
			//排序
			example.setOrderByClause("is_default desc");
			List<TbItem> itemList = itemMapper.selectByExample(example);

			//6,封装数据,使用map对象
			Map map=new HashMap();
			map.put("goods", goods);
			map.put("goodsDesc",goodsDesc );
			map.put("itemCat1",itemCat1 );
			map.put("itemCat2",itemCat2 );
			map.put("itemCat3",itemCat3 );
			map.put("itemList",itemList );
			//7,指定写出的路径以及html文件
			Writer fileWriter = new OutputStreamWriter(new FileOutputStream(new File("D:\\ideawork\\pyg1\\item\\"
					+id+".html")),"utf-8");
			//8,调用模板文件输出文件
			template.process(map,fileWriter );
			//9,释放资源
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

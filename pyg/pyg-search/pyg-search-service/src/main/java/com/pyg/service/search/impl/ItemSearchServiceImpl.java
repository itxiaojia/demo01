package com.pyg.service.search.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemExample;
import com.pyg.service.search.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.jms.core.JmsTemplate;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/11 16:19
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

	//查询solr索引库
	@Autowired
	private SolrTemplate solrTemplate;

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public Map search(Map searchMap) {

		//1,创建搜索对象
		//SimpleQuery query = new SimpleQuery("*:*");
		SimpleHighlightQuery query = new SimpleHighlightQuery();

		//2.1,判断条件
		String keywords = (String)searchMap.get("keywords");
		if (keywords!=null&&!"".equals(keywords)){
			//3,将条件加入搜索对象中
			Criteria criteria = new Criteria("item_keywords").is(keywords);
			query.addCriteria(criteria);
		}else{
			Criteria criteria = new Criteria();
			criteria.expression("*:*");
			query.addCriteria(criteria);
		}

		//2.2分类的过滤条件
		String category = (String)searchMap.get("category");
		if (category!=null&&!"".equals(category)){
			//过滤条件对象
			SimpleFilterQuery filterQuery = new SimpleFilterQuery();
			Criteria criteria = new Criteria("item_category").is(category);
			filterQuery.addCriteria(criteria);
			//将过滤条件加入query
			query.addFilterQuery(filterQuery);
		}

		//2.3品牌的过滤条件
		String brand = (String)searchMap.get("brand");
		if (brand!=null&&!"".equals(brand)){
			//创建过滤条件对象
			SimpleFilterQuery filterQuery = new SimpleFilterQuery();
			//指定域
			Criteria criteria = new Criteria("item_brand").is(brand);
			filterQuery.addCriteria(criteria);
			//将过滤条件对象加入query
			query.addFilterQuery(filterQuery);
		}

		//2.4价格的过滤条件
		String price = (String)searchMap.get("price");
		if (price!=null&&!"".equals(price)){
			//截取价格字符串 -
			String[] split = price.split("-");
			if (!"0".equals(split[0])){
				//创建过滤条件对象
				SimpleFilterQuery filterQuery = new SimpleFilterQuery();
				//指定域
				Criteria criteria = new Criteria("item_price").greaterThanEqual(split[0]);
				filterQuery.addCriteria(criteria);
				//将过滤条件对象加入query
				query.addFilterQuery(filterQuery);
			}

			if (!"*".equals(split[1])){
				//创建过滤条件对象
				SimpleFilterQuery filterQuery = new SimpleFilterQuery();
				//指定域
				Criteria criteria = new Criteria("item_price").lessThanEqual(split[1]);
				filterQuery.addCriteria(criteria);
				//将过滤条件对象加入query
				query.addFilterQuery(filterQuery);
			}
		}

		//2.5规格的过滤条件
		Map<String,String> specMap = (Map<String,String>)searchMap.get("spec");
		if (specMap!=null){
			for (String key : specMap.keySet()) {
				//创建过滤条件对象
				SimpleFilterQuery filterQuery = new SimpleFilterQuery();
				//指定域
				Criteria criteria = new Criteria("item_spec_"+key).is(specMap.get(key));
				filterQuery.addCriteria(criteria);
				//将过滤条件对象加入query
				query.addFilterQuery(filterQuery);
			}
		}

		//2.6排序查询
		String sortField = (String)searchMap.get("sortField");
		String sort = (String)searchMap.get("sort");

		if (sortField!=null&&!"".equals(sortField)){
			if ("ASC".equals(sort)){
				Sort sort1 = new Sort(Sort.Direction.ASC, "item_" + sortField);
				query.addSort(sort1);
			}
			if ("DESC".equals(sort)){
				Sort sort1 = new Sort(Sort.Direction.DESC, "item_" + sortField);
				query.addSort(sort1);
			}
		}

		//2.7设置分页
		Integer page = (Integer)searchMap.get("page");
		Integer pageSize = (Integer)searchMap.get("pageSize");
		//判断
		if (page==null){
			page=1;
		}
		if (pageSize==null){
			pageSize=30;
		}
		query.setOffset((page-1)*pageSize);//起始页
		query.setRows(pageSize);//每页显示条数

		//设置高亮条件
		HighlightOptions options = new HighlightOptions();
		//设置高亮域
		options.addField("item_title");
		//设置前缀
		options.setSimplePrefix("<span style=\"color:red\">");
		//设置后缀
		options.setSimplePostfix("</span>");
		query.setHighlightOptions(options);
		//4,通过solrTemplate搜索
		HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

		//5,获取高亮数据
		for(HighlightEntry<TbItem> h: highlightPage.getHighlighted()){//循环高亮入口集合
			TbItem item = h.getEntity();//获取原实体类
			if(h.getHighlights().size()>0 && h.getHighlights().get(0).getSnipplets().size()>0){
				//将高亮数据设置到item中
				item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));//设置高亮的结果
			}
		}
		//6,获取搜索结果
		List<TbItem> itemList = highlightPage.getContent();
		//获取分页数据
		long total = highlightPage.getTotalElements();//总条数
		int totalPages = highlightPage.getTotalPages();//总页数
		//7,封装数据返回
		Map map = new HashMap<>();
		map.put("rows",itemList );
		map.put("total",total );
		map.put("totalPages",totalPages );
		map.put("pageNo",page );

		return map;

	}

	@Override
	public void importSolr(Long id) {
		//根据条件查询
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);
		List<TbItem> itemList = itemMapper.selectByExample(example);
		//规格是动态域
		for (TbItem tbItem : itemList) {
			Map map= JSON.parseObject(tbItem.getSpec(),Map.class);
			tbItem.setSpecMap(map);
		}

		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
	}

	@Override
	public void deleteSolr(Long id) {
		solrTemplate.delete(new SimpleQuery("item_goodsid:"+id));
		solrTemplate.commit();
	}
}

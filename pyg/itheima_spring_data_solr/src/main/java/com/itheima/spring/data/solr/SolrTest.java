package com.itheima.spring.data.solr;

import com.pyg.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/9 18:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-solr.xml")
public class SolrTest {

	@Autowired
	private SolrTemplate solrTemplate;

	@Test
	public void addAndUpdate(){
		List<TbItem> list = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			TbItem item = new TbItem();
			item.setId(10001l+i);
			item.setGoodsId(20000l+i);
			item.setTitle("华为mate10"+i);
			item.setBrand("华为");
			item.setImage("http://1234567.jpg");
			item.setSeller("华为旗舰店"+i);
			item.setCategory("手机");
			item.setPrice(new BigDecimal(1999+i));
			list.add(item);
		}

		solrTemplate.saveBeans(list);

		solrTemplate.commit();
	}

	@Test
	public void delete(){
		//根据id删除
		//solrTemplate.deleteById("10000");

		//根据条件删除
		solrTemplate.delete(new SimpleQuery("*:*"));
		solrTemplate.commit();
	}

	@Test
	public void query(){
		//创建查询条件
		SimpleQuery query = new SimpleQuery();
		//添加条件对象
		Criteria criteria = new Criteria("item_title").contains("2");
		query.addCriteria(criteria);
		//设置分页
		query.setOffset(0);//其实索引
		query.setRows(10);//每页显示条数
		ScoredPage<TbItem> queryForPage = solrTemplate.queryForPage(query, TbItem.class);

		List<TbItem> content = queryForPage.getContent();
		System.err.println("总记录数="+queryForPage.getTotalElements());
		for (TbItem tbItem : content) {
			System.out.println(tbItem.getBrand()+"=="+tbItem.getPrice());
		}
	}

	@Test//高亮查询
	public void queryHightLight(){
		//创建高亮查询条件
		SimpleHighlightQuery query = new SimpleHighlightQuery();

		//设置搜索条件
		Criteria criteria = new Criteria("item_title").contains("2");
		query.addCriteria(criteria);

		//创建高亮对象,设置高亮
		HighlightOptions highlightOptions = new HighlightOptions();
		highlightOptions.addField("item_title");
		highlightOptions.setSimplePrefix("<font color='red'>");
		highlightOptions.setSimplePostfix("</font>");

		//将高亮对象设置到query中
		query.setHighlightOptions(highlightOptions);

		HighlightPage<TbItem> items = solrTemplate.queryForHighlightPage(query, TbItem.class);
		long totalElements = items.getTotalElements();
		System.out.println("获取总记录数="+totalElements);

		//获取总记录
		List<TbItem> content = items.getContent();
		for (TbItem tbItem : content) {
			//获取高亮
			List<HighlightEntry.Highlight> highlights = items.getHighlights(tbItem);
			List<String> snipplets = highlights.get(0).getSnipplets();
			System.out.println(snipplets);
		}
	}
}

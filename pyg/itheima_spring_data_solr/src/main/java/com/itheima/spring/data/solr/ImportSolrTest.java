package com.itheima.spring.data.solr;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/9 20:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext*.xml")
public class ImportSolrTest {

	@Autowired
	private SolrTemplate solrTemplate;

	@Autowired
	private TbItemMapper itemMapper;

	@Test
	public void importSolr(){
		//1,查询数据库
		List<TbItem> tbItems = itemMapper.selectByExample(null);

		for (TbItem tbItem : tbItems) {
			Map map = JSON.parseObject(tbItem.getSpec(), Map.class);
			tbItem.setSpecMap(map);
		}
		//2,将集合数据放入solr
		solrTemplate.saveBeans(tbItems);

		solrTemplate.commit();
	}
}

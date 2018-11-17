package com.pyg;

import com.pyg.mapper.TbBrandMapper;
import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbBrandExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/22 20:09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/spring/applicationContext-dao.xml")
public class MbgTest {

	//注入mapper代理对象
	@Autowired
	private TbBrandMapper tbBrandMapper;
	/**
	 * 品牌添加测试
	 */
	@Test
	public void save(){
		TbBrand tbBrand = new TbBrand();
		tbBrand.setName("大海");
		tbBrand.setFirstChar("D");
		tbBrandMapper.insertSelective(tbBrand);

	}
	/**
	 * 品牌修改测试
	 */
	@Test
	public void update(){
		TbBrand tbBrand = new TbBrand();
		tbBrand.setId(56L);
		tbBrand.setName("小孩");
		tbBrand.setFirstChar("D");
		tbBrandMapper.updateByPrimaryKeySelective(tbBrand);

	}
	/**
	 * 品牌修改测试
	 */
	@Test
	public void delete(){
		tbBrandMapper.deleteByPrimaryKey(56L);
	}
	/**
	 * 品牌查询测试
	 */
	@Test
	public void query(){
		TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(1L);
		System.out.println(tbBrand.getName());
	}
	/**
	 * 品牌查询测试
	 */
	@Test
	public void queryAll(){
		List<TbBrand> tbBrands = tbBrandMapper.selectByExample(null);
		for (TbBrand tbBrand : tbBrands) {
			System.out.println(tbBrand.getName());
		}
	}
	/**
	 * 品牌条件查询测试 and
	 */
	@Test
	public void query1(){
		TbBrandExample tbBrandExample = new TbBrandExample();
		TbBrandExample.Criteria criteria = tbBrandExample.createCriteria();
		criteria.andNameLike("%海%");
		criteria.andIdEqualTo(17L);
		List<TbBrand> tbBrands = tbBrandMapper.selectByExample(tbBrandExample);
		for (TbBrand tbBrand : tbBrands) {
			System.out.println(tbBrand.getName());
		}
	}
	/**
	 * 品牌条件查询测试 or
	 */
	@Test
	public void query2(){
		TbBrandExample example = new TbBrandExample();
		example.or().andNameLike("%海%");
		example.or().andIdEqualTo(55L);

		List<TbBrand> tbBrands = tbBrandMapper.selectByExample(example);
		for (TbBrand tbBrand : tbBrands) {
			System.out.println(tbBrand.getName());
		}
	}
}

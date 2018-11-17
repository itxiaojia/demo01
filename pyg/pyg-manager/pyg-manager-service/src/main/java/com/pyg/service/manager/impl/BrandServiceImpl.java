package com.pyg.service.manager.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pyg.manager.service.BrandService;
import com.pyg.mapper.BrandMapper;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.pojo.QueryVo;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.Query;
import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/17 18:36
 */
@Service
public class BrandServiceImpl implements BrandService {

	//注入mapper对象
	@Autowired
	private TbBrandMapper brandMapper;

	/**
	 *	查询所有品牌
	 */
	public List<TbBrand> findAll() {
		return brandMapper.selectByExample(null);
	}

	/**
	 * 需求:分页查询所有品牌
	 */
	public PageResult findPage(Integer page, Integer rows) {
		//开启分页
		PageHelper.startPage(page,rows );

		Page<TbBrand> pageInfo = (Page<TbBrand>) brandMapper.selectByExample(null);

		return new PageResult(pageInfo.getTotal(),pageInfo.getResult());
	}

	/**
	 * 需求:添加品牌
	 */
	public void insert(TbBrand tbBrand) {
		brandMapper.insert(tbBrand);
	}

	/**
	 * 需求:根据id修改
	 */
	public TbBrand findOne(long id) {
		return brandMapper.selectByPrimaryKey(id);
	}

	/**
	 * 需求:根据id更新品牌数据
	 */
	public void update(TbBrand tbBrand) {
		brandMapper.updateByPrimaryKey(tbBrand);
	}

	/**
	 * 需求:更具id删除品牌数据
	 */
	public void delete(Long[] ids) {
		//循环删除
		for (Long id : ids) {
			brandMapper.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 需求:条件查询
	 */
	public PageResult findPage(TbBrand tbBrand, Integer page, Integer rows) {
//		//开启分页
//		PageHelper.startPage(page,rows);
//
//		QueryVo queryVo = new QueryVo(page, rows, tbBrand);
//
//		Page<TbBrand> pageInfo = (Page<TbBrand>) brandMapper.selectByExample(queryVo);
//
//		System.out.println(pageInfo.getResult());
		return null;//new PageResult(pageInfo.getTotal(),pageInfo.getResult());
	}

	/**
	 * 需求:查询品牌数据,进行下拉列表展示
	 */
	public List<Map> findBrandList() {

		return brandMapper.findBrandList();
	}
}

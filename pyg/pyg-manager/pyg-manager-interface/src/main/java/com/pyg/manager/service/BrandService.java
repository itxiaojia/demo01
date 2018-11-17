package com.pyg.manager.service;

import com.pyg.pojo.QueryVo;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/17 18:35
 */
public interface BrandService {

	/**
	 * 查询所有品牌
	 */
	public List<TbBrand> findAll();

	/**
	 * 需求:分页查询所有品牌
	 */
	public PageResult findPage(Integer page, Integer rows);

	/**
	 * 需求:添加品牌
	 */
	public void insert(TbBrand tbBrand);

	/**
	 * 需求:根据id修改
	 */
	public TbBrand findOne(long id);

	/**
	 * 需求:根据id更新品牌数据
	 */
	public void update(TbBrand tbBrand);

	/**
	 * 需求:更具id删除品牌数据
	 */
	public void delete(Long[] ids);

	/**
	 * 需求:条件查询
	 */
	public PageResult findPage(TbBrand tbBrand, Integer page, Integer rows);

	/**
	 * 需求:查询品牌数据,进行下拉列表展示
	 */
	public List<Map> findBrandList();
}

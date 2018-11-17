package com.pyg.mapper;

import com.pyg.pojo.QueryVo;
import com.pyg.pojo.TbBrand;
import com.pyg.utils.PageResult;

import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/17 18:00
 */
public interface BrandMapper {

	/**
	 * 需求:查询所有品牌
	 */
	public List<TbBrand> findAll();

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
	public void delete(Long id);

	/**
	 * 需求:根据品牌名称,首字母查询,并分页
	 */
	public List<TbBrand> findQv(QueryVo queryVo);
}

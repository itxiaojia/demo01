package com.pyg.manager.service;

import com.pyg.pojo.TbSpecification;
import com.pyg.utils.PageResult;
import com.pyg.vo.Specifiaction;

import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/22 21:22
 */
public interface SpecificationService {
	/**
	 * 查询所有规格
	 */
	public List<TbSpecification> findAll();

	/**
	 * 需求:分页查询所有规格
	 */
	public PageResult findPage(Integer page, Integer rows);

	/**
	 * 需求:添加规格
	 */
	public void insert(Specifiaction specifiaction);

	/**
	 * 需求:根据id修改
	 */
	public Specifiaction findOne(long id);

	/**
	 * 需求:根据id更新规格数据
	 */
	public void update(Specifiaction specifiaction);

	/**
	 * 需求:更具id删除规格数据
	 */
	public void delete(Long[] ids);

	/**
	 * 需求:查询规格数据进行多项选择
	 */
	public List<Map> findSpecList();
}

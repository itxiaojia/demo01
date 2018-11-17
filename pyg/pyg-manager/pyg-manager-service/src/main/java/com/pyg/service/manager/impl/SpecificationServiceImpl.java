package com.pyg.service.manager.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.manager.service.SpecificationService;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.mapper.TbSpecificationMapper;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationExample;
import com.pyg.pojo.TbSpecificationOption;
import com.pyg.pojo.TbSpecificationOptionExample;
import com.pyg.utils.PageResult;
import com.pyg.vo.Specifiaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/22 21:24
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {
	//注入mapper对象
	@Autowired
	private TbSpecificationMapper specificationMapper;

	@Autowired
	private TbSpecificationOptionMapper tbSpecificationOptionMapper;

	/**
	 *	查询所有规格
	 */
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 需求:分页查询所有规格
	 */
	public PageResult findPage(Integer page, Integer rows) {
		//开启分页
		PageHelper.startPage(page,rows );

		Page<TbSpecification> pageInfo = (Page<TbSpecification>) specificationMapper.selectByExample(null);

		return new PageResult(pageInfo.getTotal(),pageInfo.getResult());
	}

	/**
	 * 需求:添加规格
	 */
	public void insert(Specifiaction specifiaction) {
		//获取规格对象,保存规格
		TbSpecification tbSpecification = specifiaction.getTbSpecification();
		specificationMapper.insert(tbSpecification);

		//保存规格选项
		List<TbSpecificationOption> specificationOptionList = specifiaction.getSpecificationOptionList();
		for (TbSpecificationOption tbSpecificationOption : specificationOptionList) {
			//设置外键
			Long id = tbSpecification.getId();
			tbSpecificationOption.setSpecId(id);
			//保存规格属性
			tbSpecificationOptionMapper.insertSelective(tbSpecificationOption);

		}
	}

	/**
	 * 需求:根据id修改
	 */
	public Specifiaction findOne(long id) {
		//查询规格对象
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		//根据外键查询规格选项
		//创建example对象
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		//创建criteria对象
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		//设置查询参数
		criteria.andSpecIdEqualTo(id);
		//执行查询
		List<TbSpecificationOption> specificationOptions = tbSpecificationOptionMapper.selectByExample(example);

		//创建包装类对象,封装查询结果
		Specifiaction specifiaction = new Specifiaction();
		specifiaction.setTbSpecification(tbSpecification);
		specifiaction.setSpecificationOptionList(specificationOptions);
		return specifiaction;
	}

	/**
	 * 需求:根据id更新规格数据
	 */
	public void update(Specifiaction specifiaction) {
		//获取规格对象,先修改规格
		TbSpecification tbSpecification = specifiaction.getTbSpecification();
		specificationMapper.updateByPrimaryKey(tbSpecification);

		//创建规格选项example对象
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(tbSpecification.getId());

		//先删除规格属性
		tbSpecificationOptionMapper.deleteByExample(example);

		//获取规格选项实现保存
		List<TbSpecificationOption> specificationOptionList = specifiaction.getSpecificationOptionList();
		for (TbSpecificationOption tbSpecificationOption : specificationOptionList) {
			//设置外键
			tbSpecificationOption.setSpecId(tbSpecification.getId());
			//保存规格属性
			tbSpecificationOptionMapper.insertSelective(tbSpecificationOption);

		}
	}

	/**
	 * 需求:更具id删除规格数据
	 */
	public void delete(Long[] ids) {
		//循环删除
		for (Long id : ids) {
			//删除规格选项
			//创建规格选项example
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			//创建criteria对象
			TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			//设置查询参数
			criteria.andIdEqualTo(id);
			//删除规格选项
			tbSpecificationOptionMapper.deleteByExample(example);

			//删除规格
			specificationMapper.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 需求:查询规格数据进行多项选择
	 */
	public List<Map> findSpecList() {
		return specificationMapper.findSpecList();
	}
}

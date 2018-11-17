package com.pyg.vo;

import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/22 22:53
 */
public class Specifiaction implements Serializable {

	//规格对象
	private TbSpecification tbSpecification;

	//一个规格对应多个规格选项
	private List<TbSpecificationOption> specificationOptionList;

	public Specifiaction() {
	}

	public Specifiaction(TbSpecification tbSpecification, List<TbSpecificationOption> specificationOptionList) {
		this.tbSpecification = tbSpecification;
		this.specificationOptionList = specificationOptionList;
	}

	public TbSpecification getTbSpecification() {
		return tbSpecification;
	}

	public void setTbSpecification(TbSpecification tbSpecification) {
		this.tbSpecification = tbSpecification;
	}

	public List<TbSpecificationOption> getSpecificationOptionList() {
		return specificationOptionList;
	}

	public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
		this.specificationOptionList = specificationOptionList;
	}
}

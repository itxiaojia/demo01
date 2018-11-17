package com.pyg.pojo;

import java.io.Serializable;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/20 18:54
 */
public class QueryVo implements Serializable {

	//当前页
	private Integer page;
	//每页显示条数
	private Integer rows;
	//条件参数对象
	private TbBrand tbBrand;

	public QueryVo(Integer page, Integer rows, TbBrand tbBrand) {
		this.page = page;
		this.rows = rows;
		this.tbBrand = tbBrand;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public TbBrand getTbBrand() {
		return tbBrand;
	}

	public void setTbBrand(TbBrand tbBrand) {
		this.tbBrand = tbBrand;
	}
}

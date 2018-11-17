package com.pyg.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/9/19 17:57
 */
public class PageResult implements Serializable {

	//总记录数
	private Long total;
	//总记录
	private List<?> rows;

	public PageResult(Long total, List<?> rows) {
		this.total = total;
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
}

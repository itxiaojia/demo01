package com.pyg.service.page;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/12 20:11
 */
//商品详情页接口
public interface ItemPageService {


	/**
	 * 根据SPUid生成商品的详情静态页面
	 * @param id
	 */
	void getHtml(Long id);
}

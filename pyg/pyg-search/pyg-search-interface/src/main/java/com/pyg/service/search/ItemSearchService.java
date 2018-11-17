package com.pyg.service.search;

import java.util.Map; /**
 * @Author: Mr.jia
 * @Date: 2018/10/11 16:19
 */
public interface ItemSearchService {

	/**
	 * 搜索
	 * @param searchMap
	 * @return
	 */
	Map search(Map searchMap);

	/**
	 * 根据id导入solr索引库
	 * @param id
	 */
	void importSolr(Long id);

	/**
	 * 根据id删除索引库
	 * @param id
	 */
	void deleteSolr(Long id);
}

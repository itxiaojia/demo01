package com.pyg.service.content.impl;

import java.util.List;

import com.pyg.content.service.ContentService;
import com.pyg.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbContentMapper;
import com.pyg.pojo.TbContent;
import com.pyg.pojo.TbContentExample;
import com.pyg.pojo.TbContentExample.Criteria;

import com.pyg.utils.PageResult;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		contentMapper.insert(content);
		//添加之前清空redis缓存
		redisTemplate.boundHashOps("contentList").delete(content.getCategoryId());

	}


	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content) {
		//在修改之前先根据id查询出内容对象
		TbContent content1 = contentMapper.selectByPrimaryKey(content.getId());
		//再根据对象categoryId删除之前的缓存.
		redisTemplate.boundHashOps("contentList").delete(content1.getCategoryId());
		//修改数据
		contentMapper.updateByPrimaryKey(content);
		//再删除现在的缓存
		if (content.getCategoryId().longValue()!=content1.getCategoryId().longValue()){
			redisTemplate.boundHashOps("contentList").delete(content.getCategoryId());
		}
	}

	/**
	 * 根据ID获取实体
	 *
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id) {
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			//删除之前先根据id查询
			TbContent content = contentMapper.selectByPrimaryKey(id);
			//再删除缓存
			redisTemplate.boundHashOps("contentList").delete(content.getCategoryId());
			contentMapper.deleteByPrimaryKey(id);
		}
	}


	@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();

		if (content != null) {
			if (content.getTitle() != null && content.getTitle().length() > 0) {
				criteria.andTitleLike("%" + content.getTitle() + "%");
			}
			if (content.getUrl() != null && content.getUrl().length() > 0) {
				criteria.andUrlLike("%" + content.getUrl() + "%");
			}
			if (content.getPic() != null && content.getPic().length() > 0) {
				criteria.andPicLike("%" + content.getPic() + "%");
			}
			if (content.getStatus() != null && content.getStatus().length() > 0) {
				criteria.andStatusLike("%" + content.getStatus() + "%");
			}

		}

		Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<TbContent> findCategoryListById(Long categoryId) {

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);

		return contentMapper.selectByExample(example);
	}

	@Override
	public List<TbContent> findContentCategoryById(Long categoryId) {

		//1,在查询数据库之前先查询缓存redis
		List<TbContent> contentList = (List<TbContent>)redisTemplate.boundHashOps("contentList").get(categoryId);
		//2,如果有数据直接返回
		if (contentList!=null){
			return contentList;
		}
		//3,没有数据再查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		criteria.andStatusEqualTo("1");
		contentList = contentMapper.selectByExample(example);
		//4,将数据放入redis中
		redisTemplate.boundHashOps("contentList").put(categoryId,contentList );
		return contentList;
	}

}

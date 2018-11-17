package com.pyg.spring.task.service;

import com.pyg.mapper.TbSeckillGoodsMapper;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/23 20:11
 */
@Component
public class ImportTask {
	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	/***
	 * 每年双十一启动秒杀
	 * 将商品数据全部跟新到索引库
	 */
	@Scheduled(cron = "0/3 * * * * ?")  //我们这里测试数据每30秒执行
	public void startSeckill() {
		TbSeckillGoodsExample example = new TbSeckillGoodsExample();
		TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
		//商品审核通过
		criteria.andStatusEqualTo("1");
		//库存数量>0
		criteria.andStartTimeLessThan(new Date());
		criteria.andEndTimeGreaterThan(new Date());
		//批量查询所有缓存数据，增加到Redis缓存中
		List<TbSeckillGoods> goods = seckillGoodsMapper.selectByExample(example);

		System.err.println("秒杀添加到redis中===============");
		//将商品数据加入到缓存中
		for (TbSeckillGoods good : goods) {
			//秒杀商品信息加入缓存
			redisTemplate.boundHashOps(TbSeckillGoods.class.getSimpleName()).put(good.getId(), good);
		}
	}

}



package com.itheima.spring.data.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/8 23:29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class
RedisTest {

	//注入redisTemplate
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * keyValue操作
	 */
	@Test
	public void keyValueAdd(){
		redisTemplate.boundValueOps("username").set("张三");
	}

	@Test
	public void keyValueQuery(){
		System.out.println(redisTemplate.boundValueOps("username").get());
	}

	@Test
	public void delete(){
		redisTemplate.delete("username");
	}

	/**
	 * set操作
	 */
	@Test
	public void setAdd(){
		redisTemplate.boundSetOps("username").add("张三");
		redisTemplate.boundSetOps("username").add("李四");
		redisTemplate.boundSetOps("username").add("王五");
	}

	@Test
	public void setQuery(){
		System.err.println(redisTemplate.boundSetOps("username").members());
	}

	/**
	 * list操作
	 */
	@Test
	public void listAdd(){
		redisTemplate.boundListOps("username1").rightPush("张三");
		redisTemplate.boundListOps("username1").rightPush("李四");
		redisTemplate.boundListOps("username1").rightPush("王五");
	}

	@Test
	public void listQuery(){
		System.out.println(redisTemplate.boundListOps("username1").range(0,5 ));
	}

	/**
	 * hash操作
	 */
	@Test
	public void hashAdd(){
		redisTemplate.boundHashOps("user").put("name","小花" );
		redisTemplate.boundHashOps("user").put("age","18" );
	}

	@Test
	public void hashQuery(){
		System.out.println(redisTemplate.boundHashOps("user").get("name"));
		System.out.println(redisTemplate.boundHashOps("user").get("age"));
		System.out.println(redisTemplate.boundHashOps("user").keys());
		System.out.println(redisTemplate.boundHashOps("user").values());
	}
}

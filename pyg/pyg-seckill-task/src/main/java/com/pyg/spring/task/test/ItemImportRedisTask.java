package com.pyg.spring.task.test;

import com.pyg.spring.task.service.ImportTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/23 20:09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/*.xml")
public class ItemImportRedisTask {

	@Autowired
	private ImportTask importTask;

	@Test
	public void importTask(){
		importTask.startSeckill();

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

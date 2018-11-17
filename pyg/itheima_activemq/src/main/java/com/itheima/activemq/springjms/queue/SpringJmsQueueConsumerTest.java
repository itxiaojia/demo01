package com.itheima.activemq.springjms.queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/15 16:08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/applicationContext-jms-consumer.xml")
public class SpringJmsQueueConsumerTest {

	@Test
	public void message(){
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

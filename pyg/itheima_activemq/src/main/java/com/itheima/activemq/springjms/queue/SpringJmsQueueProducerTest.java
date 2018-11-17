package com.itheima.activemq.springjms.queue;

import com.itheima.activemq.springjms.queue.producer.SpringJmsQueueProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/15 15:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/applicationContext-jms-producer.xml")
public class SpringJmsQueueProducerTest {

	@Autowired
	private SpringJmsQueueProducer springJmsQueueProducer;

	@Test
	public void send(){
		springJmsQueueProducer.send("springjms-测试一");
	}
}

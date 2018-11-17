package com.itheima.activemq.springjms.topic;

import com.itheima.activemq.springjms.queue.producer.SpringJmsQueueProducer;
import com.itheima.activemq.springjms.topic.producer.SpringJmsTopicProducer;
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
@ContextConfiguration(locations = "classpath:/topic/applicationContext-jms-producer.xml")
public class SpringJmsTopicProducerTest {

	@Autowired
	private SpringJmsTopicProducer springJmsTopicProducer;

	@Test
	public void send(){
		springJmsTopicProducer.send("springjms-测试一");
	}
}

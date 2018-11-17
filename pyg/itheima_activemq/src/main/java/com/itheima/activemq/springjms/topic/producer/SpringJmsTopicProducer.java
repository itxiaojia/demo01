package com.itheima.activemq.springjms.topic.producer;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/15 15:20
 */
@Component
public class SpringJmsTopicProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination topicTextDestination;

	/**
	 *负责发送消息
	 * @param message
	 */
	@Test
	public void send(String message){
		//参数一:确定参数类型 参数二:发送消息
		jmsTemplate.send(topicTextDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	}
}

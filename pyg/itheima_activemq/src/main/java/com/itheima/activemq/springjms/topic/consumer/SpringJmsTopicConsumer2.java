package com.itheima.activemq.springjms.topic.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/15 16:02
 */
public class SpringJmsTopicConsumer2 implements MessageListener {

	@Override
	public void onMessage(Message message) {

		try {
			//1,强转成你想要的类型
			TextMessage message1=(TextMessage)message;
			//2,获取消息
			String text = message1.getText();
			//3,打印
			System.err.println("接受到的消息:"+text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

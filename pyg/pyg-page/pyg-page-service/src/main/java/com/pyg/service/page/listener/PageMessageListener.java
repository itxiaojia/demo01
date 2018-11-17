package com.pyg.service.page.listener;

import com.pyg.service.page.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/15 17:22
 */
public class PageMessageListener implements MessageListener {

	@Autowired
	private ItemPageService pageService;

	@Override
	public void onMessage(Message message) {
		try {
			//1,消息强转成object
			ObjectMessage message1 = (ObjectMessage) message;
			Long[] ids = (Long[]) message1.getObject();
			for (Long id : ids) {
				//2,生成静态页面
				pageService.getHtml(id);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

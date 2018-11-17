package com.pyg.service.search.listener;

import com.pyg.service.search.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/15 17:39
 */
public class SearchDeleteListener implements MessageListener {

	@Autowired
	private ItemSearchService searchService;

	@Override
	public void onMessage(Message message) {
		try {
			//1,强转信息类型
			ObjectMessage message1=(ObjectMessage)message;
			Long [] ids = (Long [])message1.getObject();
			//删除索引库
			for (Long id : ids) {
				searchService.deleteSolr(id);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

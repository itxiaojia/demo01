package com.pyg.service.search.listener;

import com.pyg.service.search.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * @Author: Mr.jia
 * @Date: 2018/10/15 16:36
 */
public class SearchMessageListener implements MessageListener {

	@Autowired
	private ItemSearchService searchService;

	@Override
	public void onMessage(Message message) {
		try {
			//1,强转成object
			ObjectMessage message1=(ObjectMessage)message;
			Long[] ids = (Long[])message1.getObject();
			for (Long id : ids) {
				//2,导入索引库
				searchService.importSolr(id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

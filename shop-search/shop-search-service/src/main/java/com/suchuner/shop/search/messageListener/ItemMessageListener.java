package com.suchuner.shop.search.messageListener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.suchuner.shop.search.service.impl.SearchItemService;

public class ItemMessageListener implements MessageListener {
	@Autowired
	private SearchItemService itemService;

	public void onMessage(Message message) {
		/*1.获取消息,取得商品添加的id号
		 * 2.根据id从数据库中查询到商品信息
		 * 3.将商品信息添加到索引库中
		 * */
		try {
			TextMessage textMessage = (TextMessage) message;
			Thread.sleep(1000);
			itemService.updateItemByItemId(Long.parseLong(textMessage.getText()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}

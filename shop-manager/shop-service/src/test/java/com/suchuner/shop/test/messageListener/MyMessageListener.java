package com.suchuner.shop.test.messageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {

	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			System.out.println("收到的消息为>>>>>>>>"+textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

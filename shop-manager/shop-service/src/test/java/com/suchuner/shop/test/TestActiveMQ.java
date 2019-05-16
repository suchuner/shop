package com.suchuner.shop.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class TestActiveMQ {
	@Test
	public void testQueueProducer() throws JMSException {
		/*
		 * 发送者步骤: 
		 * 1.创建ConnectionFactory对象,需要指定服务端ip即端口号
		 * 2.使用ConnectionFactory对象创建一个Connection对象
		 * 3.开启连接,使用connection对象调用start()方法 
		 * 4.使用connection对象创建一个session对象
		 * 5.使用session对象创建一个destination对象(即queue或者topic对象),此处创建一个queue对象
		 * 6.使用session对象创建一个producer对象. 
		 * 7.创建一个message对象,创建一个textmessage对象(可以使用session.createTextMessage来获取,也可以new一个消息对象)
		 * 8.使用producer对象发送消息 
		 * 9关闭资源
		 */
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.2.128:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		// 参数说明:第一个参数表示是否要开启事务,第二个参数表示消息的应答模式:自动应答,手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 参数说明:消息队列的名称,消费者通过该名称获取消息
		Queue queue = session.createQueue("test-queue");
		// 参数说明:为创建的Queue对象
		MessageProducer producer = session.createProducer(queue);
		TextMessage textMessage = session.createTextMessage("完美,测试成功 ");
		producer.send(textMessage);
		producer.close();
		session.close();
		connection.close();
	}
	@Test
	public void testQueueConsumer() throws Exception{
		/*接收者实现步骤:
		 * 1.创建一个connectionfactory对象,
		 * 2.根据connectionfactory对象获取一个connection对象
		 * 3.开启连接,使用connection对象开启连接
		 * 4.使用connection对象创建一个session对象
		 * 5.使用session对象创建一个destination对象,由于该方法为接收者,参数说明:需要从哪个消息队列中取值,(即消息的名称)
		 * 6.使用session对象创建一个consumer对象
		 * 7.接收消息
		 * 8.打印消息
		 * 9.关闭资源
		 * */
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.2.128:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		// 参数说明:第一个参数表示是否要开启事务,第二个参数表示消息的应答模式:自动应答,手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//参数说明:表示需要从哪个消息队列中去消息[消息队列的名称]
		Queue queue = session.createQueue("test-queue");
		MessageConsumer consumer = session.createConsumer(queue);
		//接收消息:即设置消息监听器,有消息进入此匿名内部类
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println("接收到的消息为>>>>>>>"+text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}
	@Test
	public void testTopicProducer() throws Exception{
		/*发送者步骤:
		 * 1.创建一个connectionfactory对象
		 * 2.根据connectionfactory对象获取connection对象
		 * 3.开启连接,使用connection对象中的start()方法
		 * 4.使用connection对象创建一个session对象
		 * 5.使用session对象创建一个destination对象
		 * 6.使用session对象创建一个producer对象
		 * 7.创建一个textmessage对象,可以使用session创建,也可以new一个对象出来
		 * 8.使用producer对象发送消息
		 * 9.关闭资源
		 * */
		ConnectionFactory connectionFactory = new  ActiveMQConnectionFactory("tcp://192.168.2.128:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("test-topic");
		MessageProducer producer = session.createProducer(topic);
		TextMessage textMessage = session.createTextMessage("测试~~~~~~~~~");
		producer.send(textMessage);
		producer.close();
		session.close();
		connection.close();
	}
	@Test
	public void testTopicConsumer() throws Exception{
		/*接收者实现步骤:
		 * 1.创建一个connectionfactory对象,
		 * 2.根据connectionfactory对象获取一个connection对象
		 * 3.开启连接,使用connection对象开启连接
		 * 4.使用connection对象创建一个session对象
		 * 5.使用session对象创建一个destination对象,由于该方法为接收者,参数说明:需要从哪个消息队列中取值,(即消息的名称)
		 * 6.使用session对象创建一个consumer对象
		 * 7.接收消息
		 * 8.打印消息
		 * 9.关闭资源
		 * */
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.2.128:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		// 参数说明:第一个参数表示是否要开启事务,第二个参数表示消息的应答模式:自动应答,手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//参数说明:表示需要从哪个消息队列中去消息[消息队列的名称]
		Topic topic = session.createTopic("test-topic");
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息:即设置消息监听器,有消息进入此匿名内部类
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println("接收到的消息为>>>>>>>"+text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}
	
}

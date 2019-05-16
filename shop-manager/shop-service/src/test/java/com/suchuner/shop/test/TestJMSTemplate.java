package com.suchuner.shop.test;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestJMSTemplate {
	ApplicationContext applicationContext;
	@Before
	public void init(){
		applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
	}
	@Test
	public void testJMSProducer() throws Exception{
		/*实现步骤:首先需要导入相应的整合jar,然后添加相应的配置文件,最后代码实现
		 * 1.在配置文件中配置ActiveMQConnectionFactory对象,targetConnectionFactory
		 * 2.配置spring的SingleConnectionFactory对象,并且将ActiveMQConnectionFactory对象注入connectionFactory
		 * 3.配置用于发送消息的对象JmsTemplate,并将spring的连接工厂注入
		 * 4.配置Destination对象,有ActiveMQQueue和ActiveTopic之分.需要配置构造器的name属性,为自定义的一个消息队列的名称
		 * 5.配置自定义的消息监听器,和系统的消息监听器
		 * 6.系统监听器需要注入的属性有:connectionFactory,destination,和messageListener
		 * 
		 * 代码实现:
		 * 1.获取JmsTemplate对象
		 * 2.获取destination对象
		 * 3.使用JmsTemplate对象发送消息,参数说明:代码部分体现
		 * 4.线程sleep:为了保证该事件必须完成之后才发送消息,以免造成空指针异常
		 * */
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		Destination destination = (Destination) applicationContext.getBean("topicDestination");
		//参数说明:第一个参数为目标对象即queue或者topic,第二个参数为消息创建对象,创建匿名内部类即可
		jmsTemplate.send(destination, new MessageCreator() {
			//参数说明:该session对象用于创建消息对象
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("张钰,你是最美的~~~~~~~~");
			}
		});
			Thread.sleep(1000);
		}
}

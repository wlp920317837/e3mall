package cn.e3mall.manager.test;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestActiveMqSpring {

	@Test
	public void sendMessage() {
		// 初始化一个spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		// 从spring容器中获得jmsTemplate对象,生产者
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		// 从sprign容器中获得Destination对象,发送方式两种
		Destination destination = context.getBean(ActiveMQQueue.class);
		jmsTemplate.send(destination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("测试消息1");
			}
		});
	}
	
}

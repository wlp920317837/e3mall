package cn.e3mall.manager.test;

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

public class TestActiveMq {

	@Test
	public void TestQueueProducer() throws Exception {
		// 1.创建一个连接工厂对象,ActiveMq连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.217.129:61616");
		// 2.使用连接工厂获得一个连接对象connection
		Connection connection = factory.createConnection();
		// 3.开启连接,connection.start()
		connection.start();
		// 4.使用连接对象connection获得一个session,两个参数
		// 第一个参数:是否开启事务.一般不开启
		// 第二次参数:消息应答模式.自动应答,手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5.使用session获得一个destination对象,有两种方式queue和topic
		Queue queue = session.createQueue("testQueue");// 参数为queue的名字
		// 6.使用session在创建一个producer对象,消息发送者
		MessageProducer producer = session.createProducer(queue);
		// 7.使用session创建一个消息message对象,textMessage
		TextMessage message = session.createTextMessage("hello wlp");
		// 8.发送消息
		producer.send(message);
		// 9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}

	@Test
	public void TestQueueConsumer() throws Exception {
		// 1.创建一个连接工厂对象,ActiveMq连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.217.129:61616");
		// 2.使用连接工厂获得一个连接对象connection
		Connection connection = factory.createConnection();
		// 3.开启连接,connection.start()
		connection.start();
		// 4.使用连接对象connection获得一个session,两个参数
		// 第一个参数:是否开启事务.一般不开启
		// 第二次参数:消息应答模式.自动应答,手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5.使用session获得一个destination对象,有两种方式queue和topic
		Queue queue = session.createQueue("test-queue");// 参数为queue的名字
		// 6.使用session创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(queue);
		// 7.接受消息
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				String text = null;
				try {
					// 8.打印结果
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		// 8.等待接受消息
		System.in.read();

		// 9.关闭资源
		consumer.close();
		session.close();
		connection.close();
	}

	@Test
	public void TestTopicProducer() throws Exception {
		// 1.创建一个连接工厂对象,ActiveMq连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.217.129:61616");
		// 2.使用连接工厂获得一个连接对象connection
		Connection connection = factory.createConnection();
		// 3.开启连接,connection.start()
		connection.start();
		// 4.使用连接对象connection获得一个session,两个参数
		// 第一个参数:是否开启事务.一般不开启
		// 第二次参数:消息应答模式.自动应答,手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5.使用session获得一个destination对象,有两种方式queue和topic
		Topic topic = session.createTopic("mytopic");// 参数为topic的名字
		// 6.使用session在创建一个producer对象,消息发送者
		MessageProducer producer = session.createProducer(topic);
		// 7.使用session创建一个消息message对象,textMessage
		TextMessage message = session.createTextMessage("hello wlp");
		// 8.发送消息
		producer.send(message);
		// 9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}

	@Test
	public void TestTopicConsumer() throws Exception {
		// 1.创建一个连接工厂对象,ActiveMq连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.217.129:61616");
		// 2.使用连接工厂获得一个连接对象connection
		Connection connection = factory.createConnection();
		// 3.开启连接,connection.start()
		connection.start();
		// 4.使用连接对象connection获得一个session,两个参数
		// 第一个参数:是否开启事务.一般不开启
		// 第二次参数:消息应答模式.自动应答,手动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 5.使用session获得一个destination对象,有两种方式queue和topic
		Topic topic = session.createTopic("mytopic");// 参数为topic的名字
		// 6.使用session创建一个消费者对象
		MessageConsumer consumer = session.createConsumer(topic);
		// 7.接受消息
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				String text = null;
				try {
					// 8.打印结果
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		System.out.println("topic消费者1启动");
		// 8.等待接受消息
		System.in.read();

		// 9.关闭资源
		consumer.close();
		session.close();
		connection.close();
	}

}

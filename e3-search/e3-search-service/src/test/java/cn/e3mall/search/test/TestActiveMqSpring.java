package cn.e3mall.search.test;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestActiveMqSpring {

	@Test
	public void receiveMessage() throws IOException {
		// 初始化一个spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		// 等待消息接收
		System.in.read();
	}

}

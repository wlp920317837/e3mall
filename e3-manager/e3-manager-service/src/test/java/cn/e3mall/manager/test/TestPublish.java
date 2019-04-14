package cn.e3mall.manager.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPublish {

	@Test
	public void manager() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("manager服务启动了");
		System.in.read();
		System.out.println("manager服务结束了");
	}
}

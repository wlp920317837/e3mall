package cn.e3mall.sso.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPublish {

	@Test
	public void sso() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("sso服务启动了");
		System.in.read();
		System.out.println("sso服务结束了");
	}
}

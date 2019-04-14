package com.e3mall.content.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPublish {

	@Test
	public void content() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("content服务启动了");
		System.in.read();
		System.out.println("content服务结束了");
	}
}

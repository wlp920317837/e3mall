package com.e3mall.content.test;

import cn.e3mall.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试jedisClient
 * 1.jedisClientPool 单机版
 * 2.jedisClientCluster 集群版
 * @author wlp
 *
 */

public class JedisClientTest {

	@Test
	public void testJedisClient() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		JedisClient jedisClient = (JedisClient) context.getBean(JedisClient.class);
		String string = jedisClient.get("test");
		System.out.println(string);
	}
}

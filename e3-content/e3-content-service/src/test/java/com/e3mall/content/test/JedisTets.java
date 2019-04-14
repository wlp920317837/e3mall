package com.e3mall.content.test;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * Jedis的测试
 * @author wlp
 *
 */
public class JedisTets {

	@Test
	public void testJedis() {
		// 创建一个jedis连接对象,构造参数host,port
		Jedis jedis = new Jedis("192.168.217.129", 6379);
		// 直接使用jedis操作redis
		jedis.set("myjiedis", "test1");
		String string = jedis.get("myjiedis");
		System.out.println(string);
		// 关闭资源
		jedis.close();
	}

	@Test
	public void testJedisPool() {
		// 创建连接池对象,构造参数host,port
		JedisPool jedisPool = new JedisPool("192.168.217.129", 6379);
		// 连接池获得一个连接jedis
		Jedis jedis = jedisPool.getResource();
		// 使用Jedis操作完redis
		String string = jedis.get("myjiedis");
		System.out.println(string);
		// 每次使用完毕后关闭连接,连接池回收资源
		jedis.close();
		// 关闭连接池
		jedisPool.close();
	}

	@Test
	public void testJedisCluster() {
		// 集群的节点
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.217.129", 7001));
		nodes.add(new HostAndPort("192.168.217.129", 7002));
		nodes.add(new HostAndPort("192.168.217.129", 7003));
		nodes.add(new HostAndPort("192.168.217.129", 7004));
		nodes.add(new HostAndPort("192.168.217.129", 7005));
		nodes.add(new HostAndPort("192.168.217.129", 7006));

		// 创建一个JedisCluster对象,构造参数set<HostAndPort>
		JedisCluster jedisCluster = new JedisCluster(nodes);

		// 直接使用jedisCluster操作redis
		jedisCluster.set("test", "myttt");
		String string = jedisCluster.get("test");
		System.out.println(string);
		// 关闭jedisCluster
		jedisCluster.close();
	}
}

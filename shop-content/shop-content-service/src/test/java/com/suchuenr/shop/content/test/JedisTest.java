package com.suchuenr.shop.content.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	@Test
	public void testJedis(){
		/*需求:建立jedis的缓存机制(单机版)
		 * 步骤:
		 * 1.建立jedis的服务端的联系
		 * 2.存值
		 * 3.取值
		 * 4.关闭连接
		 * */
		Jedis jedis = new Jedis("192.168.25.128",6379);
		jedis.set("suchuner", "suchuenr beauty");
		System.out.println("hello>>>>>>"+jedis.get("suchuner"));
		jedis.close();
	}
	@Test
	public void testJedisPool(){
		/*需求:建立jedis的连接池访问
		 * 步骤:1.建立jedis的连接池对象
		 * 2.通过连接池对象得到jedis客户端对象
		 * 3.存值
		 * 4.取值
		 * 5.取消客户端资源占用,关闭对象
		 * 6.关闭连接池对象
		 * */
		JedisPool jedisPool = new JedisPool("192.168.25.128",6379);
		Jedis jedis = jedisPool.getResource();
		jedis.set("suchuner", "suchuenr beauty best");
		System.out.println("hello>>>>>>"+jedis.get("suchuner"));
		jedis.close();
		jedisPool.close();
	}
	@Test
	public void testJedisCluster() throws IOException{
		/*需求:测试jedis的集群的访问
		 * 步骤:1.创建jedis集群的对象,并且将集群的计算机的ip和端口设置进去
		 * 2.存值
		 * 3.取值
		 * 4.关闭资源
		 * */
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		jedisCluster.set("suchuner", "张钰");
		System.out.println("hello>>>>>>>>"+jedisCluster.get("suchuner"));
		jedisCluster.close();
	}
}

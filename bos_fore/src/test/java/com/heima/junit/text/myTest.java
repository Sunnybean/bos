package com.heima.junit.text;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

@ContextConfiguration(locations="classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class myTest {
	@Autowired
	private RedisTemplate<String,String> rdis;
	
	@Test
	public void m (){
		
		rdis.opsForValue().set("city","北京",30,TimeUnit.DAYS);
		System.out.println(rdis.opsForValue().get("city"));
	}
	
/*	@Test
	public void my(){
		Jedis dis  = new Jedis(localhost);
		
		return null;
		
	}*/
	
}

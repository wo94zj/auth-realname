package com.auth.realname.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.auth.realname.redis.config.RedisConfig;
import com.auth.realname.util.RandomUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisLockServiceTest {

	@Autowired
	private RedisLockService redisLockService;
	
	@Test
	public void lockTest() {
		String key = "test1";
		String value = RandomUtil.randomUINT4();
		
		System.out.println(value + ":" + RedisConfig.LOCK_TIMEOUT_SECONDS + ":" + RedisConfig.TRY_LOCK_WAIT_SECONDS);
		System.out.println(redisLockService.lock(key, value, RedisConfig.LOCK_TIMEOUT_SECONDS));
		System.out.println(redisLockService.tryLock(key, value, RedisConfig.LOCK_TIMEOUT_SECONDS, RedisConfig.TRY_LOCK_WAIT_SECONDS - 10));
		System.out.println(redisLockService.unLock(key, value));
	}
}

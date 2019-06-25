package com.auth.realname.redis;

import java.time.Duration;
import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
public class RedisLockService {

	@Resource
    private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private DefaultRedisScript<Boolean> delKeySameLua;
	
	/**
	 * lock存活时间<br>
	 * 加锁的时候指定value是为了防止被其他程序解锁<br>
	 * 
	 * @param timeout 单位秒
	 */
	public boolean lock(String key, String value, long timeout) {
		return stringRedisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(timeout));
	}
	
	/**
	 * 解锁（key和value都要对应才能解锁）
	 */
	public Boolean unLock(String key, String value) {
		return stringRedisTemplate.execute(delKeySameLua, Collections.singletonList(key), value);
	}
	
	/**
	 * 尝试获取锁
	 * @param timeout 锁超时时间，单位秒
	 * @param seconds 获取锁最大等待时间，单位秒
	 */
	public boolean tryLock(String key, String value, long timeout, long seconds) {
		ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        Boolean result = false;
        
        while(!(result = opsForValue.setIfAbsent(key, value, Duration.ofSeconds(timeout)))) {
        	seconds = seconds - 2;
        	if(seconds < 0) {
        		break;
        	}
        	
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        return result;
	}
	
}

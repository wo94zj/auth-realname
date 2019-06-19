package com.auth.realname.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisLockService {

	@Resource
    private RedisTemplate<String, Integer> redisTemplate;
	
	public boolean lock(String key, long timeout, TimeUnit unit) {
        ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();
        Boolean result = opsForValue.setIfAbsent(key, 1, timeout, unit);
        
        return result;
    }
	
	public boolean tryLock(String key, long timeout, TimeUnit unit, long seconds) {
		ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();
        Boolean result = false;
        
        while(!(result = opsForValue.setIfAbsent(key, 1, timeout, unit))) {
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
	
	public boolean unlock(String key) {
		return redisTemplate.delete(key);
	}
}

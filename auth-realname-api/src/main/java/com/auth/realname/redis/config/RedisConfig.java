package com.auth.realname.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
//通过Spring Cloud Config加载配置，去掉PropertySource注解
//@PropertySource("classpath:redis.properties")
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {
	
	//锁超时时间
	public static int LOCK_TIMEOUT_SECONDS;
	//获取锁最大等待时间
	public static int TRY_LOCK_WAIT_SECONDS;

	public void setLockTimeoutSeconds(int lockTimeoutSeconds) {
		LOCK_TIMEOUT_SECONDS = lockTimeoutSeconds;
		log.info("LOCK_TIMEOUT_SECONDS :{}", LOCK_TIMEOUT_SECONDS);
	}
	public void setTryLockWaitSeconds(int tryLockWaitSeconds) {
		TRY_LOCK_WAIT_SECONDS = tryLockWaitSeconds;
		log.info("TRY_LOCK_WAIT_SECONDS :{}", TRY_LOCK_WAIT_SECONDS);
	}
	
	@Bean("delKeySameLua")
	public DefaultRedisScript<Boolean> delKeySameLua() {
		DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
		script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/del_key_same_kv.lua")));
		script.setResultType(Boolean.class);
		
		return script;
	}
}

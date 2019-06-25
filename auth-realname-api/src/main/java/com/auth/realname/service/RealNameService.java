package com.auth.realname.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.bean.Resident;
import com.auth.realname.bean.RealName;
import com.auth.realname.enums.AuthStatusEnums;
import com.auth.realname.exception.SystemBusyException;
import com.auth.realname.mapper.RealNameMapper;
import com.auth.realname.redis.RedisLockService;
import com.auth.realname.redis.config.RedisConfig;
import com.auth.realname.util.RandomUtil;
import com.auth.service.IDcardAuthService;
import com.auth.util.IDcardUtil;
import com.auth.util.RegExUtil;

@Service
public class RealNameService {

	@Autowired
	private RealNameMapper realnameMapper;

	@Autowired
	private RedisLockService redisLockService;

	@Autowired
	private IDcardAuthService idcardAuthService;

	public boolean checkIDcardAndName(RealName realname) {
		Objects.requireNonNull(realname, "RealName must not be null");
		Objects.requireNonNull(realname.getIdcard(), "idcard must not be null");
		Objects.requireNonNull(realname.getName(), "name must not be null");

		if ((!IDcardUtil.checkIDcard(realname.getIdcard())) || (!RegExUtil.matchHanzi(realname.getName()))) {
			return false; // 格式不正确
		}

		int insertResult = realnameMapper.insertRealName(realname);
		if (insertResult == 0) {// 库中存在记录
			RealName realnameDB = realnameMapper.selectRealNameByIDcard(realname.getIdcard());
			if (Objects.nonNull(realnameDB)) {
				if (realname.getName().equals(realnameDB.getName())) {
					if (AuthStatusEnums.PASS.getStatus().equals(realnameDB.getStatus())) {
						// 库中记录认证成功，返回结果
						return true;
					} else if (AuthStatusEnums.UNPASS.getStatus().equals(realnameDB.getStatus())) {
						// 库中记录认证失败，直接返回
						return false;
					}
				} else {
					if (AuthStatusEnums.PASS.getStatus().equals(realnameDB.getStatus())) {
						// 库中记录认证成功，但是name不符合认证结果
						return false;
					}
				}
			}
		}

		realname.setStatus(AuthStatusEnums.UNPASS.getStatus());
		Resident resident = idcardAuthService.realnameAuth(realname.getIdcard(), realname.getName());
		if (Objects.nonNull(resident)) {
			realname.setStatus(AuthStatusEnums.PASS.getStatus());
			realname.setSex(resident.getSex());
			realname.setBirthday(resident.getBirthday());
			realname.setArea(resident.getArea());
			realnameMapper.updateRealNameByIDcard(realname);

			return true;
		}

		realnameMapper.updateStatusByIDcard(realname);
		return false;
	}

	/**
	 * idcard重复的串行处理（避免重复请求，导致多扣费）
	 */
	public boolean checkIDcardAndNameWithLock(RealName realname) {
		Objects.requireNonNull(realname, "RealName must not be null");
		Objects.requireNonNull(realname.getIdcard(), "idcard must not be null");
		Objects.requireNonNull(realname.getName(), "name must not be null");

		if ((!IDcardUtil.checkIDcard(realname.getIdcard())) || (!RegExUtil.matchHanzi(realname.getName()))) {
			return false; // 格式不正确
		}

		int insertResult = realnameMapper.insertRealName(realname);
		if (insertResult == 0) {// 库中存在记录
			RealName realnameDB = realnameMapper.selectRealNameByIDcard(realname.getIdcard());
			if (Objects.nonNull(realnameDB)) {
				if (realname.getName().equals(realnameDB.getName())) {
					if (AuthStatusEnums.PASS.getStatus().equals(realnameDB.getStatus())) {
						// 库中记录认证成功，返回结果
						return true;
					} else if (AuthStatusEnums.UNPASS.getStatus().equals(realnameDB.getStatus())) {
						// 库中记录认证失败，直接返回
						return false;
					}
				} else {
					if (AuthStatusEnums.PASS.getStatus().equals(realnameDB.getStatus())) {
						// 库中记录认证成功，但是name不符合认证结果
						return false;
					}
				}
			}
		}

		String randomValue = RandomUtil.randomUINT4();
		if (redisLockService.lock(realname.getIdcard(), randomValue, RedisConfig.LOCK_TIMEOUT_SECONDS)) {
			realname.setStatus(AuthStatusEnums.UNPASS.getStatus());
			Resident resident = idcardAuthService.realnameAuth(realname.getIdcard(), realname.getName());
			if (Objects.nonNull(resident)) {
				realname.setStatus(AuthStatusEnums.PASS.getStatus());
				realname.setSex(resident.getSex());
				realname.setBirthday(resident.getBirthday());
				realname.setArea(resident.getArea());
				realnameMapper.updateRealNameByIDcard(realname);

				redisLockService.unLock(realname.getIdcard(), randomValue);
				return true;
			}

			realnameMapper.updateStatusByIDcard(realname);
		} else {
			// 如果需要争用锁，说明这个idcard程序已经在处理；所以在获取锁之后立马释放锁并进行是否已经获取到认证信息校验
			boolean lock = redisLockService.tryLock(realname.getIdcard(), randomValue, RedisConfig.LOCK_TIMEOUT_SECONDS, RedisConfig.TRY_LOCK_WAIT_SECONDS);
			if (lock) {
				redisLockService.unLock(realname.getIdcard(), randomValue);
				return checkIDcardAndName(realname);
			}

			throw new SystemBusyException("can get lock to auth realname");
		}

		redisLockService.unLock(realname.getIdcard(), randomValue);
		return false;
	}
}

package com.auth.realname.service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.bean.Resident;
import com.auth.realname.bean.RealName;
import com.auth.realname.enums.AuthStatusEnums;
import com.auth.realname.mapper.RealNameMapper;
import com.auth.realname.redis.RedisLockService;
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

		if (redisLockService.lock(realname.getIdcard(), 30, TimeUnit.SECONDS)) {
			realname.setStatus(AuthStatusEnums.UNPASS.getStatus());
			Resident resident = idcardAuthService.realnameAuth(realname.getIdcard(), realname.getName());
			if (Objects.nonNull(resident)) {
				realname.setStatus(AuthStatusEnums.PASS.getStatus());
				realname.setSex(resident.getSex());
				realname.setBirthday(resident.getBirthday());
				realname.setArea(resident.getArea());
				realnameMapper.updateRealNameByIDcard(realname);

				redisLockService.unlock(realname.getIdcard());
				return true;
			}

			realnameMapper.updateStatusByIDcard(realname);
		} else {
			// 如果需要争用锁，说明这个idcard程序已经在处理；所以在获取锁之后立马释放锁并进行是否已经获取到认证信息校验
			int count = 0;
			boolean lock = false;
			while (!(lock = redisLockService.tryLock(realname.getIdcard(), 30, TimeUnit.SECONDS, 5))) {
				count++;
				if (count > 6) {
					break;
				}
			}
			if (lock) {
				redisLockService.unlock(realname.getIdcard());
				return checkIDcardAndName(realname);
			}

			throw new RuntimeException("can get lock to auth realname");
		}

		redisLockService.unlock(realname.getIdcard());
		return false;
	}
}

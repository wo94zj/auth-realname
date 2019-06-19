package com.auth.service;

import com.auth.bean.Resident;

public interface IDcardAuthService {

	/**
	 * 返回居民信息，即为认证成功；null表示认证失败
	 */
	Resident realnameAuth(String idcard, String name);
}

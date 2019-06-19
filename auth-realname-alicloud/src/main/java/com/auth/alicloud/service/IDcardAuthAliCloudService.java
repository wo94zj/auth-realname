package com.auth.alicloud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth.alicloud.common.RequestUtil;
import com.auth.bean.Resident;
import com.auth.service.IDcardAuthService;

public class IDcardAuthAliCloudService implements IDcardAuthService {

	private Logger log = LoggerFactory.getLogger(IDcardAuthAliCloudService.class);
	
	@Override
	public Resident realnameAuth(String idcard, String name) {
		log.info("realname auth idcard :{}, name :{}", idcard, name);
		return RequestUtil.request(idcard, name);
	}
	
}

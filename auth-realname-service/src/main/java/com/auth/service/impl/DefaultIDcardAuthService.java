package com.auth.service.impl;

import java.util.Objects;

import com.auth.bean.Resident;
import com.auth.service.IDcardAuthService;
import com.auth.util.IDcardUtil;
import com.auth.util.RegExUtil;

public class DefaultIDcardAuthService implements IDcardAuthService {

	public Resident realnameAuth(String idcard, String name) {
		if(Objects.isNull(idcard) || Objects.isNull(name)) {
			return null;
		}
		
		if(IDcardUtil.checkIDcard(idcard) && RegExUtil.matchHanzi(name)) {
			Resident resident = new Resident();
			resident.setIdcard(idcard);
			resident.setName(name);
			
			return resident;
		}
		
		return null;
	}

}

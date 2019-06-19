package com.auth.realname.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.realname.bean.RealName;
import com.auth.realname.enums.AuthStatusEnums;
import com.auth.realname.resp.BaseDto;
import com.auth.realname.resp.ResultUtil;
import com.auth.realname.service.RealNameService;
import com.auth.realname.util.TimeUtil;

@RestController
@RequestMapping("/realname")
public class RealNameController {

	@Autowired
	private RealNameService realnameService;

	@RequestMapping(value = "/idcardname", method = RequestMethod.POST)
	public BaseDto<Serializable> checkIDcardAndName(@RequestParam String idcard, @RequestParam String name) {
		long time = TimeUtil.currentMilli();
		RealName realname = new RealName();
		realname.setIdcard(idcard);
		realname.setName(name);
		realname.setStatus(AuthStatusEnums.UNCHECK.getStatus());
		realname.setUpdateTime(time);
		realname.setCreateTime(time);
		
		if (realnameService.checkIDcardAndNameWithLock(realname)) {
			return ResultUtil.success();
		}

		return ResultUtil.failed();
	}
}

package com.auth.realname.enums;

public enum AuthStatusEnums {

	UNCHECK(0),PASS(1),UNPASS(2);
	
	private Integer status;
	
	private AuthStatusEnums(int status) {
		this.status = status;
	}
	
	public Integer getStatus() {
		return status;
	}
}

package com.auth.realname.exception;

import com.auth.realname.resp.ResultCode;


public class SystemBusyException extends RuntimeException {

	private static final long serialVersionUID = -9028168794954036138L;

	private Integer code;
	private String msg;
	
	public SystemBusyException() {
		this.code = ResultCode.BUSY.getCode();
		this.msg = ResultCode.BUSY.getMsg();
	}
	
	public SystemBusyException(String msg) {
		this.code = ResultCode.BUSY.getCode();
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
}

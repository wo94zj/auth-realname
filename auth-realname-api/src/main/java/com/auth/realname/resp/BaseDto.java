package com.auth.realname.resp;

import lombok.Data;

@Data
public class BaseDto<T> {

	private int code;
	private String msg;
	private T data;
	
	public void setResult(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public void setResult(ResultCode result, String msg, T data) {
		setResult(result.getCode(), msg, data);
	}
	
	public void setResult(ResultCode result, T data) {
		setResult(result.getCode(), result.getMsg(), data);
	}
}

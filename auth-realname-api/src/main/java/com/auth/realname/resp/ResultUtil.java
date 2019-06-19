package com.auth.realname.resp;

import java.io.Serializable;

public class ResultUtil {

	public static BaseDto<Serializable> success() {
		return result(ResultCode.OK.getCode(), ResultCode.OK.getMsg(), "");
	}
	
	public static BaseDto<Serializable> failed() {
		return result(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMsg(), "");
	}
	
	public static <T> BaseDto<T> success(T data) {
		return result(ResultCode.OK.getCode(), ResultCode.OK.getMsg(), data);
	}
	
	public static BaseDto<Serializable> result(ResultCode code) {
		return result(code.getCode(), code.getMsg(), "");
	}
	
	public static <T> BaseDto<T> result(ResultCode code, T data) {
		return result(code.getCode(), code.getMsg(), data);
	}
	
	public static BaseDto<Serializable> result(int code, String msg) {
		return result(code, msg, "");
	}
	
	public static <T> BaseDto<T> result(int code, String msg, T data) {
		BaseDto<T> result = new BaseDto<T>();
		result.setResult(code, msg, data);
		return result;
	}
}

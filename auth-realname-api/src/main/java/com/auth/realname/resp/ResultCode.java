package com.auth.realname.resp;

public enum ResultCode {

	OK(2000, "OK"),
	FAILED(2001, "Failed"),
	BUSY(2002, "Server busy, please try again later.."),
	
	BAD_REQUEST(4000, "请求信息校验失败"),
	METHOD_ERROR(4050, "请求方法不支持"),
	URL_ERROR(4040, "未找到API"),
	MEDIA_TYPE_ERROR(4150, "MediaType ERROR"),
	
	ERROR(5000, "ERROR");
    
	
	private Integer code;
	private String msg;
	
	private ResultCode(int code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}

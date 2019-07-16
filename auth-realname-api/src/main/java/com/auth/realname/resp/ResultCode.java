package com.auth.realname.resp;

public enum ResultCode {

OK(2000, ResultMsgConstant.OK),
	
	FAILED(2001, ResultMsgConstant.FAILTED),
	BUSY(2002, ResultMsgConstant.BUSY),
	
	VISIT_LIMIT(3001, ResultMsgConstant.VISIT_LIMIT),
	IDEMPOTENT_LIMIT(3002, ResultMsgConstant.IDEMPOTENT_LIMIT),
	
	BAD_REQUEST(4000, ResultMsgConstant.BAD_REQUEST),
	NOT_LOGIN(4010, ResultMsgConstant.NOT_LOGIN),
	NOT_AUTH(4030, ResultMsgConstant.NOT_AUTH),
	URL_ERROR(4040, ResultMsgConstant.URL_ERROR),
	METHOD_ERROR(4050, ResultMsgConstant.METHOD_ERROR),
	MEDIA_TYPE_ERROR(4150, ResultMsgConstant.MEDIA_TYPE_ERROR),
	
	SYSTEM_ERROR(5000, ResultMsgConstant.SYSTEM_ERROR);
    
	
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

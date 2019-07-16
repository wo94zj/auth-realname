package com.auth.realname.resp;

public class ResultMsgConstant {

	private final static String PREFIX = "{result.";
	private final static String SUFFIX = "}";
	
	public final static String OK = PREFIX + "ok" + SUFFIX;
	public final static String FAILTED = PREFIX + "failed" + SUFFIX;
	public final static String BUSY = PREFIX + "busy" + SUFFIX;
	
	public final static String VISIT_LIMIT = PREFIX + "visit.limit" + SUFFIX;
	public final static String IDEMPOTENT_LIMIT = PREFIX + "idempotent.limit" + SUFFIX;
	
	public final static String BAD_REQUEST = PREFIX + "badrequest" + SUFFIX;
	public final static String NOT_LOGIN = PREFIX + "unauthorized" + SUFFIX;
	public final static String NOT_AUTH = PREFIX + "forbidden" + SUFFIX;
	public final static String METHOD_ERROR = PREFIX + "notmethod" + SUFFIX;
	public final static String URL_ERROR = PREFIX + "notfound" + SUFFIX;
	public final static String MEDIA_TYPE_ERROR = PREFIX + "media.error" + SUFFIX;
	
	public final static String SYSTEM_ERROR = PREFIX + "system.error" + SUFFIX;
}

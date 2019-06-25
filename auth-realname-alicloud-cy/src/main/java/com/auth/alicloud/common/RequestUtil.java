package com.auth.alicloud.common;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth.bean.Resident;
import com.auth.util.HttpUtil;
import com.auth.util.MediaTypeUtil;

public class RequestUtil {

	private final static Logger log = LoggerFactory.getLogger(RequestUtil.class);
	
	private final static String ALICLOUD_AUTH_URL = "http://vipidcardcheck.haoservice.com/idcard/VerifyIdcardv2";
	private final static String APPCODE = "appcode";
	
	public static Resident request(String idcard, String name) {
		Map<String, String> querys = new HashMap<>();
		querys.put("cardNo", idcard);
		querys.put("realName", name);
		
		String response = HttpUtil.get(withURL(querys), header());
		log.info("request idcard :{}, name :{}, result :{}", idcard, name, response);
		if(Objects.isNull(response)) {
			return null;
		}
		
		JSONObject json = JSON.parseObject(response);
		if(json.getIntValue("error_code") == 0/* && "Success".equals(json.getString("reason"))*/) {
			JSONObject resultObject = json.getJSONObject("result");
			if(resultObject.getBoolean("isok")) {
				JSONObject idCardInfor = resultObject.getJSONObject("IdCardInfor");
				Resident resident = new Resident();
				resident.setIdcard(idcard);
				resident.setName(name);
				resident.setSex("男".equals(idCardInfor.getString("sex"))?1:0);
				resident.setBirthday(idCardInfor.getString("birthday"));
				resident.setArea(idCardInfor.getString("area"));
				
				return resident;
			}
			
		}
		return null;
	}

	private static String withURL(Map<String, String> querys) {
		String queryForm = MediaTypeUtil.serializeForm(querys, Charset.forName("UTF-8"));
		try {
			return URI.create(ALICLOUD_AUTH_URL + "?" + queryForm).toURL().toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ALICLOUD_AUTH_URL + "?" + queryForm;
	}
	
	private static Map<String, String> header() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "APPCODE " + APPCODE);

		return headers;
	}
	
}

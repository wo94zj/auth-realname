package com.auth.util;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

	static OkHttpClient client = new OkHttpClient();
	
	private final static Logger log = LoggerFactory.getLogger(HttpUtil.class);

	public static String get(String url) {
		Request request = new Request.Builder().url(url).build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		} catch (IOException e) {
			log.error("get {} ERROR :{}", url, e);
		}

		return null;
	}
	
	public static String get(String url, Map<String, String> headers) {
		Request request = new Request.Builder().headers(Headers.of(headers)).url(url).build();
		
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		} catch (IOException e) {
			log.error("get {} ERROR :{}", url, e);
		}

		return null;
	}
}

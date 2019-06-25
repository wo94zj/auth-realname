package com.auth.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MediaTypeUtil {

	public static String serializeForm(Map<String, ?> formData, Charset charset) {
	    if(Objects.isNull(formData) || formData.size() == 0) {
	        return new String();
	    }
	    
		StringBuilder builder = new StringBuilder();
		formData.forEach((name, value) -> {
			try {
				builder.append(URLEncoder.encode(name, charset.name())).append("=")
						.append(URLEncoder.encode(String.valueOf(value), charset.name())).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		});
		
		return builder.deleteCharAt(builder.length()-1).toString();
		//return builder.substring(0, builder.length()-1).toString();
	}
	
	/**
     * form表单格式数据(k1=v1&k2=v2..)转Map<String, String>
     */
    public static Map<String, String> mediaFormToMap(String str) {
        Map<String, String> params = new HashMap<>();
        if(Objects.isNull(str)) {
            return params;
        }
        
        if(str.indexOf("?") > 0) {
            String[] split = str.split("?");
            str = split[1];
        }
        
        for (String kv : str.split("&")) {
            if(kv.indexOf("=")  >= 1) {
                String[] split = kv.split("=");
                String key = split[0];
                String value = split[1];
                
                if(Objects.nonNull(key) && Objects.nonNull(value)) {
                    params.put(key, value);
                }
            }
        }
        
        return params;
    }
	
	public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        /*map.put("1", "1");
        map.put("2", "2");*/
        
        System.out.println(MediaTypeUtil.serializeForm(map, Charset.forName("UTF-8")));
        System.out.println(MediaTypeUtil.mediaFormToMap("account=zhaoj&token=e00ae5f75f72fbb915247e4c69faacca"));
    }

}

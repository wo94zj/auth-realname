package com.auth.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtil {

	/**
	 * 全匹配
	 */
	public static boolean isMatch(String str, Pattern pattern) {
		Matcher m = pattern.matcher(str);
		return m.matches();
	}

	public static boolean existMatch(String str, Pattern pattern) {
		Matcher m = pattern.matcher(str);
		return m.find();
	}

	/**
	 * 全汉字校验
	 */
	public static boolean matchHanzi(String str) {
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
		char c[] = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			Matcher matcher = pattern.matcher(Character.toString(c[i]));
			if (!matcher.matches()) {
				return false;
			}
		}

		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(RegExUtil.matchHanzi(" "));
	}
}

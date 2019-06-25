package com.auth.realname.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

public class RandomUtil {

	public static String randomUINT4() {
		return StringUtils.leftPad(RandomUtils.nextInt(9999)+"", 4, "0");
	}
}

package com.auth.realname.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeUtil {

	private static ZoneOffset ZONEOFFSET = ZoneOffset.of("+8");
	
	public static long currentMilli() {
		return LocalDateTime.now().toInstant(ZONEOFFSET).toEpochMilli();
	}
}

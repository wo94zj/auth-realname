package com.auth.realname.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

	public static String asString(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}

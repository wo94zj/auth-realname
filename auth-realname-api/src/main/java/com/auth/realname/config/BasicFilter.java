package com.auth.realname.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;

import com.auth.realname.util.AddressUtil;

public class BasicFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		MDC.put("ip", AddressUtil.getIpAddr((HttpServletRequest) request));
		chain.doFilter(request, response);
	}

}

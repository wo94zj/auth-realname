package com.auth.realname.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MessageSourceConfig {

	@Bean(name = "messageSource")
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
		rbms.setDefaultEncoding("UTF-8");
		//支持加载多个文件
		rbms.setBasenames("i18n/common");
		return rbms;
	}
	
	//SessionLocaleResolver
	@Bean
	public LocaleResolver localeResolver() {
		LocaleResolver localeResolver = new SessionLocaleResolver();
		return localeResolver;
	}
}

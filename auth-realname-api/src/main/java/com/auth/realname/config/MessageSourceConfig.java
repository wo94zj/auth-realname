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
		rbms.setBasename("i18n/messages");
		return rbms;
	}
	
	//SessionLocaleResolver
	@Bean
	public LocaleResolver localeResolver() {
		LocaleResolver localeResolver = new SessionLocaleResolver();
		return localeResolver;
	}
}

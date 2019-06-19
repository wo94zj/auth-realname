package com.auth.realname.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.auth.engine.IDcardAuthServiceEngine;
import com.auth.service.IDcardAuthService;

@Configuration
public class RegistryConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
    
	@Bean
	public HttpMessageConverters customConverters() {
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		List<MediaType> types = Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON,
				new MediaType("application", "*+json", Charset.forName("UTF-8")));
		fastJsonHttpMessageConverter.setSupportedMediaTypes(types);
		fastJsonHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));

		FastJsonConfig config = fastJsonHttpMessageConverter.getFastJsonConfig();
		config.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullBooleanAsFalse);
		config.setDateFormat("yyyy-MM-dd HH:mm:ss");
		return new HttpMessageConverters(fastJsonHttpMessageConverter);
	}
	
	@Bean
	public FilterRegistrationBean<BasicFilter> initFilter(){
		FilterRegistrationBean<BasicFilter> bean = new FilterRegistrationBean<BasicFilter>();
		BasicFilter filter = new BasicFilter();
		bean.setFilter(filter);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");
		bean.setUrlPatterns(urlPatterns);
		return bean;
	}
	
	@Bean
	public IDcardAuthService idcardAuthService() {
		return IDcardAuthServiceEngine.authProvider();
	}
}

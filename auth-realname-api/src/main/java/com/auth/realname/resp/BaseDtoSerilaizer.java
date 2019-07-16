package com.auth.realname.resp;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.alibaba.fastjson.serializer.BeforeFilter;
import com.auth.realname.util.ExceptionUtil;
import com.auth.realname.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseDtoSerilaizer extends BeforeFilter {

	private MessageSource messageSource;
	
	//匹配{}
	private final static Pattern BRACES_PATTERN = Pattern.compile("(\\{[^{}]+\\})");

	public BaseDtoSerilaizer(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public void writeBefore(Object object) {
		if(object instanceof BaseDto) {
			BaseDto<?> result = (BaseDto<?>) object;
			if(!StringUtil.isBlank(result.getMsg())) {
				StringBuffer sb = new StringBuffer();
				Matcher matcher = BRACES_PATTERN.matcher(result.getMsg());
				while(matcher.find()) {
					String key = matcher.group();
					String msg = null;
					try {
						msg = messageSource.getMessage(key.substring(1, key.length()-1), null, LocaleContextHolder.getLocale());
					} catch (Exception e) {
						log.error("MessageSource key :{}; ERROR :{}", key, ExceptionUtil.asString(e));
					}
					if(Objects.nonNull(msg)) {
						matcher.appendReplacement(sb, msg);
					}
				}
				matcher.appendTail(sb);
				result.setMsg(sb.toString());
			}
		}
	}

}
package com.auth.engine;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import com.auth.service.IDcardAuthService;
import com.auth.service.impl.DefaultIDcardAuthService;

public class IDcardAuthServiceEngine {

	public static IDcardAuthService authProvider() {
		ServiceLoader<IDcardAuthService> loader = ServiceLoader.load(IDcardAuthService.class);
		Optional<IDcardAuthService> optional = StreamSupport.stream(loader.spliterator(), false).findFirst();
		return optional.orElse(new DefaultIDcardAuthService());
	}
}

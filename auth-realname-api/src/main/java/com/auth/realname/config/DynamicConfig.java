package com.auth.realname.config;

import org.apache.commons.configuration.AbstractConfiguration;
import org.springframework.context.annotation.Bean;

import com.netflix.config.DynamicConfiguration;
//import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.sources.URLConfigurationSource;

public class DynamicConfig {

	@Bean
    public AbstractConfiguration addApplicationPropertiesSource() {
        PolledConfigurationSource source = new URLConfigurationSource("classpath:dynamic-config.properties");
        return new DynamicConfiguration(source, new FixedDelayPollingScheduler());
    }
}

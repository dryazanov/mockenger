package com.socialstartup.mockenger.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
@Configuration
@PropertySources(value = {
        @PropertySource("classpath:application-security-test.properties")
})
public class TestSecurityPropertyContext {}
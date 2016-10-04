package com.socialstartup.mockenger.core.config;

import com.socialstartup.mockenger.data.config.MongoDBConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author Dmitry Ryazanov
 */
@Configuration
@Import(MongoDBConfiguration.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.socialstartup.mockenger.core.log", "com.socialstartup.mockenger.core.service"})
public class CoreConfiguration {}
package org.mockenger.core.config;

import org.mockenger.data.config.MongoDBConfiguration;
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
@ComponentScan(basePackages = {"org.mockenger.core.log", "org.mockenger.core.service"})
public class CoreConfiguration {}
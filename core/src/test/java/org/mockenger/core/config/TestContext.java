package org.mockenger.core.config;

import org.mockenger.core.service.OAuth2TokenManager;
import org.mockenger.core.web.controller.endpoint.AccountController;
import org.mockenger.data.config.MongoDBConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
@Configuration
@EnableAutoConfiguration
@Import({MongoDBConfiguration.class})
@ComponentScan(
        excludeFilters = {
                @ComponentScan.Filter(value = OAuth2TokenManager.class, type = FilterType.ASSIGNABLE_TYPE),
                @ComponentScan.Filter(value = AccountController.class, type = FilterType.ASSIGNABLE_TYPE)
        },
        basePackages = {"org.mockenger.core.web", "org.mockenger.core.log", "org.mockenger.core.service"}
)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TestContext {}
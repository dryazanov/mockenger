package com.socialstartup.mockenger.core.config;

import com.socialstartup.mockenger.core.service.OAuth2TokenManager;
import com.socialstartup.mockenger.core.web.controller.endpoint.AccountController;
import com.socialstartup.mockenger.data.config.DatasourceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
@Configuration
@EnableWebMvc
@ComponentScan(
        excludeFilters = {
                @ComponentScan.Filter(value = OAuth2TokenManager.class, type = FilterType.ASSIGNABLE_TYPE),
                @ComponentScan.Filter(value = AccountController.class, type = FilterType.ASSIGNABLE_TYPE)
        },
        basePackages = {"com.socialstartup.mockenger.core.web", "com.socialstartup.mockenger.core.service"}
)
@Import({DatasourceConfiguration.class})
public class TestContext {}
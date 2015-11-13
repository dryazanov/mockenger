package com.socialstartup.mockenger.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.socialstartup.mockenger.core.web"})
@Import({CoreConfiguration.class, SecurityConfiguration.class, OAuth2ServerConfiguration.class})
public class TestContext {}
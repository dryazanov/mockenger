package com.socialstartup.mockenger.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;


@Profile("security")
@Configuration
@Import({ResourceServerConfiguration.class, AuthorizationServerConfiguration.class})
public class OAuth2ServerConfiguration {}
package org.mockenger.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * @author Dmitry Ryazanov
 */
@Profile("security")
@Configuration
@Import({OAuth2ResourceServerConfig.class, AuthorizationServerConfiguration.class})
public class OAuth2ServerConfiguration {}
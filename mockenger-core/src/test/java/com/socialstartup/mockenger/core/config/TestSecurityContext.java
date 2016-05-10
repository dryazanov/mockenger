package com.socialstartup.mockenger.core.config;

import com.socialstartup.mockenger.data.config.DatasourceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
@Configuration
@EnableWebMvc
@PropertySources(value = {
        @PropertySource("classpath:application-security-test.properties")
})
@ComponentScan(basePackages = {"com.socialstartup.mockenger.core.web", "com.socialstartup.mockenger.core.service"})
@Import({DatasourceConfiguration.class, SecurityConfiguration.class, OAuth2ServerConfiguration.class})
public class TestSecurityContext {

    private TokenStore inMemoryTokenStore = new InMemoryTokenStore();

    @Bean
    public TokenStore getTokenStore() {
        return inMemoryTokenStore;
    }
}
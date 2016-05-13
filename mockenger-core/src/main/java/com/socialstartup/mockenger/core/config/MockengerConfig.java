package com.socialstartup.mockenger.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author Dmitry Ryazanov
 */
@Configuration
@PropertySources(value = {
        @PropertySource("classpath:mongodb.properties"),
        @PropertySource("classpath:application.properties")
})
@ComponentScan(basePackages = {"com.socialstartup.mockenger.core.web"})
@Import({CoreConfiguration.class, SecurityConfiguration.class, OAuth2ServerConfiguration.class})
public class MockengerConfig {

    private TokenStore inMemoryTokenStore = new InMemoryTokenStore();

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public TokenStore getTokenStore() {
        return inMemoryTokenStore;
    }
}

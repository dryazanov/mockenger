package org.mockenger.core.config;

import org.mockenger.data.config.MongoDBConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
@Configuration
@EnableAutoConfiguration
@PropertySources(value = {
        @PropertySource("classpath:application-security-test.properties")
})
@ComponentScan(basePackages = {"org.mockenger.core.web", "org.mockenger.core.service"})
@Import({MongoDBConfiguration.class, SecurityConfiguration.class, OAuth2ServerConfiguration.class})
public class TestSecurityContext {

    private TokenStore inMemoryTokenStore = new InMemoryTokenStore();

    @Bean
    public TokenStore getTokenStore() {
        return inMemoryTokenStore;
    }

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
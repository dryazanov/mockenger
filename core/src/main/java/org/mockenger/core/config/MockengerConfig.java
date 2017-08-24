package org.mockenger.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@Configuration
@PropertySources(value = {
        @PropertySource("classpath:application.properties")
})
@ComponentScan(basePackages = {"org.mockenger.core.web"})
@Import({CoreConfiguration.class, FrontendConstConfig.class, SecurityConfiguration.class, OAuth2ServerConfiguration.class})
public class MockengerConfig extends WebMvcConfigurerAdapter {

    private final TokenStore inMemoryTokenStore = new InMemoryTokenStore();


	@Override
	public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
		// Turn off suffix-based content negotiation
		configurer.favorPathExtension(false);
	}


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Bean
    public TokenStore getTokenStore() {
        return inMemoryTokenStore;
    }


	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}

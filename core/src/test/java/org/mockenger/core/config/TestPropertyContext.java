package org.mockenger.core.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
@Configuration
@PropertySources(value = {
        @PropertySource("classpath:application-test.properties")
})
public class TestPropertyContext {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


	@Bean(name = "constantsBean")
	public String getConstants(final ApplicationContext applicationContext) {
		return "";
	}
}
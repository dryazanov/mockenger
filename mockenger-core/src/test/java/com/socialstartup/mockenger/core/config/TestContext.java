package com.socialstartup.mockenger.core.config;

import com.socialstartup.mockenger.data.repository.GroupEntityRepository;
import com.socialstartup.mockenger.data.repository.ProjectEntityRepository;
import com.socialstartup.mockenger.data.repository.RequestEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.mockito.Mockito.mock;

/**
 * Created by x079089 on 6/29/2015.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "com.socialstartup.mockenger.core.service",
        "com.socialstartup.mockenger.core.web.controller"
})
public class TestContext extends WebMvcConfigurerAdapter {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false)
                .favorParameter(true)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("json", MediaType.APPLICATION_JSON);
    }

    private RequestEntityRepository requestEntityRepository;
    private GroupEntityRepository groupEntityRepository;
    private ProjectEntityRepository projectEntityRepository;


    @Bean
    public RequestEntityRepository getRequestEntityRepository() {
        return mock(RequestEntityRepository.class);
    }

    @Bean
    public GroupEntityRepository getGroupEntityRepository() {
        return mock(GroupEntityRepository.class);
    }

    @Bean
    public ProjectEntityRepository getProjectEntityRepository() {
        return mock(ProjectEntityRepository.class);
    }
}
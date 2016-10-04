package com.socialstartup.mockenger.core.config;

import com.socialstartup.mockenger.data.model.dict.RoleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import static com.socialstartup.mockenger.core.web.controller.base.AbstractController.API_PATH;

/**
 * @author Dmitry Ryazanov
 */
@Profile("security")
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${oauth2.resource.id}")
    private String resourceId;

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceId);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
            .antMatchers(API_PATH + "/oauth/revoke").authenticated()
            .antMatchers(API_PATH + "/oauth/user").authenticated()
            .antMatchers(API_PATH + "/REST/**", API_PATH + "/SOAP/**", API_PATH + "/HTTP/**").anonymous()

            .antMatchers(HttpMethod.GET, API_PATH + "/projects/**", API_PATH + "/valueset/**", API_PATH + "/events/**")
            .hasAnyAuthority(RoleType.USER.name(), RoleType.MANAGER.name(), RoleType.ADMIN.name())

            .antMatchers(HttpMethod.DELETE, API_PATH + "/projects/**")
            .hasAnyAuthority(RoleType.MANAGER.name(), RoleType.ADMIN.name())

            .antMatchers(HttpMethod.POST, API_PATH + "/projects/**")
            .hasAnyAuthority(RoleType.MANAGER.name(), RoleType.ADMIN.name())

            .antMatchers(HttpMethod.PUT, API_PATH + "/projects/**")
            .hasAnyAuthority(RoleType.MANAGER.name(), RoleType.ADMIN.name())

            .antMatchers(HttpMethod.GET, API_PATH + "/valueset/roles")
            .hasAuthority(RoleType.ADMIN.name())

            .antMatchers(API_PATH + "/accounts/**")
            .hasAnyAuthority(RoleType.ADMIN.name())

            .anyRequest().permitAll();
        // @formatter:on
    }
}
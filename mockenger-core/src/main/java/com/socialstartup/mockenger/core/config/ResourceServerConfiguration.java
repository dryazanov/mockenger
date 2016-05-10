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

/**
 * Created by dryazanov on 26/04/16.
 */
@Profile("security")
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${oauth2.resource.id}")
    private String resourceId;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceId);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
            .antMatchers("/oauth/revoke").authenticated()
            .antMatchers("/oauth/user").authenticated()
            .antMatchers("/REST/**", "/SOAP/**", "/HTTP/**").anonymous()

            .antMatchers(HttpMethod.GET, "/projects/**", "/valueset/**", "/events/**")
            .hasAnyAuthority(RoleType.USER.name(), RoleType.MANAGER.name(), RoleType.ADMIN.name())

            .antMatchers(HttpMethod.DELETE, "/projects/**")
            .hasAnyAuthority(RoleType.MANAGER.name(), RoleType.ADMIN.name())

            .antMatchers(HttpMethod.POST, "/projects/**")
            .hasAnyAuthority(RoleType.MANAGER.name(), RoleType.ADMIN.name())

            .antMatchers(HttpMethod.PUT, "/projects/**")
            .hasAnyAuthority(RoleType.MANAGER.name(), RoleType.ADMIN.name())

            .antMatchers(HttpMethod.GET, "/valueset/roles")
            .hasAuthority(RoleType.ADMIN.name())

            .antMatchers("/accounts/**")
            .hasAnyAuthority(RoleType.ADMIN.name())

            .anyRequest().denyAll();
        // @formatter:on
    }
}
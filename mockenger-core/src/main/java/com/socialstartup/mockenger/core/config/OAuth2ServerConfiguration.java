package com.socialstartup.mockenger.core.config;

import com.socialstartup.mockenger.core.service.account.AccountService;
import com.socialstartup.mockenger.data.model.dict.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;


@Configuration
@Profile("security")
public class OAuth2ServerConfiguration {

    private static final String CLIENT_APP = "clientapp";
    private static final String SECRET = "123456";
    private static final String RESOURCE_ID = "mockenger-standalone-frontend";


    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .authorizeRequests()
                .antMatchers("/oauth/revoke").authenticated()
                .antMatchers("/oauth/user").authenticated()
                .antMatchers("/REST/**", "/SOAP/**", "/HTTP/**").anonymous()

                .antMatchers(HttpMethod.GET, "/projects/**", "/valueset/**")
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


    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        private final TokenStore tokenStore = new InMemoryTokenStore();

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private AccountService accountService;


        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.tokenStore(this.tokenStore)
                .authenticationManager(this.authenticationManager)
                .userDetailsService(accountService);
        }


        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            // @formatter:off
            clients
                .inMemory()
                    .withClient(CLIENT_APP)
                    .secret(SECRET)
                    .authorizedGrantTypes("password", "refresh_token")
                    .accessTokenValiditySeconds(600)
                    .refreshTokenValiditySeconds(3600)
                    .scopes("read", "write")
                    .resourceIds(RESOURCE_ID);
            // @formatter:on
        }


        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setTokenStore(this.tokenStore);
            return tokenServices;
        }
    }
}
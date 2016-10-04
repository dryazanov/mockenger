package com.socialstartup.mockenger.core.config;

import com.socialstartup.mockenger.core.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import static com.socialstartup.mockenger.core.web.controller.base.AbstractController.API_PATH;

/**
 * @author Dmitry Ryazanov
 */
@Profile("security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Value("${oauth2.client.app.name}")
    private String clientAppNAme;

    @Value("${oauth2.client.app.secret}")
    private String secret;

    @Value("${oauth2.resource.id}")
    private String resourceId;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)
				.pathMapping("/oauth/token", API_PATH + "/oauth/token")
                .authenticationManager(authenticationManager)
                .userDetailsService(accountService);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
				.withClient(clientAppNAme)
				.secret(secret)
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(600)
				.refreshTokenValiditySeconds(3600)
				.scopes("read", "write")
				.resourceIds(resourceId);
    }


    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore);
        return tokenServices;
    }
}

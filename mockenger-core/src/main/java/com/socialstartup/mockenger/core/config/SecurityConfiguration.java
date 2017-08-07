package com.socialstartup.mockenger.core.config;

import com.socialstartup.mockenger.core.service.account.AccountService;
import com.socialstartup.mockenger.data.model.dict.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

import static com.socialstartup.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static com.socialstartup.mockenger.data.model.dict.ProjectType.HTTP;
import static com.socialstartup.mockenger.data.model.dict.ProjectType.REST;
import static com.socialstartup.mockenger.data.model.dict.ProjectType.SOAP;
import static java.util.stream.Collectors.toList;

/**
 * @author Dmitry Ryazanov
 */
@Profile("security")
@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountService accountService;

	@Autowired
	private PasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.formLogin().disable()

				.requestMatcher(new OrRequestMatcher(getRequestMatchers()))

				.authorizeRequests()
				.antMatchers("/index.html").permitAll()
				.antMatchers(API_PATH + "/oauth/revoke").authenticated()
				.antMatchers(API_PATH + "/oauth/user").authenticated()
				.antMatchers(API_PATH + "/oauth/token").permitAll()
//				.antMatchers(API_PATH + "/REST/**", API_PATH + "/SOAP/**", API_PATH + "/HTTP/**").permitAll()

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

	}

	private List<RequestMatcher> getRequestMatchers() {
		return Arrays.asList(REST, SOAP, HTTP).stream()
				.map(v -> new AntPathRequestMatcher(API_PATH + "/" + v + "/**"))
				.collect(toList());
	}


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

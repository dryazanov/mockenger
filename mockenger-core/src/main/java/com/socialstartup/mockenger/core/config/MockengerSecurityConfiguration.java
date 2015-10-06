package com.socialstartup.mockenger.core.config;

/**
 * Created by Dmitry Ryazanov on 14-Sep-15.
 */
//@Configuration
//@EnableWebSecurity
//@Profile("security")
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MockengerSecurityConfiguration {//extends WebSecurityConfigurerAdapter {

    /*@Autowired
    private UserService userService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/


    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
        //auth.userDetailsService(userService);

    }*/

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().anyRequest().fullyAuthenticated();
//        http.httpBasic();
//        http.csrf().disable();


        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll();
//            .anyRequest().authenticated()
//            .and()
//            .formLogin()
//            .loginPage("/login")
//            .permitAll();
//
//        http.csrf().disable();
    }*/
}

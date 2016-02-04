package com.socialstartup.mockenger.core.web.controller.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.core.config.TestContext;
import com.socialstartup.mockenger.core.config.TestPropertyContext;
import com.socialstartup.mockenger.core.config.TestSecurityPropertyContext;
import com.socialstartup.mockenger.core.model.TokenResponse;
import com.socialstartup.mockenger.core.service.account.AccountService;
import com.socialstartup.mockenger.data.model.dict.RoleType;
import com.socialstartup.mockenger.data.model.persistent.account.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.junit.Assert.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        TestPropertyContext.class,
        TestSecurityPropertyContext.class,
        TestContext.class
})
public class OAuthExtendedControllerTest {
    private static final String OAUTH_GET_TOKEN = "/oauth/token?grant_type=password";
    private static final String OAUTH_REVOKE_TOKEN = "/oauth/revoke";
    private static final String OAUTH_GET_USER = "/oauth/user";

    private static final String ACCOUNT_USERNAME = "testuser";
    private static final String ACCOUNT_PASSWORD = "123456";
    private static final String ACCOUNT_FNAME = "F";
    private static final String ACCOUNT_LNAME = "L";

    private static final String AUTHORIZATION = "Authorization";
    private static final String CLIENT_APP = "clientapp";
    private static final String SECRET = "123456";


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private AccountService accountService;

    protected MockMvc mockMvc;


    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilters(springSecurityFilterChain).build();

        deleteAllAccounts();
        createAccount();
    }

    @Test
    public void testGetUser() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get(OAUTH_GET_USER)
                .header(AUTHORIZATION, getBearer(getToken())));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(ACCOUNT_USERNAME))
                .andExpect(jsonPath("$.firstName").value(ACCOUNT_FNAME))
                .andExpect(jsonPath("$.lastName").value(ACCOUNT_LNAME))
                .andExpect(jsonPath("$.role").value(RoleType.ADMIN.name()));
    }

    @Test
    public void testGetNonExistingUser() throws Exception {
        String token = getToken();
        deleteAllAccounts();

        this.mockMvc.perform(get(OAUTH_GET_USER).header(AUTHORIZATION, getBearer(token)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRevokeToken() throws Exception {
        String token = getToken();
        ResultActions resultActions = this.mockMvc.perform(post(OAUTH_REVOKE_TOKEN)
                .header(AUTHORIZATION, getBearer(token))
                .param("token", token));

        resultActions.andExpect(status().isNoContent());
        assertNotEquals(token , getToken());
    }


    private String getToken() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post(OAUTH_GET_TOKEN)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", ACCOUNT_USERNAME)
                .param("password", ACCOUNT_PASSWORD)
                .with(httpBasic(CLIENT_APP, SECRET)));

        String resultJson = resultActions.andReturn().getResponse().getContentAsString();
        TokenResponse response = new ObjectMapper(new JsonFactory()).readValue(resultJson, TokenResponse.class);

        return response.getAccessToken();
    }


    private String getBearer(String token) {
        return String.format("Bearer %s", token);
    }


    private void deleteAllAccounts() {
        accountService.removeAll();
    }


    private void createAccount() {
        Account account = new Account();
        account.setFirstName(ACCOUNT_FNAME);
        account.setLastName(ACCOUNT_LNAME);
        account.setUsername(ACCOUNT_USERNAME);
        account.setPassword((new BCryptPasswordEncoder()).encode(ACCOUNT_PASSWORD));
        account.setRoles(RoleType.ADMIN);

        accountService.save(account);
    }
}



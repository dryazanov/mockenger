package com.socialstartup.mockenger.core.web.controller.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.core.config.TestPropertyContext;
import com.socialstartup.mockenger.core.config.TestSecurityContext;
import com.socialstartup.mockenger.core.model.TokenResponse;
import com.socialstartup.mockenger.core.service.account.AccountService;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.dict.RoleType;
import com.socialstartup.mockenger.data.model.persistent.account.Account;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dmitry Ryazanov
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        TestPropertyContext.class,
        TestSecurityContext.class
})
public class OAuth2AbstractControllerTest {
    protected static final String OAUTH_GET_TOKEN = AbstractController.API_PATH + "/oauth/token?grant_type=password";
    protected static final String OAUTH_REVOKE_TOKEN = AbstractController.API_PATH + "/oauth/revoke";
    protected static final String OAUTH_GET_USER = AbstractController.API_PATH + "/oauth/user";

    protected static final String ACCOUNT_USERNAME = "testuser";
    protected static final String ACCOUNT_PASSWORD = "123456";
    protected static final String ACCOUNT_FNAME = "F";
    protected static final String ACCOUNT_LNAME = "L";

    protected static final String AUTHORIZATION = "Authorization";
    protected static final String CLIENT_APP = "clientapp";
    protected static final String SECRET = "123456";

    protected static final String ENDPOINT_ACCOUNT = AbstractController.API_PATH + "/accounts/";
    protected static final String SEMICOLON = ";";
    protected static final String CHARSET_UTF8 = "charset=UTF-8";
    protected static final String CONTENT_TYPE_JSON_UTF8 = MediaType.APPLICATION_JSON_VALUE + SEMICOLON + CHARSET_UTF8;


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected MockMvc mockMvc;


    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build();

        deleteAllAccounts();
        createAdminAccount();
    }


    protected void deleteAccountRestRequest(final String token, final Account account) throws Exception {
        mockMvc.perform(
                delete(ENDPOINT_ACCOUNT + account.getId())
                        .header(AUTHORIZATION, getBearerHeaderValue(token))
                        .contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
    }


    protected String getTokenRestRequest() throws Exception {
        final ResultActions resultActions = mockMvc.perform(
                post(OAUTH_GET_TOKEN)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", ACCOUNT_USERNAME)
                        .param("password", ACCOUNT_PASSWORD)
                        .with(httpBasic(CLIENT_APP, SECRET)));

        final String resultJson = resultActions.andReturn().getResponse().getContentAsString();
        final TokenResponse response = new ObjectMapper(new JsonFactory()).readValue(resultJson, TokenResponse.class);

        return response.getAccessToken();
    }


    protected String getBearerHeaderValue(final String token) {
        return OAuth2AccessToken.BEARER_TYPE + " " + token;
    }


    // ===============
    // ACCOUNT HELPERS
    // ===============

    protected Account getAccount(final String accountId) {
        return accountService.findById(accountId);
    }

    protected Iterable<Account> getAllAccounts() {
        return accountService.findAll();
    }

    protected void deleteAllAccounts() {
        accountService.removeAll();
    }

    protected void deleteAccount(final Account account) {
        accountService.remove(account);
    }

//    protected Account createAccount() {
//        return createAccount(true, RoleType.ADMIN);
//    }

//    protected Account createAccount(final RoleType role) {
//        return createAccount(getAccountBuilder(role).build());
//    }

//    protected Account createAccount(final boolean randomUsername, final RoleType role) {
//        return createAccount(getAccountBuilder(randomUsername, role).build());
//    }

//    protected Account createAccount(final Account account) {
//        return accountService.save(account);
//    }

    protected Account createAdminAccount() {
        final Account account = Account.builder()
                .firstName(ACCOUNT_FNAME)
                .lastName(ACCOUNT_LNAME)
                .username(ACCOUNT_USERNAME)
                .password(passwordEncoder.encode(ACCOUNT_PASSWORD))
                .role(RoleType.ADMIN)
                .build();

        return accountService.save(account);
    }

    protected Account createRandomAccount() {
        return createRandomAccount(RoleType.ADMIN);
    }

    protected Account createRandomAccount(final RoleType roleType) {
        final Account account = Account.builder().firstName(ACCOUNT_FNAME)
                .lastName(ACCOUNT_LNAME)
                .username(ACCOUNT_USERNAME + CommonUtils.generateUniqueId())
                .password(passwordEncoder.encode(ACCOUNT_PASSWORD))
                .role(roleType)
                .build();

        return accountService.save(account);
    }

    protected Account.AccountBuilder getAccountBuilder() {
        return getAccountBuilder(true, RoleType.ADMIN);
    }

    protected Account.AccountBuilder getAccountBuilder(final boolean randomUsername) {
        return getAccountBuilder(randomUsername, RoleType.ADMIN);
    }

    protected Account.AccountBuilder getAccountBuilder(final boolean randomUsername, final RoleType roleType) {
        final String id = CommonUtils.generateUniqueId();

        return Account.builder()
                .id(id)
                .firstName(ACCOUNT_FNAME)
                .lastName(ACCOUNT_LNAME)
                .username(ACCOUNT_USERNAME + (randomUsername ? CommonUtils.generateUniqueId() : ""))
                .password(ACCOUNT_PASSWORD)
                .role(roleType);
    }
}



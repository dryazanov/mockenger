package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.web.controller.base.OAuth2AbstractControllerTest;
import com.socialstartup.mockenger.data.model.dict.RoleType;
import com.socialstartup.mockenger.data.model.persistent.account.Account;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
public class AccountControllerTest extends OAuth2AbstractControllerTest {

    private static final String ENDPOINT_ACCOUNT = "/accounts/";
    private static final String ACCOUNT_FIRST_NAME_UPDATED = "First name1";
    private static final String ACCOUNT_LAST_NAME_UPDATED = "Last name1";
    private static final String ACCOUNT_USERNAME_UPDATED = "UserName1";
    private static final String ACCOUNT_PASSWORD_TUPDATED = "pswd1";
    
    
    private static final String PROJECT_WITH_WRONG_TYPE = "{\"name\":\"ABC project\",\"type\":\"WRONG\"}";


    @Before
    public void setUp() {
        super.setUp();

        deleteAllAccounts();
        createAdminAccount();
    }

    @Test
    public void testAddAccountWithEmptyFirstName() throws Exception {
        testAddAccountFailed(getAccountBuilder().firstName(""), "First name: may not be null or empty");
    }

    @Test
    public void testAddAccountWithNullFirstName() throws Exception {
        testAddAccountFailed(getAccountBuilder().firstName(null), "First name: may not be null or empty");
    }

    @Test
    public void testAddAccountWithEmptyLastName() throws Exception {
        testAddAccountFailed(getAccountBuilder().lastName(""), "Last name: may not be null or empty");
    }

    @Test
    public void testAddAccountWithNullLastName() throws Exception {
        testAddAccountFailed(getAccountBuilder().lastName(null), "Last name: may not be null or empty");
    }

    @Test
    public void testAddAccountWithEmptyUserName() throws Exception {
        testAddAccountFailed(getAccountBuilder().username(""), "Username: may not be null or empty");
    }

    @Test
    public void testAddAccountWithNullUserName() throws Exception {
        testAddAccountFailed(getAccountBuilder().username(null), "Username: may not be null or empty");
    }

    private void testAddAccountFailed(final Account.AccountBuilder account, final String expectedError) throws Exception {
        final String token = getTokenRestRequest();
        final ResultActions resultActions = createAccountRest(token, account.build());

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value(expectedError));
    }

    @Test
    public void testAddAccount() throws Exception {
        final String token = getTokenRestRequest();

        // Expect response status 200
        createAccountRest(token, getAccountBuilder().build())
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));

        // Expect response status 200
        createAccountRest(token, getAccountBuilder().build())
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));

        deleteAllAccounts();
    }

    @Test
    public void testSaveAccount() throws Exception {
        final String token = getTokenRestRequest();
        final Account account = createRandomAccount();
        final Account accountToUpdate = getAccountBuilder().id(account.getId()).username(ACCOUNT_USERNAME_UPDATED).build();
        final ResultActions resultActions = updateAccountRest(token, accountToUpdate);

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(accountToUpdate.getId()));

        assertEquals(ACCOUNT_USERNAME_UPDATED, getAccount(accountToUpdate.getId()).getUsername());
    }

    @Test
    public void testSaveAccountWithEmptyUserName() throws Exception {
        testSaveAccount("");
    }

    @Test
    public void testSaveAccountWithNullUserName() throws Exception {
        testSaveAccount(null);
    }

    private void testSaveAccount(final String username) throws Exception {
        final String token = getTokenRestRequest();
        final Account project = createRandomAccount();
        final Account projectToUpdate = getAccountBuilder().id(project.getId()).username(username).build();
        final ResultActions resultActions = updateAccountRest(token, projectToUpdate);

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Username: may not be null or empty"));
    }

//    @Test
//    public void testDeleteAccount() throws Exception {
//        final Account account = createRandomAccount();
//        final ResultActions resultActions = deleteAccountRest(account.getId());
//
//        resultActions.andExpect(status().isNoContent())
//                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
//    }

    @Test
    public void testDeleteAccount() throws Exception {
        final String token = getTokenRestRequest();
        final Account account1 = createRandomAccount();
        final Account account2 = createRandomAccount();

        deleteAccountRestRequest(token, account1);
        deleteAccountRestRequest(token, account2);
    }

    @Test
    public void testGetAccountList() throws Exception {
        final String token = getTokenRestRequest();

        createRandomAccount(RoleType.ADMIN);
        createRandomAccount(RoleType.MANAGER);
        createRandomAccount(RoleType.USER);

        getAccountAllRest(token)
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)));

        deleteAllAccounts();
    }


    private ResultActions getAccountAllRest(final String token) throws Exception {
        return mockMvc.perform(
                get(ENDPOINT_ACCOUNT)
                        .header(AUTHORIZATION, getBearerHeaderValue(token))
                        .accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ResultActions getAccountRest(final String token, final String accountId) throws Exception {
        return mockMvc.perform(
                get(ENDPOINT_ACCOUNT + accountId)
                        .header(AUTHORIZATION, getBearerHeaderValue(token))
                        .accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ResultActions createAccountRest(final String token, final Account account) throws Exception {
        final String accountJson = getObjectMapper().writeValueAsString(account);
        return mockMvc.perform(
                post(ENDPOINT_ACCOUNT)
                        .header(AUTHORIZATION, getBearerHeaderValue(token))
                        .contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8))
                        .content(accountJson));
    }

    private ResultActions updateAccountRest(final String token, final Account account) throws Exception {
        final String accountJson = getObjectMapper().writeValueAsString(account);
        return mockMvc.perform(
                put(ENDPOINT_ACCOUNT + account.getId())
                        .header(AUTHORIZATION, getBearerHeaderValue(token))
                        .contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8))
                        .content(accountJson));
    }

    private ResultActions deleteAccountRest(final String token, final String accountId) throws Exception {
        return mockMvc.perform(
                delete(ENDPOINT_ACCOUNT + accountId)
                        .header(AUTHORIZATION, getBearerHeaderValue(token))
                        .contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper(new JsonFactory()).configure(SerializationConfig.Feature.USE_ANNOTATIONS, false);
    }
}

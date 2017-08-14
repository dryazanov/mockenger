package org.mockenger.core.web.controller.base;

import org.mockenger.data.model.dict.RoleType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dmitry Ryazanov
 */
public class OAuth2ExtendedControllerTest extends OAuth2AbstractControllerTest {

    @Before
    public void setUp() {
        super.setUp();

        deleteAllAccounts();
        createAdminAccount();
    }

    @Test
    public void testGetUserInfo() throws Exception {
        final ResultActions resultActions = mockMvc.perform(
                get(OAUTH_GET_USER)
                        .header(AUTHORIZATION, getBearerHeaderValue(getTokenRestRequest())));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(ACCOUNT_USERNAME))
                .andExpect(jsonPath("$.firstName").value(ACCOUNT_FNAME))
                .andExpect(jsonPath("$.lastName").value(ACCOUNT_LNAME))
                .andExpect(jsonPath("$.role").value(RoleType.ADMIN.name()));
    }

    @Test
    public void testGetNonExistingUser() throws Exception {
        final String token = getTokenRestRequest();
        deleteAllAccounts();

        mockMvc.perform(get(OAUTH_GET_USER).header(AUTHORIZATION, getBearerHeaderValue(token)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRevokeToken() throws Exception {
        final String token = getTokenRestRequest();
        final ResultActions resultActions = mockMvc.perform(
                post(OAUTH_REVOKE_TOKEN)
                        .header(AUTHORIZATION, getBearerHeaderValue(token))
                        .param("token", token));

        resultActions.andExpect(status().isNoContent());
        assertNotEquals(token , getTokenRestRequest());
    }
}



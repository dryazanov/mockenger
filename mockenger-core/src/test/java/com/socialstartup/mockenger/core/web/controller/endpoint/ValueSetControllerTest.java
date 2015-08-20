package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.config.TestContext;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by x079089 on 6/29/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestContext.class})
public class ValueSetControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    private static final String ENDPOINT_VALUESET = "/valueset";
    protected static final String CONTENT_TYPE_JSON_UTF8 = "application/json;charset=UTF-8";


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    public void testGetProject() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get(ENDPOINT_VALUESET).param("id", "project-type"));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.REST").value(ProjectType.REST.getType()))
                .andExpect(jsonPath("$.SOAP").value(ProjectType.SOAP.getType()))
                .andExpect(jsonPath("$.SIMPLE").value(ProjectType.SIMPLE.getType()));
    }

    @Test
    public void testGetProjectIdIsNull() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get(ENDPOINT_VALUESET).param("id", "project"));
        resultActions.andExpect(status().isNotFound()).andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
    }
}

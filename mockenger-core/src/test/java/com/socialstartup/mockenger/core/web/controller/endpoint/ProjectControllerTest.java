package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.config.TestContext;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
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
public class ProjectControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProjectService projectService;

    @Mock
    private Project projectMock;

    private MockMvc mockMvc;

    private static final String ENDPOINT = "/projects/";
    private static final String PROJECT_ID = "557050015a672b9af6cfcb49";
    private static final String PROJECT_NAME = "ProjectOne";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        projectMock = new Project(PROJECT_NAME, ProjectType.SIMPLE);
        projectMock.setId(PROJECT_ID);
    }

    @Test
    public void testGetProjectById() throws Exception {
        when(projectService.findById(eq(PROJECT_ID))).thenReturn(projectMock);

        ResultActions resultActions = this.mockMvc.perform(get(ENDPOINT + PROJECT_ID).accept(MediaType.parseMediaType(CONTENT_TYPE)));
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(jsonPath("$.id").value(PROJECT_ID))
            .andExpect(jsonPath("$.name").value(PROJECT_NAME))
            .andExpect(jsonPath("$.type").value(ProjectType.SIMPLE.name()));
    }

}

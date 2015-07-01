package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.core.config.TestContext;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Iterator;

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

    private MockMvc mockMvc;

    private static final String ENDPOINT = "/projects/";
    private static final String PROJECT_ID_TEST = "TEST_ID";
    private static final String PROJECT_NAME_TEST = "Unit-test project";
    private static final String PROJECT_NAME_TWO = "ABC project";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGetProject() throws Exception {
        Project project = createProject();

        ResultActions resultActions = getProjectRest(project.getId());
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(jsonPath("$.id").value(project.getId()))
            .andExpect(jsonPath("$.name").value(project.getName()))
            .andExpect(jsonPath("$.type").value(ProjectType.SIMPLE.name()));

        deleteProject(project);
    }

    @Test
    public void testGetProjectIdIsNull() throws Exception {
        ResultActions resultActions = getProjectRest(null);
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Project with ID 'null' not found"));
    }

    @Test
    public void testGetProjectNotFound() throws Exception {
        ResultActions resultActions = getProjectRest(PROJECT_ID_TEST);
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Project with ID '" + PROJECT_ID_TEST + "' not found"));
    }

    @Test
    public void testAddProjectWithEmptyName() throws Exception {
        Project project = getNewProject();
        project.setName("");
        ResultActions resultActions = createProjectRest(project);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("name: may not be empty"));
    }

    @Test
    public void testAddProjectWithNullName() throws Exception {
        Project project = getNewProject();
        project.setName(null);
        ResultActions resultActions = createProjectRest(project);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("name: may not be empty"));
    }

    @Test
    public void testAddProjectWithNullType() throws Exception {
        Project project = getNewProject();
        project.setType(null);
        ResultActions resultActions = createProjectRest(project);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("type: may not be null"));
    }

    @Test
    public void testAddProject() throws Exception {
        ResultActions resultActions = null;
        Project project = getNewProject();

        // Expect response status 200
        resultActions = createProjectRest(project);
        resultActions.andExpect(status().isCreated()).andExpect(content().contentType(CONTENT_TYPE));

        // Expect response status 200
        resultActions = createProjectRest(project);
        resultActions.andExpect(status().isCreated()).andExpect(content().contentType(CONTENT_TYPE));

        deleteAllProjects();
    }

    @Test
    public void testSaveProject() throws Exception {
        Project project = createProject();
        project.setName(PROJECT_NAME_TWO);

        ResultActions resultActions = updateProjectRest(project);
        resultActions.andExpect(status().isNoContent()).andExpect(content().contentType(CONTENT_TYPE));

        assertEquals(PROJECT_NAME_TWO, getProject(project.getId()).getName());
    }

    @Test
    public void testDeleteProject() throws Exception {
        Project project = createProject();
        ResultActions resultActions = deleteProjectRest(project.getId());
        resultActions.andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE));
    }


    private ResultActions getProjectRest(String projectId) throws Exception {
        return this.mockMvc.perform(get(ENDPOINT + projectId).accept(MediaType.parseMediaType(CONTENT_TYPE)));
    }

    private ResultActions createProjectRest(Project project) throws Exception {
        String projectJson = new ObjectMapper(new JsonFactory()).writeValueAsString(project);
        return this.mockMvc.perform(post(ENDPOINT).contentType(MediaType.parseMediaType(CONTENT_TYPE)).content(projectJson));
    }

    private ResultActions updateProjectRest(Project project) throws Exception {
        String projectJson = new ObjectMapper(new JsonFactory()).writeValueAsString(project);
        return this.mockMvc.perform(put(ENDPOINT + project.getId()).contentType(MediaType.parseMediaType(CONTENT_TYPE)).content(projectJson));
    }

    private ResultActions deleteProjectRest(String projectId) throws Exception {
        return this.mockMvc.perform(delete(ENDPOINT + projectId).contentType(MediaType.parseMediaType(CONTENT_TYPE)));
    }

    private Project getProject(String projectId) throws Exception {
        return this.projectService.findById(projectId);
    }

    private Iterable<Project> getAllProjects() throws Exception {
        return this.projectService.findAll();
    }

    private Project getAnyProject() throws Exception {
        return this.projectService.findAll().iterator().next();
    }

    private void deleteProject(Project project) throws Exception {
        this.projectService.remove(project);
    }

    private void deleteAllProjects() throws Exception {
        Iterator<Project> iterator = getAllProjects().iterator();
        while (iterator.hasNext()) {
            deleteProject(iterator.next());
        }
    }

    private Project createProject() throws Exception {
        Project project = getNewProject();
        this.projectService.save(project);
        return project;
    }

    private Project getNewProject() throws Exception {
        Project project = new Project(PROJECT_NAME_TEST, ProjectType.SIMPLE);
        project.setId(CommonUtils.generateUniqueId());
        return project;
    }
}

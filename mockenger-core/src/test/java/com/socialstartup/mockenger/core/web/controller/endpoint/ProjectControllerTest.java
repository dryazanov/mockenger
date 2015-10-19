package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
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
public class ProjectControllerTest extends AbstractControllerTest {
    private static final String ENDPOINT_PROJECT = "/projects/";
    private static final String PROJECT_NAME_UPDATED = "ABC project";
    private static final String PROJECT_WITH_WRONG_TYPE = "{\"name\":\"ABC project\",\"type\":\"WRONG\"}";


    @Before
    public void setup() {
        super.setup();

        deleteAllProjects();
    }


    @Test
    public void testGetProject() throws Exception {
        Project project = createProject();

        ResultActions resultActions = getProjectRest(project.getId());
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
            .andExpect(jsonPath("$.id").value(project.getId()))
            .andExpect(jsonPath("$.name").value(PROJECT_NAME_TEST))
            .andExpect(jsonPath("$.type").value(ProjectType.HTTP.name()));

        deleteProject(project);
    }

    @Test
    public void testGetProjectIdIsNull() throws Exception {
        ResultActions resultActions = getProjectRest(null);
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Project with ID 'null' not found"));
    }

    @Test
    public void testGetProjectNotFound() throws Exception {
        ResultActions resultActions = getProjectRest(PROJECT_ID);
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Project with ID '" + PROJECT_ID + "' not found"));
    }

    @Test
    public void testAddProjectWithEmptyName() throws Exception {
        testAddProject("");
    }

    @Test
    public void testAddProjectWithNullName() throws Exception {
        testAddProject(null);
    }

    private void testAddProject(String name) throws Exception {
        Project project = getNewProject();
        project.setName(name);
        ResultActions resultActions = createProjectRest(project);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Name: may not be null or empty"));
    }

    @Test
    public void testAddProjectWithNullType() throws Exception {
        Project project = getNewProject();
        project.setType(null);

        ResultActions resultActions = createProjectRest(project);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Type: may not be null"));
    }

    @Test
    public void testAddProjectWithWrongType() throws Exception {
        ResultActions resultActions =  this.mockMvc.perform(post(ENDPOINT_PROJECT).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)).content(PROJECT_WITH_WRONG_TYPE));
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Unable to process request: json is not readable"));
    }

    @Test
    public void testAddProject() throws Exception {
        ResultActions resultActions = null;
        Project project = getNewProject();

        // Expect response status 200
        resultActions = createProjectRest(project);
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(not(project.getId())));

        // Expect response status 500
        resultActions = createProjectRest(project);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors[0]").value(String.format("Project with the code '%s' already exist", project.getCode())));

        // Expect response status 200
        project.setCode(PROJECT_CODE_TEST + "1");
        resultActions = createProjectRest(project);
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(not(project.getId())));

        deleteAllProjects();
    }

    @Test
    public void testSaveProject() throws Exception {
        Project project = createProject();
        project.setName(PROJECT_NAME_UPDATED);

        ResultActions resultActions = updateProjectRest(project);
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(project.getId()));

        assertEquals(PROJECT_NAME_UPDATED, getProject(project.getId()).getName());
    }

    @Test
    public void testSaveProjectWithEmptyName() throws Exception {
        testSaveProject("");
    }

    @Test
    public void testSaveProjectWithNullName() throws Exception {
        testSaveProject(null);
    }

    private void testSaveProject(String name) throws Exception {
        Project project = createProject();
        project.setName(name);

        ResultActions resultActions = updateProjectRest(project);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Name: may not be null or empty"));
    }

    @Test
    public void testSaveProjectWithNullType() throws Exception {
        Project project = createProject();
        project.setType(null);

        ResultActions resultActions = updateProjectRest(project);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Type: may not be null"));
    }

    @Test
    public void testDeleteProject() throws Exception {
        Project project = createProject();
        ResultActions resultActions = deleteProjectRest(project.getId());
        resultActions.andExpect(status().isNoContent()).andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
    }

    @Test
    public void testGetProjectList() throws Exception {
        createProject(true);
        createProject(true);
        createProject(true);

        ResultActions resultActions = getProjectAllRest();
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].name", is(PROJECT_NAME_TEST)))
                .andExpect(jsonPath("$.[1].name", is(PROJECT_NAME_TEST)))
                .andExpect(jsonPath("$.[2].name", is(PROJECT_NAME_TEST)));

        deleteAllProjects();
    }


    private ResultActions getProjectAllRest() throws Exception {
        return this.mockMvc.perform(get(ENDPOINT_PROJECT).accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ResultActions getProjectRest(String projectId) throws Exception {
        return this.mockMvc.perform(get(ENDPOINT_PROJECT + projectId).accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ResultActions createProjectRest(Project project) throws Exception {
        String projectJson = new ObjectMapper(new JsonFactory()).writeValueAsString(project);
        return this.mockMvc.perform(post(ENDPOINT_PROJECT).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)).content(projectJson));
    }

    private ResultActions updateProjectRest(Project project) throws Exception {
        String projectJson = new ObjectMapper(new JsonFactory()).writeValueAsString(project);
        return this.mockMvc.perform(put(ENDPOINT_PROJECT + project.getId()).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)).content(projectJson));
    }

    private ResultActions deleteProjectRest(String projectId) throws Exception {
        return this.mockMvc.perform(delete(ENDPOINT_PROJECT + projectId).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }


}

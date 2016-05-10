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
    public void setUp() {
        super.setUp();

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

    private void testAddProject(final String name) throws Exception {
        final ResultActions resultActions = createProjectRest(getProjectBuilder().name(name).build());

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Project name: may not be null or empty"));
    }

    @Test
    public void testAddProjectWithNullType() throws Exception {
        final ResultActions resultActions = createProjectRest(getProjectBuilder().type(null).build());

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Project type: may not be null"));
    }

    @Test
    public void testAddProjectWithWrongType() throws Exception {
        final ResultActions resultActions =  this.mockMvc.perform(
                post(ENDPOINT_PROJECT)
                        .contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8))
                        .content(PROJECT_WITH_WRONG_TYPE));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Unable to process request: json is not readable"));
    }

    @Test
    public void testAddProject() throws Exception {
        final Project project1 = getProjectBuilder().build();

        // Expect response status 200
        final ResultActions resultActions1 = createProjectRest(project1);
        resultActions1.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(not(project1.getId())));

        // Expect response status 500
        final ResultActions resultActions2 = createProjectRest(project1);
        resultActions2.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors[0]").value(String.format("Project with the code '%s' already exist", project1.getCode())));

        // Expect response status 200
        final Project project2 = getProjectBuilder().code(PROJECT_CODE_TEST + "1").build();
        final ResultActions resultActions3 = createProjectRest(project2);
        resultActions3.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(not(project2.getId())));

        deleteAllProjects();
    }

    @Test
    public void testSaveProject() throws Exception {
        final Project project = createProject();
        final Project projectToUpdate = getProjectBuilder().id(project.getId()).name(PROJECT_NAME_UPDATED).build();
        final ResultActions resultActions = updateProjectRest(projectToUpdate);

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

    private void testSaveProject(final String name) throws Exception {
        final Project project = createProject();
        final Project projectToUpdate = getProjectBuilder().id(project.getId()).name(name).build();
        final ResultActions resultActions = updateProjectRest(projectToUpdate);

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Project name: may not be null or empty"));
    }

    @Test
    public void testSaveProjectWithNullType() throws Exception {
        final Project project = createProject();
        final Project projectToUpdate = getProjectBuilder().id(project.getId()).type(null).build();
        final ResultActions resultActions = updateProjectRest(projectToUpdate);

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Project type: may not be null"));
    }

    @Test
    public void testDeleteProject() throws Exception {
        final Project project = createProject();
        final ResultActions resultActions = deleteProjectRest(project.getId());

        resultActions.andExpect(status().isNoContent())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
    }

    @Test
    public void testGetProjectList() throws Exception {
        createProject(true);
        createProject(true);
        createProject(true);

        final ResultActions resultActions = getProjectAllRest();

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

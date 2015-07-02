package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.core.config.TestContext;
import com.socialstartup.mockenger.core.service.GroupService;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
public class GroupControllerTest extends AbstractControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GroupService groupService;

    private MockMvc mockMvc;

//    private static final String ENDPOINT_PROJECT = "/projects/";
    private static final String ENDPOINT_GROUP = "/projects/" + PROJECT_ID + "/groups/";
    private static final String GROUP_ID_TEST = "TEST_ID";
    private static final String GROUP_NAME_TWO = "ABC group";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGetGroup() throws Exception {
        Group group = createGroup();

        ResultActions resultActions = getGroupRest(group.getId());
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(jsonPath("$.id").value(group.getId()))
            .andExpect(jsonPath("$.name").value(group.getName()))
            .andExpect(jsonPath("$.recording").value(true));

        deleteGroup(group);
    }

    @Test
    public void testGetGroupIdIsNull() throws Exception {
        ResultActions resultActions = getGroupRest(null);
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Group with ID 'null' not found"));
    }

    @Test
    public void testGetProjectNotFound() throws Exception {
        ResultActions resultActions = getGroupRest(GROUP_ID_TEST);
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Group with ID '" + GROUP_ID_TEST + "' not found"));
    }

    @Test
    public void testAddProjectWithEmptyName() throws Exception {
        Group group = getNewGroup();
        group.setName("");
        ResultActions resultActions = createGroupRest(group);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("name: may not be empty"));
    }

    @Test
    public void testAddProjectWithNullName() throws Exception {
        Group group = getNewGroup();
        group.setName(null);
        ResultActions resultActions = createGroupRest(group);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("name: may not be empty"));
    }

    @Test
    public void testAddProject() throws Exception {
        ResultActions resultActions = null;
        Group group = getNewGroup();

        // Expect response status 200
        resultActions = createGroupRest(group);
        resultActions.andExpect(status().isCreated()).andExpect(content().contentType(CONTENT_TYPE));

        // Expect response status 200
        resultActions = createGroupRest(group);
        resultActions.andExpect(status().isCreated()).andExpect(content().contentType(CONTENT_TYPE));

        deleteAllGroups();
    }

    @Test
    public void testSaveProject() throws Exception {
        Group group = createGroup();
        group.setName(GROUP_NAME_TWO);

        ResultActions resultActions = updateGroupRest(group);
        resultActions.andExpect(status().isNoContent()).andExpect(content().contentType(CONTENT_TYPE));

        assertEquals(GROUP_NAME_TWO, getGroup(group.getId()).getName());
    }

    @Test
    public void testDeleteProject() throws Exception {
        Group group = createGroup();
        ResultActions resultActions = deleteGroupRest(group.getId());
        resultActions.andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE));
    }

    @Test
    public void testGetGroupsByProjectId() throws Exception {
        Project project = createProject();

        Group group = createGroup();
        group.setProjectId(project.getId());
        createGroupRest(group);
        createGroupRest(group);
        createGroupRest(group);

        ResultActions resultActions = getGroupsAllRest(project.getId());
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].projectId", is(project.getId())))
                .andExpect(jsonPath("$.[1].projectId", is(project.getId())))
                .andExpect(jsonPath("$.[2].projectId", is(project.getId())));

        deleteProject(project);
        deleteAllGroups();
    }


    private ResultActions getGroupsAllRest(String projectId) throws Exception {
        String endpoint = ENDPOINT_GROUP.replace(PROJECT_ID, projectId);
        return this.mockMvc.perform(get(endpoint).accept(MediaType.parseMediaType(CONTENT_TYPE)));
    }

    private ResultActions getGroupRest(String groupId) throws Exception {
        return this.mockMvc.perform(get(ENDPOINT_GROUP + groupId).accept(MediaType.parseMediaType(CONTENT_TYPE)));
    }

    private ResultActions createGroupRest(Group group) throws Exception {
        String groupJson = new ObjectMapper(new JsonFactory()).writeValueAsString(group);
        return this.mockMvc.perform(post(ENDPOINT_GROUP).contentType(MediaType.parseMediaType(CONTENT_TYPE)).content(groupJson));
    }

    private ResultActions updateGroupRest(Group group) throws Exception {
        String groupJson = new ObjectMapper(new JsonFactory()).writeValueAsString(group);
        return this.mockMvc.perform(put(ENDPOINT_GROUP + group.getId()).contentType(MediaType.parseMediaType(CONTENT_TYPE)).content(groupJson));
    }

    private ResultActions deleteGroupRest(String groupId) throws Exception {
        return this.mockMvc.perform(delete(ENDPOINT_GROUP + groupId).contentType(MediaType.parseMediaType(CONTENT_TYPE)));
    }
}

package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
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
 * @author Dmitry Ryazanov
 */
public class GroupControllerTest extends AbstractControllerTest {
    private static final String ENDPOINT_TEMPLATE = AbstractController.API_PATH + "/projects/%s/groups/%s";
    private static final String ENDPOINT_GROUP = String.format(ENDPOINT_TEMPLATE, PROJECT_ID, "");
    private static final String GROUP_NAME_UPDATED = "ABC group";


    @Test
    public void testGetGroup() throws Exception {
        Group group = createGroup();

        ResultActions resultActions = getGroupRest(group.getId());
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
            .andExpect(jsonPath("$.id").value(group.getId()))
            .andExpect(jsonPath("$.name").value(GROUP_NAME_TEST))
            .andExpect(jsonPath("$.recording").value(true));

        deleteGroup(group);
    }

    @Test
    public void testGetGroupIdIsNull() throws Exception {
        ResultActions resultActions = getGroupRest(null);
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Group with ID 'null' not found"));
    }

    @Test
    public void testGetGroupNotFound() throws Exception {
        ResultActions resultActions = getGroupRest(GROUP_ID);
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Group with ID '" + GROUP_ID + "' not found"));
    }

    @Test
    public void testAddGroupWithEmptyName() throws Exception {
        final ResultActions resultActions = createGroupRest(
                getGroupBuilder().name("").build()
        );

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("name: may not be null or empty"));
    }

    @Test
    public void testAddGroupWithNullName() throws Exception {
        final ResultActions resultActions = createGroupRest(
                getGroupBuilder().name(null).build()
        );

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("name: may not be null or empty"));
    }

    @Test
    public void testAddGroup() throws Exception {
        final Group group = getGroupBuilder().build();

        // Expect response status 200
        callAddGroup(group);
        // Expect response status 200
        callAddGroup(group);

        deleteAllGroups();
    }

    private void callAddGroup(final Group group) throws Exception {
        final ResultActions resultActions1 = createGroupRest(group);
        resultActions1.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(not(group.getId())));
    }

    @Test
    public void testSaveGroup() throws Exception {
        final Group group = createGroup();
        final Group groupToUpdate = getGroupBuilder().id(group.getId()).name(GROUP_NAME_UPDATED).build();
        final ResultActions resultActions = updateGroupRest(groupToUpdate);

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(group.getId()));

        assertEquals(GROUP_NAME_UPDATED, getGroup(group.getId()).getName());
    }

    @Test
    public void testSaveGroupWithEmptyName() throws Exception {
        final ResultActions resultActions = updateGroupRest(
                getGroupBuilder().name("").build()
        );

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("name: may not be null or empty"));
    }

    @Test
    public void testSaveGroupWithNullName() throws Exception {
        final ResultActions resultActions = updateGroupRest(
                getGroupBuilder().name(null).build()
        );

        resultActions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("name: may not be null or empty"));
    }

    @Test
    public void testDeleteGroup() throws Exception {
        Group group = createGroup();
        ResultActions resultActions = deleteGroupRest(group.getId());
        resultActions.andExpect(status().isNoContent()).andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
    }

    @Test
    public void testGetNoGroupsByProjectId() throws Exception {
        deleteAllGroups();
        Project project = createProject(true);
        ResultActions resultActions = getGroupsAllRest(project.getId());
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));

        deleteProject(project);
    }

    @Test
    public void testGetGroupsByProjectId() throws Exception {
        final Project project = createProject(true);
        final Group group = getGroupBuilder().projectId(project.getId()).build();

        createGroupRest(group);
        createGroupRest(group);
        createGroupRest(group);

        final ResultActions resultActions = getGroupsAllRest(project.getId());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].projectId", is(project.getId())))
                .andExpect(jsonPath("$.[1].projectId", is(project.getId())))
                .andExpect(jsonPath("$.[2].projectId", is(project.getId())));

        deleteProject(project);
        deleteAllGroups();
    }


    private ResultActions getGroupsAllRest(String projectId) throws Exception {
        String endpoint = String.format(ENDPOINT_TEMPLATE, projectId, "");
        return this.mockMvc.perform(get(endpoint).accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ResultActions getGroupRest(String groupId) throws Exception {
        String endpoint = String.format(ENDPOINT_TEMPLATE, PROJECT_ID, groupId);
        return this.mockMvc.perform(get(endpoint).accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ResultActions createGroupRest(Group group) throws Exception {
        String groupJson = new ObjectMapper(new JsonFactory()).writeValueAsString(group);
        return this.mockMvc.perform(post(ENDPOINT_GROUP).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)).content(groupJson));
    }

    private ResultActions updateGroupRest(Group group) throws Exception {
        String groupJson = new ObjectMapper(new JsonFactory()).writeValueAsString(group);
        String endpoint = String.format(ENDPOINT_TEMPLATE, PROJECT_ID, group.getId());
        return this.mockMvc.perform(put(endpoint).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)).content(groupJson));
    }

    private ResultActions deleteGroupRest(String groupId) throws Exception {
        String endpoint = String.format(ENDPOINT_TEMPLATE, PROJECT_ID, groupId);
        return this.mockMvc.perform(delete(endpoint).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }
}

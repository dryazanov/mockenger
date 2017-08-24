package org.mockenger.core.web.controller.endpoint;

import org.junit.Test;
import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.parseMediaType;
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
        final Group group = createGroup();
        final ResultActions resultActions = getGroupRest(group.getCode());

        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
            .andExpect(jsonPath("$.id").value(group.getId()))
            .andExpect(jsonPath("$.name").value(GROUP_NAME_TEST))
            .andExpect(jsonPath("$.recording").value(true));

        deleteGroup(group);
    }


    @Test
    public void testGetGroupIdIsNull() throws Exception {
        final ResultActions resultActions = getGroupRest(null);

        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Group with ID 'null' not found"));
    }


    @Test
    public void testGetGroupNotFound() throws Exception {
        final ResultActions resultActions = getGroupRest(GROUP_CODE);

        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("Group with ID '" + GROUP_CODE + "' not found"));
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

		final Group groupWithUniqueCode = getGroupBuilder().code(GROUP_CODE + new Date().getTime()).build();
        // Expect response status 200
        callAddGroup(groupWithUniqueCode);

		// Expect response status 400
		callAddGroupWithSameCode(groupWithUniqueCode);

        deleteAllGroups();
    }


    private void callAddGroup(final Group group) throws Exception {
        final ResultActions resultActions1 = createGroupRest(group);

        resultActions1.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(not(group.getId())));
    }


	private void callAddGroupWithSameCode(final Group group) throws Exception {
		final ResultActions resultActions1 = createGroupRest(group);

		resultActions1.andExpect(status().isBadRequest())
				.andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
				.andExpect(jsonPath("$.errors[0]").value(String.format("Group with the code '%s' already exists", group.getCode())));
	}


    @Test
    public void testSaveGroup() throws Exception {
        final Group group = createGroup();
        final Group groupToUpdate = getGroupBuilder()
				.id(group.getId())
				.code(group.getCode())
				.name(GROUP_NAME_UPDATED)
				.build();

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
        final Group group = createGroup();
        final ResultActions resultActions = deleteGroupRest(group.getCode());

        resultActions.andExpect(status().isNoContent()).andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
    }


    @Test
    public void testGetNoGroupsByProjectId() throws Exception {
        deleteAllGroups();

        final Project project = createProject(true);
        final ResultActions resultActions = getGroupsAllRest(project.getCode());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));

        deleteProject(project);
    }


    @Test
    public void testGetGroupsByProjectId() throws Exception {
        final Project project = createProject(true);

		IntStream.range(1, 4).forEach(i -> {
        	final Group group = getGroupBuilder(true)
					.projectId(project.getId())
					.build();

			try {
				createGroupRest(group);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

        final ResultActions resultActions = getGroupsAllRest(project.getCode());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].projectId", is(project.getId())))
                .andExpect(jsonPath("$.[1].projectId", is(project.getId())))
                .andExpect(jsonPath("$.[2].projectId", is(project.getId())));

        deleteProject(project);
        deleteAllGroups();
    }


    private ResultActions getGroupsAllRest(final String projectCode) throws Exception {
        final String endpoint = String.format(ENDPOINT_TEMPLATE, projectCode, "");

        return this.mockMvc.perform(
        		get(endpoint)
						.accept(parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }


    private ResultActions getGroupRest(final String groupCode) throws Exception {
        final String endpoint = String.format(ENDPOINT_TEMPLATE, PROJECT_CODE, groupCode);

        return this.mockMvc.perform(
        		get(endpoint)
						.accept(parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }


    private ResultActions createGroupRest(final Group group) throws Exception {
        final String groupJson = objectMapper.writeValueAsString(group);

        return this.mockMvc.perform(
        		post(ENDPOINT_GROUP)
						.contentType(parseMediaType(CONTENT_TYPE_JSON_UTF8))
						.content(groupJson));
    }


    private ResultActions updateGroupRest(final Group group) throws Exception {
		final String groupJson = objectMapper.writeValueAsString(group);
        final String endpoint = String.format(ENDPOINT_TEMPLATE, PROJECT_CODE, group.getCode());

        return this.mockMvc.perform(
        		put(endpoint)
						.contentType(parseMediaType(CONTENT_TYPE_JSON_UTF8))
						.content(groupJson));
    }


    private ResultActions deleteGroupRest(final String groupCode) throws Exception {
        final String endpoint = String.format(ENDPOINT_TEMPLATE, PROJECT_CODE, groupCode);

        return this.mockMvc.perform(
        		delete(endpoint)
						.contentType(parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }
}

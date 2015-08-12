package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.core.config.TestContext;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
public class RequestControllerTest extends AbstractControllerTest {

    private static final String ENDPOINT_TEMPLATE = "/projects/%s/groups/%s/requests/%s";
    private static final String ENDPOINT_REQUEST = String.format(ENDPOINT_TEMPLATE, PROJECT_ID, GROUP_ID, "");
    private static final String REQUEST_NAME_UPDATED = "ABC mock-request";

    private Project project;
    private Group group;
    private AbstractRequest request;

    @Before
    public void setup() {
        super.setup();

        project = createProject();
        group = createGroup();
        request = createRequest(group.getId());
    }

    @After
    public void cleanup() {
        deleteProject(project);
        deleteGroup(group);
        deleteRequest(request);
    }


    @Test
    public void testGetRequest() throws Exception {
        // Happy flow
        testGetRequestOk(project.getId(), group.getId(), request);

        // Not found, because requestId is null
        testGetRequestNotOk(project.getId(), group.getId());

        // Not found, because requestId is incorrect
        testGetRequestNotOk(project.getId(), group.getId(), REQUEST_ID);
    }

    private void testGetRequestOk(String projectId, String groupId, AbstractRequest request) throws Exception {
        ResultActions resultActions = getRequestRest(projectId, groupId, request.getId());
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.name").value(REQUEST_NAME_TEST))
                .andExpect(jsonPath("$.groupId").value(groupId))
                .andExpect(jsonPath("$.method").value(RequestMethod.POST.name()))
                .andExpect(jsonPath("$.creationDate", notNullValue()))
                .andExpect(jsonPath("$.path.value").value(REQUEST_PATH))
                .andExpect(jsonPath("$.path.transformers", hasSize(2)))
                .andExpect(jsonPath("$.headers.values", notNullValue()))
                .andExpect(jsonPath("$.headers.transformers", hasSize(1)))
                .andExpect(jsonPath("$.parameters.values", notNullValue()))
                .andExpect(jsonPath("$.parameters.transformers", hasSize(1)))
                .andExpect(jsonPath("$.body.value").value(MOCK_REQUEST_BODY))
                .andExpect(jsonPath("$.body.transformers", hasSize(2)))
                .andExpect(jsonPath("$.checkSum").value(CommonUtils.getCheckSum(request)))
                .andExpect(jsonPath("$.mockResponse.httpStatus").value(200))
                .andExpect(jsonPath("$.mockResponse.headers", notNullValue()))
                .andExpect(jsonPath("$.mockResponse.body").value(MOCK_RESPONSE_BODY));
    }

    private void testGetRequestNotOk(String projectId, String groupId) throws Exception {
        ResultActions resultActions = getRequestRest(projectId, groupId, null);
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("MockRequest with ID 'null' not found"));
    }

    private void testGetRequestNotOk(String projectId, String groupId, String requestId) throws Exception {
        ResultActions resultActions = getRequestRest(projectId, groupId, requestId);
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("MockRequest with ID '" + REQUEST_ID + "' not found"));
    }

    @Test
    public void testAddRequest() throws Exception {
        ResultActions resultActions = null;

        // Expect response status 200
        resultActions = createRequestRest(project.getId(), group.getId(), request);
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(not(request.getId())));

        // Expect response status 200
        resultActions = createRequestRest(project.getId(), group.getId(), request);
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(not(request.getId())));
    }

    @Test
    public void testAddRequestWithInvalidData() throws Exception {
        AbstractRequest request = getNewRequest(group.getId());
        Helper.help(this.mockMvc, project, group, request, new AddRunner());

        // Cleanup
        deleteRequest(request);
    }

    @Test
    public void testSaveRequest() throws Exception {
        request.setName(REQUEST_NAME_UPDATED);

        // Expect response status 200
        ResultActions resultActions = updateRequestRest(project.getId(), group.getId(), request);
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(request.getId()));

        assertEquals(REQUEST_NAME_UPDATED, getRequest(request.getId()).getName());
    }

    @Test
    public void testSaveRequestWithInvalidData() throws Exception {
        AbstractRequest request = getNewRequest(group.getId());
        Helper.help(this.mockMvc, project, group, request, new SaveRunner());

        // Cleanup
        deleteRequest(request);
    }

    @Test
    public void testDeleteRequest() throws Exception {
        ResultActions resultActions = deleteRequestRest(project.getId(), group.getId(), request.getId());
        resultActions.andExpect(status().isNoContent()).andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
        assertNull(getRequest(request.getId()));
    }

    /*@Test
    public void testGetNoRequestsByGroupId() throws Exception {
        // Cleanup first
        deleteAllRequests();

        ResultActions resultActions = getRequestsAllRest(project.getId(), group.getId());
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.rowCount", is(0)))
                .andExpect(jsonPath("$.rows", hasSize(0)));
    }*/

    /*@Test
    public void testGetRequestsByGroupId() throws Exception {
        // Cleanup first
        deleteAllRequests();

        createRequestRest(project.getId(), group.getId(), getNewRequest(group.getId()));
        createRequestRest(project.getId(), group.getId(), getNewRequest(group.getId()));
        createRequestRest(project.getId(), group.getId(), getNewRequest(group.getId()));

        ResultActions resultActions = getRequestsAllRest(project.getId(), group.getId());
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.rowCount", is(3)))
                .andExpect(jsonPath("$.rows", hasSize(3)));

        deleteAllRequests();
    }*/



    private ResultActions getRequestsAllRest(String projectId, String groupId) throws Exception {
        String endpoint = String.format(ENDPOINT_TEMPLATE, projectId, groupId, "");
        return this.mockMvc.perform(get(endpoint).accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ResultActions getRequestRest(String projectId, String groupId, String requestId) throws Exception {
        String endpoint = String.format(ENDPOINT_TEMPLATE, projectId, groupId, requestId);
        return this.mockMvc.perform(get(endpoint).accept(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }

    private ResultActions createRequestRest(String projectId, String groupId, AbstractRequest request) throws Exception {
        String requestJson = new ObjectMapper(new JsonFactory()).writeValueAsString(request);
        String endpoint = String.format(ENDPOINT_TEMPLATE, projectId, groupId, "");
        return this.mockMvc.perform(post(endpoint).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)).content(requestJson));
    }

    private ResultActions updateRequestRest(String projectId, String groupId, AbstractRequest request) throws Exception {
        String requestJson = new ObjectMapper(new JsonFactory()).writeValueAsString(request);
        String endpoint = String.format(ENDPOINT_TEMPLATE, projectId, groupId, request.getId());
        return this.mockMvc.perform(put(endpoint).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)).content(requestJson));
    }

    private ResultActions deleteRequestRest(String projectId, String groupId, String requestId) throws Exception {
        String endpoint = String.format(ENDPOINT_TEMPLATE, projectId, groupId, requestId);
        return this.mockMvc.perform(delete(endpoint).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)));
    }



    private interface Runner {
        void run(MockMvc mockMvc, String pId, String gId, AbstractRequest request, String msg) throws Exception;
    }

    private static class AddRunner implements Runner {
        @Override
        public void run(MockMvc mockMvc, String projectId, String groupId, AbstractRequest request, String expectedErrorMsg) throws Exception {
            String requestJson = new ObjectMapper(new JsonFactory()).writeValueAsString(request);
            String endpoint = String.format(ENDPOINT_TEMPLATE, projectId, groupId, "");
            ResultActions resultActions = mockMvc.perform(post(endpoint).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)).content(requestJson));
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0]").value(expectedErrorMsg));
        }
    }

    private static class SaveRunner implements Runner {
        @Override
        public void run(MockMvc mockMvc, String projectId, String groupId, AbstractRequest request, String expectedErrorMsg) throws Exception {
            String requestJson = new ObjectMapper(new JsonFactory()).writeValueAsString(request);
            String endpoint = String.format(ENDPOINT_TEMPLATE, projectId, groupId, request.getId());
            ResultActions resultActions = mockMvc.perform(put(endpoint).contentType(MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8)).content(requestJson));
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0]").value(expectedErrorMsg));
        }
    }

    private static class Helper {
        static void help(MockMvc mockMvc, Project project, Group group, AbstractRequest request, Runner runner) throws Exception {
            // Empty name
            request.setName("");
            runner.run(mockMvc, project.getId(), group.getId(), request, "name: may not be null or empty");

            // Name is null
            request.setName(null);
            runner.run(mockMvc, project.getId(), group.getId(), request, "name: may not be null or empty");

            // Name is null
            request = getNewRequest(group.getId());
            request.setGroupId(null);
            runner.run(mockMvc, project.getId(), group.getId(), request, "groupId: may not be null");

            // Empty checksum
            request = getNewRequest(group.getId());
            request.setCheckSum(null);
            runner.run(mockMvc, project.getId(), group.getId(), request, "checkSum: may not be null or empty");

            // httpStatus is zero
            request = getNewRequest(group.getId());
            request.getMockResponse().setHttpStatus(0);
            runner.run(mockMvc, project.getId(), group.getId(), request, "httpStatus: must be number greater than zero");
        }
    }
}

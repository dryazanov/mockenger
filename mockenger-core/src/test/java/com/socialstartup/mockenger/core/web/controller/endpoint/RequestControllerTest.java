package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
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
 * @author Dmitry Ryazanov
 */
public class RequestControllerTest extends AbstractControllerTest {

    private static final String ENDPOINT_TEMPLATE = AbstractController.API_PATH + "/projects/%s/groups/%s/requests/%s";
    private static final String ENDPOINT_REQUEST = String.format(ENDPOINT_TEMPLATE, PROJECT_ID, GROUP_ID, "");
    private static final String REQUEST_NAME_UPDATED = "ABC mock-request";

    private Project project;
    private Group group;
    private AbstractRequest request;

    @Before
    public void setUp() {
        super.setUp();

        project = createProject(true);
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
    public void testAddAndSequenceGenerator() throws Exception {
        deleteAllRequests();

        final int numOfMocks = 99;
        final int numOfThreadToRun = numOfMocks / ThreadLocalRandom.current().nextInt(1, 25);
        final ExecutorService taskExecutor = Executors.newFixedThreadPool(numOfThreadToRun);

        for (int i = 0; i < numOfMocks; i++) {
            taskExecutor.execute(new RestRequestSender(project.getId(), getNewRequest(group.getId())));
        }

        // Disable new tasks from being submitted
        taskExecutor.shutdown();

        try {
            // Wait a while for existing tasks to terminate
            taskExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Task has been interrupted: " + e.getMessage());
            taskExecutor.shutdownNow();
        }

        Set<String> uniqueCodesSet = new HashSet<>(numOfMocks);
        for (AbstractRequest abstractRequest : getAllRequests()) {
            uniqueCodesSet.add(abstractRequest.getUniqueCode());
        }

        assertEquals(numOfMocks, uniqueCodesSet.size());
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
        ExtendedHelper.help(this.mockMvc, project, group, request, new SaveRunner());

        // Cleanup
        deleteRequest(request);
    }

    @Test
    public void testDeleteRequest() throws Exception {
        ResultActions resultActions = deleteRequestRest(project.getId(), group.getId(), request.getId());
        resultActions.andExpect(status().isNoContent()).andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
        assertNull(getRequest(request.getId()));
    }

    @Test
    public void testGetNoRequestsByGroupId() throws Exception {
        // Cleanup first
        deleteAllRequests();

        ResultActions resultActions = getRequestsAllRest(project.getId(), group.getId());
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetRequestsByGroupId() throws Exception {
        // Cleanup first
        deleteAllRequests();

        createRequestRest(project.getId(), group.getId(), getNewRequest(group.getId()));
        createRequestRest(project.getId(), group.getId(), getNewRequest(group.getId()));
        createRequestRest(project.getId(), group.getId(), getNewRequest(group.getId()));

        ResultActions resultActions = getRequestsAllRest(project.getId(), group.getId());
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].groupId", is(group.getId())))
                .andExpect(jsonPath("$.[1].groupId", is(group.getId())))
                .andExpect(jsonPath("$.[2].groupId", is(group.getId())));
    }



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
        void run(MockMvc mockMvc, String projectId, String groupId, AbstractRequest request, String msg) throws Exception;
    }

    private final static class AddRunner implements Runner {
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

    private final static class SaveRunner implements Runner {
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

    private final static class Helper {
        private static void help(MockMvc mockMvc, Project project, Group group, AbstractRequest request, Runner runner) throws Exception {
            // Empty name
            request.setName("");
            runner.run(mockMvc, project.getId(), group.getId(), request, "name: may not be null or empty");

            // Name is null
            request.setName(null);
            runner.run(mockMvc, project.getId(), group.getId(), request, "name: may not be null or empty");

            // GroupId is null
            AbstractRequest request1 = getNewRequest(group.getId());
            request1.setGroupId(null);
            runner.run(mockMvc, project.getId(), group.getId(), request1, "groupId: may not be null");

            // httpStatus in the response is zero
            AbstractRequest request2 = getNewRequest(group.getId());
            request2.getMockResponse().setHttpStatus(0);
            runner.run(mockMvc, project.getId(), group.getId(), request2, "httpStatus: must be number greater than zero");
        }
    }

    private final static class ExtendedHelper {
        private static void help(MockMvc mockMvc, Project project, Group group, AbstractRequest request, Runner runner) throws Exception {
            Helper.help(mockMvc, project, group, request, runner);

            // Wrong unique code (for save request only)
            String uniqueWrongCode = "WRONG-1";
            AbstractRequest request1 = getNewRequest(group.getId());
            request1.setUniqueCode(uniqueWrongCode);
            runner.run(mockMvc, project.getId(), group.getId(), request1, String.format("Cannot find MockRequest with ID '%s' and unique code '%s'", request1.getId(), uniqueWrongCode));
        }
    }

    private final class RestRequestSender implements Runnable {

        private final String projectId;

        private final AbstractRequest requestToSend;

        public RestRequestSender(String projectId, AbstractRequest request) {
            this.projectId = projectId;
            this.requestToSend = request;
        }

        @Override
        public void run() {
            try {
                ResultActions resultActions = createRequestRest(this.projectId, requestToSend.getGroupId(), requestToSend);
                resultActions.andExpect(status().isOk());

                // Use lines below for debug
//                String responseJson = resultActions.andReturn().getResponse().getContentAsString();
//                AbstractRequest mock = new ObjectMapper(new JsonFactory()).readValue(responseJson, AbstractRequest.class);
//                System.out.println("REQUEST UNIQUE CODE ID :: " + mock.getUniqueCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

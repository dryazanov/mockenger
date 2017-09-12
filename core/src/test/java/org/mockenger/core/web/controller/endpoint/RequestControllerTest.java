package org.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

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
        testGetRequestOk(project.getCode(), group, request);

        // Not found, because requestId is null
        testGetRequestNotOk(project.getCode(), group.getCode());

        // Not found, because requestId is incorrect
        testGetRequestNotOk(project.getCode(), group.getCode(), REQUEST_CODE);
    }

    private void testGetRequestOk(final String projectCode, final Group group, final AbstractRequest request) throws Exception {
        final ResultActions resultActions = getRequestRest(projectCode, group.getCode(), request.getCode());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.name").value(REQUEST_NAME_TEST))
                .andExpect(jsonPath("$.groupId").value(group.getId()))
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

    private void testGetRequestNotOk(final String projectCode, final String groupCode) throws Exception {
        final ResultActions resultActions = getRequestRest(projectCode, groupCode, null);

        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("MockRequest with ID 'null' not found"));
    }

    private void testGetRequestNotOk(final String projectCode, final String groupCode, final String requestCode) throws Exception {
        final ResultActions resultActions = getRequestRest(projectCode, groupCode, requestCode);

        resultActions.andExpect(status().isNotFound())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]").value("MockRequest with ID '" + REQUEST_CODE + "' not found"));
    }

    @Test
    public void testAddRequest() throws Exception {
        ResultActions resultActions;

        // Expect response status 201
        resultActions = createRequestRest(project.getCode(), group.getCode(), request);
        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(not(request.getId())));

        // Expect response status 201
        resultActions = createRequestRest(project.getCode(), group.getCode(), request);
        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(not(request.getId())));
    }

    @Test
    public void testAddRequestWithInvalidData() throws Exception {
        AbstractRequest request = getNewRequest(group.getId());
        Helper.help(project, group, request, new AddRunner());

        // Cleanup
        deleteRequest(request);
    }

    @Test
    public void testAddAndSequenceGenerator() throws Exception {
        deleteAllRequests();

        final int numOfMocks = 25;
        final int numOfThreadToRun = numOfMocks / ThreadLocalRandom.current().nextInt(1, 5);

		final AbstractRequest request = getNewRequest(group.getId());
		final ExecutorService taskExecutor = Executors.newFixedThreadPool(numOfThreadToRun);

		IntStream.range(0, numOfMocks).forEach(i -> taskExecutor.execute(() -> {
			try {
				final ResultActions resultActions = createRequestRest(project.getCode(), group.getCode(), request);
				resultActions.andExpect(status().isOk());

				// For debug
                /*
                String responseJson = resultActions.andReturn().getResponse().getContentAsString();
                AbstractRequest mock = new ObjectMapper(new JsonFactory()).readValue(responseJson, AbstractRequest.class);
                System.out.println("REQUEST UNIQUE CODE ID :: " + mock.getUniqueCode());
                */
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));

        // Disable new tasks from being submitted
        taskExecutor.shutdown();

        try {
            // Wait a while for existing tasks to terminate
            taskExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Task has been interrupted: " + e.getMessage());
            taskExecutor.shutdownNow();
        }

        final int size = StreamSupport.stream(getAllRequests().spliterator(), true)
				.map(AbstractRequest::getCode)
				.collect(Collectors.toSet())
				.size();

        assertEquals(numOfMocks, size);
    }


    @Test
    public void testSaveRequest() throws Exception {
        request.setName(REQUEST_NAME_UPDATED);

        // Expect response status 200
        final ResultActions resultActions = updateRequestRest(project.getCode(), group.getCode(), request);

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(request.getId()));

        assertEquals(REQUEST_NAME_UPDATED, getRequest(request.getId()).getName());
    }

    @Test
    public void testSaveRequestWithInvalidData() throws Exception {
        final AbstractRequest request = createRequest(group.getId());

        Helper.help(project, group, request, new SaveRunner());

        // Cleanup
        deleteRequest(request);
    }

    @Test
    public void testDeleteRequest() throws Exception {
        final ResultActions resultActions = deleteRequestRest(project.getCode(), group.getCode(), request.getCode());

        resultActions.andExpect(status().isNoContent())
				.andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));

        assertNull(getRequest(request.getId()));
    }

    @Test
    public void testGetNoRequestsByGroupId() throws Exception {
        // Cleanup first
        deleteAllRequests();

        final ResultActions resultActions = getRequestsAllRest(project.getCode(), group.getCode());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetRequestsByGroupId() throws Exception {
        // Cleanup first
        deleteAllRequests();

        IntStream.range(1, 4).forEach(i -> {
			try {
				createRequestRest(project.getCode(), group.getCode(), getNewRequest(group.getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

        final ResultActions resultActions = getRequestsAllRest(project.getCode(), group.getCode());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].groupId", is(group.getId())))
                .andExpect(jsonPath("$.[1].groupId", is(group.getId())))
                .andExpect(jsonPath("$.[2].groupId", is(group.getId())));
    }



    private ResultActions getRequestsAllRest(final String projectCode, final String groupCode) throws Exception {
		final String endpoint = createEndpoint(projectCode, groupCode, "");

        return mockMvc.perform(withMediaType(get(endpoint)));
    }

    private ResultActions getRequestRest(final String projectCode, final String groupCode, final String requestCode) throws Exception {
        final String endpoint = createEndpoint(projectCode, groupCode, requestCode);

        return mockMvc.perform(withMediaType(get(endpoint)));
    }

    private ResultActions createRequestRest(final String projectCode, final String groupCode, final AbstractRequest request) throws Exception {
        final String requestJson = createRequestJson(request);
        final String endpoint = createEndpoint(projectCode, groupCode, "");

        return mockMvc.perform(withMediaType(post(endpoint)).content(requestJson));
    }

    private ResultActions updateRequestRest(final String projectCode, final String groupCode, final AbstractRequest request) throws Exception {
        final String requestJson = createRequestJson(request);
        final String endpoint = createEndpoint(projectCode, groupCode, request.getCode());

        return this.mockMvc.perform(withMediaType(put(endpoint)).content(requestJson));
    }

    private ResultActions deleteRequestRest(final String projectCode, final String groupCode, final String requestCode) throws Exception {
        final String endpoint = createEndpoint(projectCode, groupCode, requestCode);

        return mockMvc.perform(withMediaType(delete(endpoint)));
    }

	private String createRequestJson(final AbstractRequest request) throws JsonProcessingException {
		return objectMapper.writeValueAsString(request);
	}

	private String createEndpoint(final String projectCode, final String groupCode, final String requestCode) {
		return String.format(ENDPOINT_TEMPLATE, projectCode, groupCode, requestCode);
	}


    private interface Runner {
        void run(String projectId, String groupId, AbstractRequest request, String msg) throws Exception;
    }

    private abstract class AbstractRunner {
		protected void run(final MockHttpServletRequestBuilder builder, final String requestJson, final String expectedErrorMsg) throws Exception {
			mockMvc.perform(builder.content(requestJson))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
					.andExpect(jsonPath("$.errors", hasSize(1)))
					.andExpect(jsonPath("$.errors[0]").value(expectedErrorMsg));
		}
	}

    private final class AddRunner extends AbstractRunner implements Runner {
        @Override
        public void run(final String projectCode, final String groupCode, final AbstractRequest request, final String msg) throws Exception {
			final String postEndpoint = createEndpoint(projectCode, groupCode, "");

			run(withMediaType(post(postEndpoint)), createRequestJson(request), msg);
        }
    }

    private final class SaveRunner extends AbstractRunner implements Runner {
        @Override
        public void run(final String projectCode, final String groupCode, final AbstractRequest request, final String msg) throws Exception {
			final String putEndpoint = createEndpoint(projectCode, groupCode, request.getCode());

			run(withMediaType(put(putEndpoint)), createRequestJson(request), msg);
        }
    }

    private final static class Helper {
        private static void help(final Project project, final Group group, final AbstractRequest request, final Runner runner) throws Exception {
            // Empty name
            request.setName("");
            runner.run(project.getCode(), group.getCode(), request, "name: may not be null or empty");

            // Name is null
            request.setName(null);
            runner.run(project.getCode(), group.getCode(), request, "name: may not be null or empty");

            // GroupId is null
            final AbstractRequest request1 = getNewRequest(group.getId());
            request1.setGroupId(null);
            runner.run(project.getCode(), group.getCode(), request1, "groupId: may not be null");

            // httpStatus in the response is zero
            final AbstractRequest request2 = getNewRequest(group.getId());
            request2.getMockResponse().setHttpStatus(0);
            runner.run(project.getCode(), group.getCode(), request2, "httpStatus: must be number greater than zero");
        }
    }
}

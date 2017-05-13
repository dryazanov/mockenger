package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.DeleteRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GetRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PostRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PutRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import com.socialstartup.mockenger.data.model.persistent.transformer.KeyValueTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.RegexpTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.Transformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.socialstartup.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * @author Dmitry Ryazanov
 */
public class RestfullControllerTest extends AbstractControllerTest {

    private static final String ENDPOINT_TEMPLATE = AbstractController.API_PATH + "/REST/%s/%s";
    private static final String REQUEST_PATH = "test/rest/mock/request";
    private static final String ID1 = "200000000001";
    private static final String ID2 = "100000000002";
	private static final String VALUE1 = "V1";
    private static final String VALUE2 = "V2";
    private static final String EXPECTED_RESULT_OK = "OK";

    protected static final String REST_JSON_REQUEST_BODY = "{\"id\":" + ID1 + ",\"dynamicValue\":\"" + VALUE1 + "\",\"name\":\"NAME\",\"type\":\"TYPE\"}";
    protected static final String REST_JSON_RESPONSE_BODY = "{\"result\":\"OK\"}";
    protected static final String REST_BAD_JSON_REQUEST = "{\"json\":\"is\",\"bad\"}";

    protected static final String REST_XML_REQUEST_BODY = new StringBuilder()
            .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            .append("<note>")
            .append("<id>").append(ID1).append("</id>")
            .append("<dynamicValue>").append(VALUE1).append("</dynamicValue>")
            .append("<to>Tove</to>")
            .append("<from>Jani</from>")
            .append("<heading>Reminder</heading>")
            .append("<body>Don't forget me this weekend!</body>")
            .append("</note>")
            .toString();

    protected static final String REST_XML_RESPONSE_BODY = new StringBuilder()
            .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            .append("<note>")
            .append("<result>OK</result>")
            .append("</note>")
            .toString();

    private Project project;
    private Group group;
    private Group groupWithRecording;
    private String endpoint;
    private String endpointWithRecording;


    @Before
    public void setUp() {
        super.setUp();

        project = createProject(true);
        group = createGroup(false);
        groupWithRecording = createGroup(project.getId(), true);

        endpoint = String.format(ENDPOINT_TEMPLATE, group.getId(), REQUEST_PATH);
        endpointWithRecording = String.format(ENDPOINT_TEMPLATE, groupWithRecording.getId(), REQUEST_PATH);
    }

    @After
    public void cleanup() {
        deleteProject(project);
        deleteAllGroups();
        deleteAllRequests();
    }

    //========== POST WITH JSON =================//

    @Test
    public void testPostJsonRequestOk() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        final String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2).replace(VALUE1, VALUE2);

        createRequest(createJsonMockRequestForPost(group.getId()));

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.result").value(EXPECTED_RESULT_OK));
    }

    @Test
    public void testPostJsonRequestWithBadJson() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(REST_BAD_JSON_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Failed to create instance of the mock-object: Cannot read json from the provided source"));
    }

    @Test
    public void testPostJsonRequestNotFound() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        final String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(content)).andExpect(status().isNotFound());
    }

    @Test
    public void testPostJsonRequestNotFoundButCreated() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        final String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);
        final String postEndpoint = String.format(ENDPOINT_TEMPLATE, groupWithRecording.getId(), REQUEST_PATH);

        this.mockMvc.perform(post(postEndpoint).contentType(mediaType).content(content)).andExpect(status().isCreated());
        this.mockMvc.perform(post(postEndpoint).contentType(mediaType).content(content)).andExpect(status().isFound());
    }

    //========== POST WITH XML =================//

    @Test
    public void testPostXmlRequest() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_XML_UTF8);
        final String content = REST_XML_REQUEST_BODY.replace(ID1, ID2);

        // Prepare request to add via API
        final AbstractRequest postRequest = createXmlMockRequestForPost(groupWithRecording.getId());
        final String postEndpoint = String.format(REQUEST_PATH_API, groupWithRecording.getProjectId(), groupWithRecording.getId());

        // Send real request to API
        sendPostRequest(postEndpoint, MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8), postRequest);

        mockMvc.perform(post(endpointWithRecording).contentType(mediaType).content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(CONTENT_TYPE_XML_UTF8))
                .andExpect(xpath("/note/result").string(EXPECTED_RESULT_OK));
    }


    //========== PUT WITH JSON =================//

    @Test
    public void testPutJsonRequestOk() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        final String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);

        createRequest(createJsonMockRequestForPut(group.getId()));

        this.mockMvc.perform(put(endpoint).contentType(mediaType).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8.toLowerCase()))
                .andExpect(content().string(""));
    }

    @Test
    public void testPutJsonRequestNotFound() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        final String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);

        this.mockMvc.perform(put(endpoint).contentType(mediaType).content(content)).andExpect(status().isNotFound());
    }

    @Test
    public void testPutJsonRequestWithBadJson() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);

        this.mockMvc.perform(put(endpoint).contentType(mediaType).content(REST_BAD_JSON_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Failed to create instance of the mock-object: Cannot read json from the provided source"));
    }


    //========== PUT WITH XML =================//

    @Test
    public void testPutXmlRequest() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_XML_UTF8);
        final String content = REST_XML_REQUEST_BODY.replace(ID1, ID2);

        // Prepare request to send via API
        final AbstractRequest putRequest = createXmlMockRequestForPut(groupWithRecording.getId());
        final String postEndpoint = String.format(REQUEST_PATH_API, groupWithRecording.getProjectId(), groupWithRecording.getId());

        // Send real request to API
        sendPostRequest(postEndpoint, MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8), putRequest);

        this.mockMvc.perform(put(endpointWithRecording).contentType(mediaType).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_XML_UTF8.toLowerCase()))
                .andExpect(content().string(""));
    }


    //========== GET WITH JSON =================//

    @Test
    public void testGetJsonRequestOk() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);

        createRequest(createJsonMockRequestForGet(group.getId()));

        this.mockMvc.perform(get(endpoint).contentType(mediaType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8.toLowerCase()))
                .andExpect(jsonPath("$.result").value(EXPECTED_RESULT_OK));
    }

    @Test
    public void testGetRequestNotFound() throws Exception {
        this.mockMvc.perform(get(endpoint)).andExpect(status().isNotFound());
    }

    //========== GET WITH XML =================//

    @Test
    public void testGetXmlRequestOk() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_XML_UTF8);

        createRequest(createXmlMockRequestForGet(group.getId()));

        this.mockMvc.perform(get(endpoint).contentType(mediaType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_XML_UTF8.toLowerCase()))
                .andExpect(xpath("/note/result").string(EXPECTED_RESULT_OK));
    }

    //========== DELETE =================//

    @Test
    public void testDeleteRequestOk() throws Exception {
        createRequest(createMockRequestForDelete(group.getId()));

        this.mockMvc.perform(delete(endpoint)).andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteRequestNotFound() throws Exception {
        this.mockMvc.perform(delete(endpoint)).andExpect(status().isNotFound());
    }

    //========== POST BAD REQUEST WITH HTML =================//

    @Test
    public void testPostHtmlRequestNotOk() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_HTML_UTF8);

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(""))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors[0]").value("Invalid header 'Content-type': application/json or application/xml are only allowed in REST requests"));
    }

    //========== PUT BAD REQUEST WITH HTML =================//

    @Test
    public void testPutHtmlRequestNotOk() throws Exception {
        final MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_HTML_UTF8);

        this.mockMvc.perform(put(endpoint).contentType(mediaType).content(""))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors[0]").value("Invalid header 'Content-type': application/json or application/xml are only allowed in REST requests"));
    }


	@Test
	public void testRequestCounterUpdate() throws Exception {
		deleteAllRequests();

		final int numOfMocks = 99;
		final int numOfThreadToRun = numOfMocks / ThreadLocalRandom.current().nextInt(1, 25);

		final AbstractRequest request = createRequest(createJsonMockRequestForGet(group.getId()));
		final String mockRequestEndpoint = String.format(API_PATH + "/projects/%s/groups/%s/requests/%s",
				project.getId(), group.getId(), request.getId());
		final ExecutorService taskExecutor = Executors.newFixedThreadPool(numOfThreadToRun);

		IntStream.range(0, numOfMocks).forEach(i -> taskExecutor.execute(() -> {
			try {
				mockMvc.perform(withMediaType(get(endpoint))).andExpect(status().isOk());
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

		mockMvc.perform(withMediaType(get(mockRequestEndpoint)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.requestCounter").value(numOfMocks));
	}

	private MockHttpServletRequestBuilder withMediaType(final MockHttpServletRequestBuilder builder) {
		return builder.contentType(parseMediaType(CONTENT_TYPE_JSON_UTF8));
	}


    private void createMockRequest(final AbstractRequest request, final String groupId, final String contentType,
                                   final String requestBody, final String responseBody) {

        final Set<Pair> headersSet = ImmutableSet.of(new Pair("content-type", contentType));

        request.setGroupId(groupId);
        request.setId(CommonUtils.generateUniqueId());
        request.setName(REQUEST_NAME_TEST);
        request.setCreationDate(new Date());
        request.setPath(new Path(REQUEST_PATH));
        request.setParameters(null);

        if (contentType != null) {
            request.setHeaders(new Headers(ImmutableList.of(new KeyValueTransformer("key", ID2, ID1)), headersSet));
        }
        if (requestBody != null) {
			final ImmutableList<Transformer> transformers = ImmutableList.of(
					new RegexpTransformer(ID2, ID1), new RegexpTransformer(VALUE2, VALUE1)
			);
			request.setBody(new Body(transformers, requestBody));
        }

        request.setMockResponse(new MockResponse(200, headersSet, responseBody));
    }

    private PostRequest createJsonMockRequestForPost(String groupId) {
        final PostRequest postRequest = new PostRequest();

        createMockRequest(postRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), REST_JSON_REQUEST_BODY, REST_JSON_RESPONSE_BODY);
        postRequest.setCheckSum(CommonUtils.getCheckSum(postRequest));
        postRequest.getMockResponse().setHttpStatus(201);
        postRequest.getMockResponse().setHeaders(null);

        return postRequest;
    }

    private PutRequest createJsonMockRequestForPut(String groupId) {
        final PutRequest putRequest = new PutRequest();

        createMockRequest(putRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), REST_JSON_REQUEST_BODY, null);
        putRequest.setCheckSum(CommonUtils.getCheckSum(putRequest));

        return putRequest;
    }

    private GetRequest createJsonMockRequestForGet(String groupId) {
        final GetRequest getRequest = new GetRequest();

        createMockRequest(getRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), null, REST_JSON_RESPONSE_BODY);
        getRequest.setCheckSum(CommonUtils.generateCheckSum(getRequest));

        return getRequest;
    }

    private PostRequest createXmlMockRequestForPost(String groupId) {
        final PostRequest postRequest = new PostRequest();

        createMockRequest(postRequest, groupId, CONTENT_TYPE_XML_UTF8.toLowerCase(), REST_XML_REQUEST_BODY, REST_XML_RESPONSE_BODY);
        postRequest.setCheckSum(CommonUtils.getCheckSum(postRequest));
        postRequest.getMockResponse().setHttpStatus(201);
        postRequest.getMockResponse().setHeaders(null);

        return postRequest;
    }

    private PutRequest createXmlMockRequestForPut(String groupId) {
        final PutRequest putRequest = new PutRequest();

        createMockRequest(putRequest, groupId, CONTENT_TYPE_XML_UTF8.toLowerCase(), REST_XML_REQUEST_BODY, null);
        putRequest.setCheckSum(CommonUtils.getCheckSum(putRequest));

        return putRequest;
    }

    private GetRequest createXmlMockRequestForGet(String groupId) {
        final GetRequest getRequest = new GetRequest();

        createMockRequest(getRequest, groupId, CONTENT_TYPE_XML_UTF8.toLowerCase(), null, REST_XML_RESPONSE_BODY);
        getRequest.setCheckSum(CommonUtils.generateCheckSum(getRequest));

        return getRequest;
    }

    private DeleteRequest createMockRequestForDelete(String groupId) {
        final DeleteRequest deleteRequest = new DeleteRequest();

        createMockRequest(deleteRequest, groupId, null, null, null);
        deleteRequest.getMockResponse().setHttpStatus(204);
        deleteRequest.getMockResponse().setHeaders(null);
        deleteRequest.setCheckSum(CommonUtils.generateCheckSum(deleteRequest));

        return deleteRequest;
    }
}

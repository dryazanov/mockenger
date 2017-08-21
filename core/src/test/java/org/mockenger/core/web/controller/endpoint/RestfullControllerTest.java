package org.mockenger.core.web.controller.endpoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockenger.core.util.CommonUtils;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GetRequest;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockenger.data.model.persistent.mock.request.PutRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.mockenger.core.util.CommonUtils.getCheckSum;
import static org.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
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
public class RestfullControllerTest extends AbstractHttpControllerTest {

    private static final String ENDPOINT_TEMPLATE = API_PATH + "/REST/%s/%s";

	private static final String READ_JSON_ERROR_MESSAGE = "Failed to create instance of the mock-object: " +
			"Cannot read json from the provided source";

	private static final String INVALID_CONTENT_TYPE_ERROR_MESSAGE = "Invalid header 'Content-type': " +
			"application/json or application/xml are only allowed in REST requests";

	private static final String MOCK_REQUEST_ENDPOINT = REQUEST_PATH_API + "/%s";


	@Before
    public void setUp() {
        super.setUp();
    }


    @After
    public void cleanup() {
        super.cleanup();
    }


    //========== POST ==========//

    @Test
    public void testPostJsonRequestOk() throws Exception {
        super.testPostJsonRequestOk();
    }


	@Test
	public void testPostJsonRequestNotFound() throws Exception {
		super.testPostJsonRequestNotFound();
	}


    @Test
    public void testPostJsonRequestWithBadJson() throws Exception {
		final MvcResult mvcResult = getMvcResult(withMediaType(post(endpoint).content(REST_BAD_JSON_REQUEST)));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value(READ_JSON_ERROR_MESSAGE));
    }


    @Test
    public void testPostJsonRequestNotFoundButCreated() throws Exception {
        final String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);
        final String postEndpoint = format(getEndpointTemplate(), groupWithRecording.getCode(), REQUEST_PATH);

		final MvcResult mvcResult1 = getMvcResult(withMediaType(post(postEndpoint).content(content)));
		mockMvc.perform(asyncDispatch(mvcResult1)).andExpect(status().isCreated());

		final MvcResult mvcResult2 = getMvcResult(withMediaType(post(postEndpoint).content(content)));
		mockMvc.perform(asyncDispatch(mvcResult2)).andExpect(status().isFound());
    }


    @Test
    public void testPostXmlRequest() throws Exception {
        // Prepare request to add via API
        final AbstractRequest postRequest = createXmlMockRequestForPost(groupWithRecording.getId());
        final String postEndpoint = format(REQUEST_PATH_API, project.getCode(), groupWithRecording.getCode());

        // Send real request to API
        sendPostRequest(postEndpoint, parseMediaType(CONTENT_TYPE_JSON_UTF8), postRequest);


        final String content = REST_XML_REQUEST_BODY.replace(ID1, ID2);
		final MvcResult mvcResult = getMvcResult(withMediaType(post(endpointWithRecording).content(content), CONTENT_TYPE_XML_UTF8));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(CONTENT_TYPE_XML_UTF8))
				.andExpect(xpath("/note/result").string(EXPECTED_RESULT_OK));
    }


    //========== PUT ==========//

    @Test
    public void testPutJsonRequestOk() throws Exception {
        super.testPutJsonRequestOk();
    }


    @Test
    public void testPutJsonRequestNotFound() throws Exception {
        super.testPutJsonRequestNotFound();
    }


    @Test
    public void testPutJsonRequestWithBadJson() throws Exception {
		final MvcResult mvcResult = getMvcResult(withMediaType(put(endpoint).content(REST_BAD_JSON_REQUEST)));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]").value(READ_JSON_ERROR_MESSAGE));
    }


    @Test
    public void testPutXmlRequest() throws Exception {
        // Prepare request to send via API
        final AbstractRequest putRequest = createXmlMockRequestForPut(groupWithRecording.getId());
        final String postEndpoint = format(REQUEST_PATH_API, project.getCode(), groupWithRecording.getCode());

        // Send real request to API
        sendPostRequest(postEndpoint, parseMediaType(CONTENT_TYPE_JSON_UTF8), putRequest);


        final String content = REST_XML_REQUEST_BODY.replace(ID1, ID2);
		final MvcResult mvcResult = getMvcResult(withMediaType(put(endpointWithRecording).content(content), CONTENT_TYPE_XML_UTF8));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().contentType(CONTENT_TYPE_XML_UTF8.toLowerCase()))
				.andExpect(content().string(""));
    }


    //========== GET ==========//

    @Test
    public void testGetJsonRequestOk() throws Exception {
        super.testGetJsonRequestOk();
    }


	@Test
    public void testGetRequestNotFound() throws Exception {
		super.testGetRequestNotFound();
    }


    @Test
    public void testGetXmlRequestOk() throws Exception {
        createRequest(createXmlMockRequestForGet(group.getId()));

		final MvcResult mvcResult = getMvcResult(withMediaType(get(endpoint), CONTENT_TYPE_XML_UTF8));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().contentType(CONTENT_TYPE_XML_UTF8.toLowerCase()))
				.andExpect(xpath("/note/result").string(EXPECTED_RESULT_OK));
    }


    //========== DELETE ==============//

    @Test
    public void testDeleteRequestOk() throws Exception {
        super.testDeleteRequestOk();
    }


    @Test
    public void testDeleteRequestNotFound() throws Exception {
		super.testDeleteRequestNotFound();
    }


    //========== POST BAD REQUEST WITH HTML =================//

    @Test
    public void testPostHtmlRequestNotOk() throws Exception {
        final MediaType mediaType = parseMediaType(CONTENT_TYPE_HTML_UTF8);

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(""))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors[0]").value(INVALID_CONTENT_TYPE_ERROR_MESSAGE));
    }


    //========== PUT BAD REQUEST WITH HTML =================//

    @Test
    public void testPutHtmlRequestNotOk() throws Exception {
        final MediaType mediaType = parseMediaType(CONTENT_TYPE_HTML_UTF8);

        this.mockMvc.perform(put(endpoint).contentType(mediaType).content(""))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors[0]").value(INVALID_CONTENT_TYPE_ERROR_MESSAGE));
    }


	@Test
	public void testRequestCounterUpdate() throws Exception {
		deleteAllRequests();

		final int numOfMocks = 25;
		final int numOfThreadToRun = numOfMocks / ThreadLocalRandom.current().nextInt(1, 5);

		final AbstractRequest request = createRequest(createJsonMockRequestForGet(group.getId()));
		final String mockRequestEndpoint = format(MOCK_REQUEST_ENDPOINT, project.getCode(), group.getCode(), request.getCode());
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

		Thread.sleep(3000);

		mockMvc.perform(withMediaType(get(mockRequestEndpoint)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.requestCounter").value(numOfMocks));
	}


	@Override
	protected String getEndpointTemplate() {
		return ENDPOINT_TEMPLATE;
	}


    private PostRequest createXmlMockRequestForPost(final String groupId) {
        final PostRequest postRequest = new PostRequest();

        createMockRequest(postRequest, groupId, CONTENT_TYPE_XML_UTF8.toLowerCase(), REST_XML_REQUEST_BODY, REST_XML_RESPONSE_BODY);
        postRequest.setCheckSum(getCheckSum(postRequest));
        postRequest.getMockResponse().setHttpStatus(201);

        return postRequest;
    }


    private PutRequest createXmlMockRequestForPut(final String groupId) {
        final PutRequest putRequest = new PutRequest();

        createMockRequest(putRequest, groupId, CONTENT_TYPE_XML_UTF8.toLowerCase(), REST_XML_REQUEST_BODY, null);
        putRequest.setCheckSum(getCheckSum(putRequest));

        return putRequest;
    }


    private GetRequest createXmlMockRequestForGet(final String groupId) {
        final GetRequest getRequest = new GetRequest();

        createMockRequest(getRequest, groupId, CONTENT_TYPE_XML_UTF8.toLowerCase(), null, REST_XML_RESPONSE_BODY);
        getRequest.setCheckSum(CommonUtils.generateCheckSum(getRequest));

        return getRequest;
    }
}

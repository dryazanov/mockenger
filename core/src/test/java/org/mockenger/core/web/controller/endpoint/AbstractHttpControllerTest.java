package org.mockenger.core.web.controller.endpoint;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.DeleteRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.request.GetRequest;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockenger.data.model.persistent.mock.request.PutRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.mockenger.data.model.persistent.mock.request.part.Headers;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
import org.mockenger.data.model.persistent.mock.request.part.Path;
import org.mockenger.data.model.persistent.mock.response.MockResponse;
import org.mockenger.data.model.persistent.transformer.KeyValueTransformer;
import org.mockenger.data.model.persistent.transformer.RegexpTransformer;
import org.mockenger.data.model.persistent.transformer.Transformer;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

import static java.util.Objects.nonNull;
import static org.mockenger.core.util.CommonUtils.generateCheckSum;
import static org.mockenger.core.util.CommonUtils.generateUniqueId;
import static org.mockenger.core.util.CommonUtils.getCheckSum;
import static org.mockenger.core.util.CommonUtils.joinParams;
import static org.mockenger.core.util.HttpUtils.getParameterSortedSet;
import static org.mockenger.core.util.MockRequestUtils.getHeaders;
import static org.mockenger.core.util.MockRequestUtils.isURLEncodedForm;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
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
@Ignore
public abstract class AbstractHttpControllerTest extends AbstractControllerTest {

	protected static final String REQUEST_PATH = "test/rest/mock/request";
	protected static final String ID1 = "200000000001";
	protected static final String ID2 = "100000000002";
	protected static final String VALUE1 = "V1";
	protected static final String VALUE2 = "V2";
	protected static final String EXPECTED_RESULT_OK = "OK";

	protected static final String REST_JSON_REQUEST_BODY = "{\"id\":" + ID1 + ",\"dynamicValue\":\"" + VALUE1 + "\",\"name\":\"NAME\",\"type\":\"TYPE\"}";
	protected static final String REST_JSON_REQUEST_BODY_DIFFERENT_NODE_ORDER = "{\"id\":" + ID1 + ",\"name\":\"NAME\",\"type\":\"TYPE\",\"dynamicValue\":\"" + VALUE1 + "\"}";
	protected static final String REST_JSON_RESPONSE_BODY = "{\"result\":\"OK\"}";
	protected static final String REST_BAD_JSON_REQUEST = "{\"json\":\"is\",\"bad\"}";

	protected static final String REST_XML_REQUEST_BODY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<note>" +
			"<id>" + ID1 + "</id>" +
			"<dynamicValue>" + VALUE1 + "</dynamicValue>" +
			"<to>Tove</to>" +
			"<from>Jani</from>" +
			"<heading>Reminder</heading>" +
			"<body>Don't forget me this weekend!</body>" +
			"</note>";

	protected static final String REST_XML_REQUEST_BODY_DIFFERENT_NODE_ORDER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<note>" +
			"<body>Don't forget me this weekend!</body>" +
			"<id>" + ID1 + "</id>" +
			"<dynamicValue>" + VALUE1 + "</dynamicValue>" +
			"<from>Jani</from>" +
			"<to>Tove</to>" +
			"<heading>Reminder</heading>" +
			"</note>";

	protected static final String REST_XML_RESPONSE_BODY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<note>" +
			"<result>OK</result>" +
			"</note>";

	final Set<Pair> PARAM_SET_STRAIGHT = ImmutableSet.of(PARAM_PAIR_1, PARAM_PAIR_2, PARAM_PAIR_3);
	final Set<Pair> PARAM_SET_MIXED = ImmutableSet.of(PARAM_PAIR_2, PARAM_PAIR_3, PARAM_PAIR_1);

	protected Project project;
	protected Group group;
	protected Group groupWithRecording;
	protected String endpoint;
	protected String endpointWithRecording;


	@Before
	public void setUp() {
		super.setUp();

		project = createProject(true);
		group = createGroup(true, false);
		groupWithRecording = createGroup(project.getId(), true);

		endpoint = String.format(getEndpointTemplate(), group.getCode(), REQUEST_PATH);
		endpointWithRecording = String.format(getEndpointTemplate(), groupWithRecording.getCode(), REQUEST_PATH);
	}


	@After
	public void cleanup() {
		deleteProject(project);
		deleteAllGroups();
		deleteAllRequests();
	}


	@Test
	public void testPostJsonRequestOk() throws Exception {
		createRequest(createJsonMockRequestForPost(group.getId()));

		final String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2).replace(VALUE1, VALUE2);
		final MvcResult mvcResult = getMvcResult(withMediaType(post(endpoint).content(content)));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
				.andExpect(jsonPath("$.result").value(EXPECTED_RESULT_OK));
	}


	@Test
	public void testPostJsonRequestWithDifferentNodeOrderOk() throws Exception {
		final GenericRequest request = createRequest(createJsonMockRequestForPost(group.getId()));
		request.setBody(new Body(REST_JSON_REQUEST_BODY_DIFFERENT_NODE_ORDER));

		final MvcResult mvcResult = getMvcResult(withMediaType(post(endpoint).content(REST_JSON_REQUEST_BODY)));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
				.andExpect(jsonPath("$.result").value(EXPECTED_RESULT_OK));
	}


	public void testPostURLEncodedRequestOk() throws Exception {
		createRequest(createURLEncodedMockRequestForPost(group.getId()));

		final String content = joinParams(PARAM_SET_STRAIGHT, "=", "&");
		final MvcResult mvcResult = getMvcResult(withMediaType(post(endpoint).content(content), CONTENT_TYPE_X_FORM));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().string(EXPECTED_RESULT_OK));
	}


	public void testPostURLEncodedRequestDifferentOrderOk() throws Exception {
		createRequest(createURLEncodedMockRequestForPost(group.getId()));

		final String content = joinParams(PARAM_SET_MIXED, "=", "&");
		final MvcResult mvcResult = getMvcResult(withMediaType(post(endpoint).content(content), CONTENT_TYPE_X_FORM));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().string(EXPECTED_RESULT_OK));
	}

	@Test
	public void testPostJsonRequestNotFound() throws Exception {
		final String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);
		final MvcResult mvcResult = getMvcResult(withMediaType(post(endpoint).content(content)));

		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNotFound());
	}


	@Test
	public void testPutJsonRequestOk() throws Exception {
		createRequest(createJsonMockRequestForPut(group.getId()));

		final String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);
		final MvcResult mvcResult = getMvcResult(withMediaType(put(endpoint).content(content)));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8.toLowerCase()))
				.andExpect(content().string(""));
	}


	@Test
	public void testPutJsonRequestNotFound() throws Exception {
		final String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);
		final MvcResult mvcResult = getMvcResult(withMediaType(put(endpoint).content(content)));

		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNotFound());
	}


	@Test
	public void testGetJsonRequestOk() throws Exception {
		createRequest(createJsonMockRequestForGet(group.getId()));

		final MvcResult mvcResult = getMvcResult(withMediaType(get(endpoint), CONTENT_TYPE_JSON_UTF8));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8.toLowerCase()))
				.andExpect(jsonPath("$.result").value(EXPECTED_RESULT_OK));
	}


	@Test
	public void testGetRequestNotFound() throws Exception {
		final MvcResult mvcResult = getMvcResult(get(endpoint));

		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNotFound());
	}


	@Test
	public void testDeleteRequestOk() throws Exception {
		createRequest(createMockRequestForDelete(group.getId()));

		final MvcResult mvcResult = getMvcResult(withMediaType(delete(endpoint).content("")));

		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNoContent());
	}


	@Test
	public void testDeleteRequestNotFound() throws Exception {
		final MvcResult mvcResult = getMvcResult(delete(endpoint));

		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNotFound());
	}


	protected abstract String getEndpointTemplate();


	protected void createMockRequest(final AbstractRequest request,
									 final String groupId,
									 final String contentType,
									 final String requestBody,
									 final String responseBody) {

		final String id = generateUniqueId();
		final Set<Pair> headersSet = ImmutableSet.of(new Pair(CONTENT_TYPE.toLowerCase(), contentType));

		request.setId(id);
		request.setGroupId(groupId);
		request.setCode(PROJECT_CODE + "-" + GROUP_CODE +  "-" + id);
		request.setName(REQUEST_NAME_TEST);
		request.setCreationDate(new Date());
		request.setPath(new Path(REQUEST_PATH));
		request.setParameters(null);

		if (nonNull(contentType)) {
			request.setHeaders(new Headers(ImmutableList.of(new KeyValueTransformer("key", ID2, ID1)), headersSet));
		}

		if (nonNull(requestBody)) {
			final ImmutableList<Transformer> transformers = ImmutableList.of(
					new RegexpTransformer(ID2, ID1), new RegexpTransformer(VALUE2, VALUE1)
			);

			final Headers headers = getHeaders(request);

			if (isURLEncodedForm(headers)) {
				final SortedSet sortedSet = getParameterSortedSet(requestBody);
				final String joinedParams = joinParams(sortedSet, "=", "&");

				request.setBody(new Body(transformers, joinedParams));
			} else {
				request.setBody(new Body(transformers, requestBody));
			}

		}

		request.setMockResponse(new MockResponse(200, headersSet, responseBody));
	}


	protected PostRequest createJsonMockRequestForPost(String groupId) {
		final PostRequest postRequest = new PostRequest();

		createMockRequest(postRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), REST_JSON_REQUEST_BODY, REST_JSON_RESPONSE_BODY);
		postRequest.setCheckSum(getCheckSum(postRequest));
		postRequest.getMockResponse().setHttpStatus(201);
		postRequest.getMockResponse().setHeaders(null);

		return postRequest;
	}


	protected PostRequest createURLEncodedMockRequestForPost(String groupId) {
		final PostRequest postRequest = new PostRequest();
		final String requestBody = joinParams(PARAM_SET_STRAIGHT, "=", "&");

		createMockRequest(postRequest, groupId, CONTENT_TYPE_X_FORM.toLowerCase(), requestBody, "OK");
		postRequest.setCheckSum(getCheckSum(postRequest));
		postRequest.getMockResponse().setHttpStatus(200);
		postRequest.getMockResponse().setHeaders(null);

		return postRequest;
	}


	protected PutRequest createJsonMockRequestForPut(String groupId) {
		final PutRequest putRequest = new PutRequest();

		createMockRequest(putRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), REST_JSON_REQUEST_BODY, null);
		putRequest.setCheckSum(getCheckSum(putRequest));

		return putRequest;
	}

	protected GetRequest createJsonMockRequestForGet(final String groupId) {
		final GetRequest getRequest = new GetRequest();

		createMockRequest(getRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), null, REST_JSON_RESPONSE_BODY);
		getRequest.setCheckSum(generateCheckSum(getRequest));

		return getRequest;
	}

	protected DeleteRequest createMockRequestForDelete(String groupId) {
		final DeleteRequest deleteRequest = new DeleteRequest();

		createMockRequest(deleteRequest, groupId, null, null, null);
		deleteRequest.getMockResponse().setHttpStatus(204);
		deleteRequest.getMockResponse().setHeaders(null);
		deleteRequest.setCheckSum(generateCheckSum(deleteRequest));

		return deleteRequest;
	}
}

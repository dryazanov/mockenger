package org.mockenger.core.web.controller.endpoint;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.mockenger.core.util.CommonUtils;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.DeleteRequest;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.Set;

import static org.mockenger.core.util.CommonUtils.generateCheckSum;
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

	protected Project project;
	protected Group group;
	protected Group groupWithRecording;
	protected String endpoint;
	protected String endpointWithRecording;


	@Before
	public void setUp() {
		super.setUp();

		project = createProject(true);
		group = createGroup(false);
		groupWithRecording = createGroup(project.getId(), true);

		endpoint = String.format(getEndpointTemplate(), group.getId(), REQUEST_PATH);
		endpointWithRecording = String.format(getEndpointTemplate(), groupWithRecording.getId(), REQUEST_PATH);
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


	protected void createMockRequest(final AbstractRequest request, final String groupId, final String contentType,
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


	protected PostRequest createJsonMockRequestForPost(String groupId) {
		final PostRequest postRequest = new PostRequest();

		createMockRequest(postRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), REST_JSON_REQUEST_BODY, REST_JSON_RESPONSE_BODY);
		postRequest.setCheckSum(CommonUtils.getCheckSum(postRequest));
		postRequest.getMockResponse().setHttpStatus(201);
		postRequest.getMockResponse().setHeaders(null);

		return postRequest;
	}

	protected PutRequest createJsonMockRequestForPut(String groupId) {
		final PutRequest putRequest = new PutRequest();

		createMockRequest(putRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), REST_JSON_REQUEST_BODY, null);
		putRequest.setCheckSum(CommonUtils.getCheckSum(putRequest));

		return putRequest;
	}

	protected GetRequest createJsonMockRequestForGet(String groupId) {
		final GetRequest getRequest = new GetRequest();

		createMockRequest(getRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), null, REST_JSON_RESPONSE_BODY);
		getRequest.setCheckSum(CommonUtils.generateCheckSum(getRequest));

		return getRequest;
	}

	protected DeleteRequest createMockRequestForDelete(String groupId) {
		final DeleteRequest deleteRequest = new DeleteRequest();

		createMockRequest(deleteRequest, groupId, null, null, null);
		deleteRequest.getMockResponse().setHttpStatus(204);
		deleteRequest.getMockResponse().setHeaders(null);
		deleteRequest.setCheckSum(CommonUtils.generateCheckSum(deleteRequest));

		return deleteRequest;
	}
}

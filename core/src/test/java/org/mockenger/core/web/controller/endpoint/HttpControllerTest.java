package org.mockenger.core.web.controller.endpoint;

import com.google.common.collect.ImmutableSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockenger.core.util.CommonUtils;
import org.mockenger.data.model.persistent.mock.request.HeadRequest;
import org.mockenger.data.model.persistent.mock.request.OptionsRequest;
import org.mockenger.data.model.persistent.mock.request.PatchRequest;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Dmitry Ryazanov
 */
public class HttpControllerTest extends AbstractHttpControllerTest {

    private static final String ENDPOINT_TEMPLATE = API_PATH + "/HTTP/%s/%s";


    @Before
    public void setUp() {
        super.setUp();
    }


    @After
    public void cleanup() {
        super.cleanup();
    }



	@Test
	public void testPostURLEncodedRequestOk() throws Exception {
		super.testPostURLEncodedRequestOk();
	}


	@Test
	public void testPostURLEncodedRequestDifferentOrderOk() throws Exception {
		super.testPostURLEncodedRequestDifferentOrderOk();
	}


	@Test
	public void testPatchRequestOk() throws Exception {
		createRequest(createJsonMockRequestForPatch(group.getId()));

		final MvcResult mvcResult = getMvcResult(withMediaType(patch(endpoint).content(REST_JSON_REQUEST_BODY)));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8.toLowerCase()))
				.andExpect(jsonPath("$.result").value(EXPECTED_RESULT_OK));
	}


	@Test
	public void testPatchRequestNotFound() throws Exception {
		final MvcResult mvcResult = getMvcResult(withMediaType(patch(endpoint).content(REST_JSON_REQUEST_BODY)));

		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNotFound());
	}


	@Test
	public void testHeadRequestOk() throws Exception {
		createRequest(createJsonMockRequestForHead(group.getId()));

		final MvcResult mvcResult = getMvcResult(head(endpoint));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8));
	}


	@Test
	public void testHeadRequestNotFound() throws Exception {
		final MvcResult mvcResult = getMvcResult(head(endpoint));

		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNotFound());
	}


	@Test
	public void testOptionsRequestOk() throws Exception {
		createRequest(createJsonMockRequestForOptions(group.getId()));

		final MvcResult mvcResult = getMvcResult(options(endpoint));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(header().string("Allow", "GET,HEAD,POST,OPTIONS"));
	}


	@Test
	public void testOptionsRequestNotFound() throws Exception {
		final MvcResult mvcResult = getMvcResult(options(endpoint));

		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isNotFound());
	}


    @Override
    protected String getEndpointTemplate() {
		return ENDPOINT_TEMPLATE;
	}


	protected PatchRequest createJsonMockRequestForPatch(final String groupId) {
		final PatchRequest patchRequest = new PatchRequest();

		createMockRequest(patchRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), REST_JSON_REQUEST_BODY, REST_JSON_RESPONSE_BODY);
		patchRequest.setCheckSum(CommonUtils.getCheckSum(patchRequest));
		patchRequest.getMockResponse().setHttpStatus(200);
		patchRequest.getMockResponse().setHeaders(null);

		return patchRequest;
	}


	protected HeadRequest createJsonMockRequestForHead(final String groupId) {
		final HeadRequest headRequest = new HeadRequest();
		final Set<Pair> headersSet = ImmutableSet.of(new Pair(CONTENT_TYPE.toLowerCase(), CONTENT_TYPE_JSON_UTF8));

		createMockRequest(headRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), null, null);
		headRequest.setCheckSum(CommonUtils.getCheckSum(headRequest));
		headRequest.getMockResponse().setHttpStatus(200);
		headRequest.getMockResponse().setHeaders(headersSet);

		return headRequest;
	}


	protected OptionsRequest createJsonMockRequestForOptions(final String groupId) {
		final OptionsRequest optionsRequest = new OptionsRequest();
		final Set<Pair> headersSet = ImmutableSet.of(new Pair("Allow", "GET,HEAD,POST,OPTIONS"));

		createMockRequest(optionsRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), null, null);
		optionsRequest.setCheckSum(CommonUtils.getCheckSum(optionsRequest));
		optionsRequest.getMockResponse().setHttpStatus(200);
		optionsRequest.getMockResponse().setHeaders(headersSet);

		return optionsRequest;
	}
}

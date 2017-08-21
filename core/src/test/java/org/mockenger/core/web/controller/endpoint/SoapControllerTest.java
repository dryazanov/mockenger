package org.mockenger.core.web.controller.endpoint;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.mockenger.data.model.persistent.mock.request.part.Headers;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
import org.mockenger.data.model.persistent.mock.request.part.Path;
import org.mockenger.data.model.persistent.mock.response.MockResponse;
import org.mockenger.data.model.persistent.transformer.RegexpTransformer;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.Set;

import static org.mockenger.core.util.CommonUtils.generateUniqueId;
import static org.mockenger.core.util.CommonUtils.getCheckSum;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * @author Dmitry Ryazanov
 */
public class SoapControllerTest extends AbstractControllerTest {

    private static final String ENDPOINT_TEMPLATE = AbstractController.API_PATH + "/SOAP/%s/%s";
    private static final String REQUEST_PATH = "test/rest/mock/request";
    private static final String ID1 = "200000000001";
    private static final String ID2 = "100000000002";

    protected static final String SOAP_XML_REQUEST_BODY = "<S:Body xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<ns3:DataRequestElement xmlns:ns3=\"http://www.af-klm.com/services/passenger/data-v1/xsd\">" +
			"<identificationNumber>" +
			ID1 +
			"</identificationNumber>" +
			"</ns3:DataRequestElement>" +
			"</S:Body>";

    protected static final String SOAP_XML_REQUEST = "<?xml version='1.0' encoding='UTF-8'?>" +
			"<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<S:Header/>" +
			SOAP_XML_REQUEST_BODY +
			"</S:Envelope>";

    protected static final String SOAP_XML_RESPONSE = "<?xml version='1.0' encoding='UTF-8'?>" +
			"<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<S:Header/>" +
			"<S:Body>" +
			"<ns3:DataResponseElement xmlns:ns3=\"http://www.af-klm.com/services/passenger/data-v1/xsd\">" +
			"<result>OK</result>" +
			"</ns3:DataResponseElement>" +
			"</S:Body>" +
			"</S:Envelope>";

    private Project project;
    private String endpoint;

    @Before
    public void setUp() {
        super.setUp();

        project = createProject();
        final Group group = createGroup(project.getId(), true);
        createRequest(createSoapMockRequest(group.getId()));

        endpoint = String.format(ENDPOINT_TEMPLATE, group.getCode(), REQUEST_PATH);
    }

    @After
    public void cleanup() {
        deleteProject(project);
        deleteAllGroups();
        deleteAllRequests();
    }

    @Test
    public void testProcessPosRequestOk() throws Exception {
        final String content = SOAP_XML_REQUEST.replace(ID1, ID2);
		final MvcResult mvcResult = getMvcResult(withMediaType(post(endpoint).content(content), CONTENT_TYPE_SOAP_UTF8));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(content().contentType(CONTENT_TYPE_SOAP_UTF8.toLowerCase()))
				.andExpect(xpath("//result").string("OK"));
    }

    @Test
    public void testProcessPosRequestCustomContentType() throws Exception {
        final String content = SOAP_XML_REQUEST.replace(ID1, ID2);
		final MvcResult mvcResult = getMvcResult(withMediaType(post(endpoint).content(content), CONTENT_TYPE_JSON_UTF8));

		mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isCreated());
    }

    @Test
    public void testProcessPosRequestInvalidBody() throws Exception {
        final String content = SOAP_XML_REQUEST + "<invalidTag>";
		final MvcResult mvcResult = getMvcResult(withMediaType(post(endpoint).content(content), CONTENT_TYPE_SOAP_UTF8));

		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]").value("Failed to create instance of the mock-object: Cannot create SOAP message"));
    }


    private PostRequest createSoapMockRequest(final String groupId) {
        final Set<Pair> headersSet = ImmutableSet.of(new Pair("content-type", CONTENT_TYPE_SOAP_UTF8.toLowerCase()));
        final PostRequest postRequest = new PostRequest();
		final Body body = new Body(ImmutableList.of(new RegexpTransformer(ID2, ID1)), SOAP_XML_REQUEST_BODY);
		final MockResponse mockResponse = new MockResponse(200, headersSet, SOAP_XML_RESPONSE);
		final String id = generateUniqueId();

		postRequest.setId(id);
        postRequest.setGroupId(groupId);
		postRequest.setCode(PROJECT_CODE + "-" + GROUP_CODE +  "-" + id);
        postRequest.setName(REQUEST_NAME_TEST);
        postRequest.setMethod(RequestMethod.POST);
        postRequest.setCreationDate(new Date());
        postRequest.setPath(new Path(REQUEST_PATH));
        postRequest.setParameters(null);
        postRequest.setHeaders(new Headers(headersSet));
		postRequest.setBody(body);
		postRequest.setMockResponse(mockResponse);
        postRequest.setCheckSum(getCheckSum(postRequest));

        return postRequest;
    }
}

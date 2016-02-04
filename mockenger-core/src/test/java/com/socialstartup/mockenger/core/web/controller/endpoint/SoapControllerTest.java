package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PostRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import com.socialstartup.mockenger.data.model.persistent.transformer.RegexpTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
public class SoapControllerTest extends AbstractControllerTest {
    private static final String ENDPOINT_TEMPLATE = "/SOAP/%s/%s";
    private static final String REQUEST_PATH = "test/rest/mock/request";
    private static final String ID1 = "200000000001";
    private static final String ID2 = "100000000002";

    protected static final String SOAP_XML_REQUEST_BODY = new StringBuilder()
            .append("<S:Body xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">")
            .append("<ns3:DataRequestElement xmlns:ns3=\"http://www.af-klm.com/services/passenger/data-v1/xsd\">")
            .append("<identificationNumber>")
            .append(ID1)
            .append("</identificationNumber>")
            .append("</ns3:DataRequestElement>")
            .append("</S:Body>")
            .toString();

    protected static final String SOAP_XML_REQUEST = new StringBuilder()
            .append("<?xml version='1.0' encoding='UTF-8'?>")
            .append("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">")
            .append("<S:Header/>")
            .append(SOAP_XML_REQUEST_BODY)
            .append("</S:Envelope>")
            .toString();

    protected static final String SOAP_XML_RESPONSE = new StringBuilder()
            .append("<?xml version='1.0' encoding='UTF-8'?>")
            .append("<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">")
            .append("<S:Header/>")
            .append("<S:Body>")
            .append("<ns3:DataResponseElement xmlns:ns3=\"http://www.af-klm.com/services/passenger/data-v1/xsd\">")
            .append("<result>OK</result>")
            .append("</ns3:DataResponseElement>")
            .append("</S:Body>")
            .append("</S:Envelope>")
            .toString();

    private Project project;
    private String endpoint;

    @Before
    public void setUp() {
        super.setUp();

        project = createProject();
        final Group group = createGroup();
        createRequest(createSoapMockRequest(group.getId()));

        endpoint = String.format(ENDPOINT_TEMPLATE, group.getId(), REQUEST_PATH);
    }

    @After
    public void cleanup() {
        deleteProject(project);
        deleteAllGroups();
        deleteAllRequests();
    }

    @Test
    public void testProcessPosRequestOk() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_SOAP_UTF8);
        String content = SOAP_XML_REQUEST.replace(ID1, ID2);

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_SOAP_UTF8.toLowerCase()))
                .andExpect(xpath("//result").string("OK"));
    }

    @Test
    public void testProcessPosRequestInvalidContentType() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        String content = SOAP_XML_REQUEST.replace(ID1, ID2);

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Invalid header 'Content-type': application/soap+xml is only allowed in SOAP requests"));
    }

    @Test
    public void testProcessPosRequestInvalidBody() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_SOAP_UTF8);
        String content = SOAP_XML_REQUEST + "<invalidTag>";

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Failed to create instance of the mock-object: Cannot create SOAP message"));
    }


    private PostRequest createSoapMockRequest(String groupId) {
        Set<Pair> headersSet = ImmutableSet.of(new Pair("content-type", CONTENT_TYPE_SOAP_UTF8.toLowerCase()));

        PostRequest postRequest = new PostRequest();
        postRequest.setGroupId(groupId);
        postRequest.setId(CommonUtils.generateUniqueId());
        postRequest.setName(REQUEST_NAME_TEST);
        postRequest.setMethod(RequestMethod.POST);
        postRequest.setCreationDate(new Date());
        postRequest.setPath(new Path(REQUEST_PATH));
        postRequest.setParameters(null);
        postRequest.setHeaders(new Headers(headersSet));
        postRequest.setBody(new Body(ImmutableList.of(new RegexpTransformer(ID2, ID1)), SOAP_XML_REQUEST_BODY));
        postRequest.setMockResponse(new MockResponse(200, headersSet, SOAP_XML_RESPONSE));
        postRequest.setCheckSum(CommonUtils.getCheckSum(postRequest));

        return postRequest;
    }
}

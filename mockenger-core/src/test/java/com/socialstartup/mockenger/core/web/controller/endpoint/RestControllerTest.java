package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.config.TestContext;
import com.socialstartup.mockenger.core.util.CommonUtils;
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
import com.socialstartup.mockenger.data.model.persistent.transformer.RegexpTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

/**
 * Created by x079089 on 6/29/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestContext.class})
public class RestControllerTest extends AbstractControllerTest {

    private static final String ENDPOINT_TEMPLATE = "/REST/%s/%s";
    private static final String REQUEST_PATH = "test/rest/mock/request";
    private static final String ID1 = "200000000001";
    private static final String ID2 = "100000000002";
    private static final String EXPECTED_RESULT_OK = "OK";

    protected static final String REST_JSON_REQUEST_BODY = "{\"id\":" + ID1 + ",\"name\":\"NAME\",\"type\":\"TYPE\"}";
    protected static final String REST_JSON_RESPONSE_BODY = "{\"result\":\"OK\"}";
    protected static final String REST_BAD_JSON_REQUEST = "{\"json\":\"is\",\"bad\"}";

    protected static final String REST_XML_REQUEST_BODY = new StringBuilder()
            .append("<note>")
            .append("<id>")
            .append(ID1)
            .append("</id>")
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


    @Before
    public void setup() {
        super.setup();

        project = createProject(true);
        group = createGroup(false);
        groupWithRecording = createGroup();

        endpoint = String.format(ENDPOINT_TEMPLATE, group.getId(), REQUEST_PATH);
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
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);

        createRequest(createJsonMockRequestForPost(group.getId()));

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.result").value(EXPECTED_RESULT_OK));
    }

    @Test
    public void testPostJsonRequestWithBadJson() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(REST_BAD_JSON_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Failed to create instance of the mock-object: Cannot read json from the provided source"));
    }

    @Test
    public void testPostJsonRequestNotFound() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(content)).andExpect(status().isNotFound());
    }

    @Test
    public void testPostJsonRequestNotFoundButCreated() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);
        endpoint = String.format(ENDPOINT_TEMPLATE, groupWithRecording.getId(), REQUEST_PATH);

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(content)).andExpect(status().isCreated());
        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(content)).andExpect(status().isFound());
    }

    //========== POST WITH XML =================//

    @Test
    public void testPostXmlRequest() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_XML_UTF8);
        String content = REST_XML_REQUEST_BODY.replace(ID1, ID2);

        createRequest(createXmlMockRequestForPost(group.getId()));

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(content))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(CONTENT_TYPE_XML_UTF8))
                .andExpect(xpath("/note/result").string(EXPECTED_RESULT_OK));
    }


    //========== PUT WITH JSON =================//

    @Test
    public void testPutJsonRequestOk() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);

        createRequest(createJsonMockRequestForPut(group.getId()));

        this.mockMvc.perform(put(endpoint).contentType(mediaType).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8.toLowerCase()))
                .andExpect(content().string(""));
    }

    @Test
    public void testPutJsonRequestNotFound() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);
        String content = REST_JSON_REQUEST_BODY.replace(ID1, ID2);

        this.mockMvc.perform(put(endpoint).contentType(mediaType).content(content)).andExpect(status().isNotFound());
    }

    @Test
    public void testPutJsonRequestWithBadJson() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);

        this.mockMvc.perform(put(endpoint).contentType(mediaType).content(REST_BAD_JSON_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Failed to create instance of the mock-object: Cannot read json from the provided source"));
    }


    //========== PUT WITH XML =================//

    @Test
    public void testPutXmlRequest() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_XML_UTF8);
        String content = REST_XML_REQUEST_BODY.replace(ID1, ID2);

        createRequest(createXmlMockRequestForPut(group.getId()));

        this.mockMvc.perform(put(endpoint).contentType(mediaType).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE_XML_UTF8.toLowerCase()))
                .andExpect(content().string(""));
    }


    //========== GET WITH JSON =================//

    @Test
    public void testGetJsonRequestOk() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_JSON_UTF8);

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
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_XML_UTF8);

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
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_HTML_UTF8);

        this.mockMvc.perform(post(endpoint).contentType(mediaType).content(""))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors[0]").value("Invalid header 'Content-type': application/json or application/xml are only allowed in REST requests"));
    }

    //========== PUT BAD REQUEST WITH HTML =================//

    @Test
    public void testPutHtmlRequestNotOk() throws Exception {
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE_HTML_UTF8);

        this.mockMvc.perform(put(endpoint).contentType(mediaType).content(""))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(CONTENT_TYPE_JSON_UTF8))
                .andExpect(jsonPath("$.errors[0]").value("Invalid header 'Content-type': application/json or application/xml are only allowed in REST requests"));
    }

    private void createMockRequest(AbstractRequest request, String groupId, String contentType, String requestBody, String responseBody) {
        Set<Pair> headersMap = new HashSet<>();
        headersMap.add(new Pair("content-type", contentType));

        RegexpTransformer regexpTransformer = new RegexpTransformer(ID2, ID1);

        request.setGroupId(groupId);
        request.setId(CommonUtils.generateUniqueId());
        request.setName(REQUEST_NAME_TEST);
        request.setCreationDate(new Date());

        request.setPath(new Path(REQUEST_PATH));
        request.setParameters(null);
        if (contentType != null) {
            request.setHeaders(new Headers(headersMap));
        }
        if (requestBody != null) {
            request.setBody(new Body(Arrays.asList(regexpTransformer), requestBody));
        }

        MockResponse mockResponse = new MockResponse();
        mockResponse.setHttpStatus(200);
        mockResponse.setHeaders(headersMap);
        mockResponse.setBody(responseBody);
        request.setMockResponse(mockResponse);
    }

    private PostRequest createJsonMockRequestForPost(String groupId) {
        PostRequest postRequest = new PostRequest();
        createMockRequest(postRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), REST_JSON_REQUEST_BODY, REST_JSON_RESPONSE_BODY);
        postRequest.setCheckSum(CommonUtils.getCheckSum(postRequest));
        postRequest.getMockResponse().setHttpStatus(201);
        postRequest.getMockResponse().setHeaders(null);
        return postRequest;
    }

    private PutRequest createJsonMockRequestForPut(String groupId) {
        PutRequest putRequest = new PutRequest();
        createMockRequest(putRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), REST_JSON_REQUEST_BODY, null);
        putRequest.setCheckSum(CommonUtils.getCheckSum(putRequest));
        return putRequest;
    }

    private GetRequest createJsonMockRequestForGet(String groupId) {
        GetRequest getRequest = new GetRequest();
        createMockRequest(getRequest, groupId, CONTENT_TYPE_JSON_UTF8.toLowerCase(), null, REST_JSON_RESPONSE_BODY);
        getRequest.setCheckSum(CommonUtils.generateCheckSum(getRequest));
        return getRequest;
    }

    private PostRequest createXmlMockRequestForPost(String groupId) {
        PostRequest postRequest = new PostRequest();
        createMockRequest(postRequest, groupId, CONTENT_TYPE_XML_UTF8.toLowerCase(), REST_XML_REQUEST_BODY, REST_XML_RESPONSE_BODY);
        postRequest.setCheckSum(CommonUtils.getCheckSum(postRequest));
        postRequest.getMockResponse().setHttpStatus(201);
        postRequest.getMockResponse().setHeaders(null);
        return postRequest;
    }

    private PutRequest createXmlMockRequestForPut(String groupId) {
        PutRequest putRequest = new PutRequest();
        createMockRequest(putRequest, groupId, CONTENT_TYPE_XML_UTF8.toLowerCase(), REST_XML_REQUEST_BODY, null);
        putRequest.setCheckSum(CommonUtils.getCheckSum(putRequest));
        return putRequest;
    }

    private GetRequest createXmlMockRequestForGet(String groupId) {
        GetRequest getRequest = new GetRequest();
        createMockRequest(getRequest, groupId, CONTENT_TYPE_XML_UTF8.toLowerCase(), null, REST_XML_RESPONSE_BODY);
        getRequest.setCheckSum(CommonUtils.generateCheckSum(getRequest));
        return getRequest;
    }

    private DeleteRequest createMockRequestForDelete(String groupId) {
        DeleteRequest deleteRequest = new DeleteRequest();
        createMockRequest(deleteRequest, groupId, null, null, null);
        deleteRequest.getMockResponse().setHttpStatus(204);
        deleteRequest.getMockResponse().setHeaders(null);
        deleteRequest.setCheckSum(CommonUtils.generateCheckSum(deleteRequest));
        return deleteRequest;
    }
}

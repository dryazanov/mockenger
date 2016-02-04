package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.socialstartup.mockenger.core.config.TestContext;
import com.socialstartup.mockenger.core.config.TestPropertyContext;
import com.socialstartup.mockenger.core.service.GroupService;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.KeyValueTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.RegexpTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.XPathTransformer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by Dmitry Ryazanov on 6/29/2015.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestPropertyContext.class, TestContext.class})
public class AbstractControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RequestService requestService;

    protected MockMvc mockMvc;

    protected static final String PROJECT_ID = "PROJECT_ID";
    protected static final String GROUP_ID = "GROUP_ID";
    protected static final String REQUEST_ID = "REQUEST_ID";
    protected static final String PROJECT_NAME_TEST = "Unit-test project";
    protected static final String PROJECT_CODE_TEST = "UTCODE";
    protected static final String GROUP_NAME_TEST = "Unit-test group";
    protected static final String REQUEST_NAME_TEST = "Unit-test mock-request";
    protected static final String REQUEST_PATH = "/unit/test/mock/request";
    protected static final String REQUEST_PATH_API = "/projects/%s/groups/%s/requests";

    protected static final String SEMICOLON = ";";
    protected static final String CHARSET_UTF8 = "charset=UTF-8";
    protected static final String CONTENT_TYPE_JSON_UTF8 = MediaType.APPLICATION_JSON_VALUE + SEMICOLON + CHARSET_UTF8;
    protected static final String CONTENT_TYPE_SOAP_UTF8 = "application/soap+xml" + SEMICOLON + CHARSET_UTF8;
    protected static final String CONTENT_TYPE_XML_UTF8 = MediaType.APPLICATION_XML_VALUE + SEMICOLON + CHARSET_UTF8;
    protected static final String CONTENT_TYPE_HTML_UTF8 = MediaType.TEXT_HTML_VALUE + SEMICOLON + CHARSET_UTF8;

    protected static final String MOCK_REQUEST_BODY = "{\"name\":\"NAME\",\"type\":\"TYPE\"}";
    protected static final String MOCK_RESPONSE_BODY = "{\"result\":\"OK\"}";

    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    // ===============
    // PROJECT HELPERS
    // ===============
    
    protected Project getProject(String projectId) {
        return this.projectService.findById(projectId);
    }

    protected Iterable<Project> getAllProjects() {
        return this.projectService.findAll();
    }

    protected void deleteProject(Project project) {
        this.projectService.remove(project);
    }

    protected void deleteAllProjects() {
        for (Project project : getAllProjects()) {
            deleteProject(project);
        }
    }

    protected Project createProject() {
        return createProject(false);
    }

    protected Project createProject(boolean generateRandomProjectCode) {
        return createProject(getNewProject(generateRandomProjectCode));
    }

    protected Project createProject(Project project) {
        this.projectService.save(project);
        return project;
    }

    protected Project getNewProject() {
        return getNewProject(false);
    }

    protected Project getNewProject(boolean generateRandomProjectCode) {
        String id = CommonUtils.generateUniqueId();
        String code = PROJECT_CODE_TEST + (generateRandomProjectCode ? "-" + id  : "");
        Project project = new Project(PROJECT_NAME_TEST, code, ProjectType.HTTP);
        project.setId(id);
        return project;
    }

    // ===============
    // GROUP HELPERS
    // ===============

    protected Group getGroup(String groupId) {
        return this.groupService.findById(groupId);
    }

    protected Iterable<Group> getAllGroups() {
        return this.groupService.findAll();
    }

    protected void deleteAllGroups() {
        Iterator<Group> iterator = getAllGroups().iterator();
        while (iterator.hasNext()) {
            deleteGroup(iterator.next());
        }
    }

    protected void deleteGroup(Group group) {
        this.groupService.remove(group);
    }

    protected Group createGroup() {
        return createGroup(true);
    }

    protected Group createGroup(boolean recording) {
        Group group = getNewGroup(recording);
        this.groupService.save(group);
        return group;
    }

    protected Group createGroup(String projectId, boolean recording) {
        Group group = getNewGroup(projectId, recording);
        this.groupService.save(group);
        return group;
    }

    protected Group getNewGroup() {
        return getNewGroup(true);
    }

    protected Group getNewGroup(boolean recording) {
        return getNewGroup(PROJECT_ID, recording);
    }

    protected Group getNewGroup(String projectId, boolean recording) {
        Group group = new Group(projectId, GROUP_NAME_TEST, recording);
        group.setId(CommonUtils.generateUniqueId());
        return group;
    }

    // ===============
    // REQUEST HELPERS
    // ===============

    protected AbstractRequest getRequest(String requestId) {
        return this.requestService.findById(requestId);
    }

    protected Iterable<AbstractRequest> getAllRequests() {
        return this.requestService.findAll();
    }

    protected void deleteAllRequests() {
        for (AbstractRequest abstractRequest : getAllRequests()) {
            deleteRequest(abstractRequest);
        }
    }

    protected void deleteRequest(AbstractRequest request) {
        this.requestService.remove(request);
    }

    protected AbstractRequest createRequest(String groupId) {
        AbstractRequest request = getNewRequest(groupId);
        this.requestService.save(request);
        return request;
    }

    protected void createRequest(AbstractRequest request) {
        this.requestService.save(request);
    }

    protected ResultActions sendPostRequest(String endpoint, MediaType mediaType, AbstractRequest request) throws Exception {
        String requestJson = new ObjectMapper(new JsonFactory()).writeValueAsString(request);
        return this.mockMvc.perform(post(endpoint).contentType(mediaType).content(requestJson));
    }

    protected static AbstractRequest getNewRequest(String groupId) {
        AbstractRequest request = new AbstractRequest();
        List<AbstractMapTransformer> mapTransformers = ImmutableList.of(new KeyValueTransformer());
        List<AbstractTransformer> transformers = ImmutableList.of(new RegexpTransformer(), new XPathTransformer());
        Set<Pair> headersSet = ImmutableSet.of(new Pair("header1", "H1"), new Pair("header2", "H2"));
        Set<Pair> paramsSet = ImmutableSet.of(new Pair("A", "1"), new Pair("b", "2"));

        String id = CommonUtils.generateUniqueId();
        request.setId(id);
        request.setGroupId(groupId);
        request.setUniqueCode(PROJECT_CODE_TEST + id);
        request.setName(REQUEST_NAME_TEST);
        request.setMethod(RequestMethod.POST);
        request.setCreationDate(new Date());
        request.setPath(new Path(transformers, REQUEST_PATH));
        request.setParameters(new Parameters(mapTransformers, paramsSet));
        request.setHeaders(new Headers(mapTransformers, headersSet));
        request.setBody(new Body(transformers, MOCK_REQUEST_BODY));
        request.setMockResponse(new MockResponse(200, headersSet, MOCK_RESPONSE_BODY));
        request.setCheckSum(CommonUtils.getCheckSum(request));

        return request;
    }
}

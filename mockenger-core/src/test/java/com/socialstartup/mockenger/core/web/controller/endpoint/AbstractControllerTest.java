package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.config.TestContext;
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
import com.socialstartup.mockenger.data.model.persistent.transformer.KeyValueTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.RegexpTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.XPathTransformer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by x079089 on 6/29/2015.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestContext.class})
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

    protected static final String CONTENT_TYPE_JSON_UTF8 = "application/json;charset=UTF-8";
    protected static final String CONTENT_TYPE_SOAP_UTF8 = "application/soap+xml;charset=UTF-8";
    protected static final String CONTENT_TYPE_XML_UTF8 = "application/xml;charset=UTF-8";
    protected static final String CONTENT_TYPE_HTML_UTF8 = "text/html;charset=UTF-8";

    protected static final String MOCK_REQUEST_BODY = "{\"name\":\"NAME\",\"type\":\"TYPE\"}";
    protected static final String MOCK_RESPONSE_BODY = "{\"result\":\"OK\"}";

    
    @Before
    public void setup() {
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

    protected Project getAnyProject() throws Exception {
        return this.projectService.findAll().iterator().next();
    }

    protected void deleteProject(Project project) {
        this.projectService.remove(project);
    }

    protected void deleteAllProjects() {
        Iterator<Project> iterator = getAllProjects().iterator();
        while (iterator.hasNext()) {
            deleteProject(iterator.next());
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
        Project project = new Project(PROJECT_NAME_TEST, code, ProjectType.SIMPLE);
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

    protected Group getNewGroup() {
        return getNewGroup(true);
    }

    protected Group getNewGroup(boolean recording) {
        Group group = new Group(PROJECT_ID, GROUP_NAME_TEST, recording);
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
        Iterator<AbstractRequest> iterator = getAllRequests().iterator();
        while (iterator.hasNext()) {
            deleteRequest(iterator.next());
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

    protected static AbstractRequest getNewRequest(String groupId) {
        Set<Pair> paramsMap = new HashSet<>();
        Set<Pair> headersMap = new HashSet<>();
        String PARAM_NAME1 = "A";
        String PARAM_NAME2 = "b";
        String PARAM_VALUE1 = "1";
        String PARAM_VALUE2 = "2";
        String HEADER_NAME1 = "header1";
        String HEADER_NAME2 = "header2";
        String HEADER_VALUE1 = "H1";
        String HEADER_VALUE2 = "h2";

        paramsMap.add(new Pair(PARAM_NAME1, PARAM_VALUE1));
        paramsMap.add(new Pair(PARAM_NAME2, PARAM_VALUE2));

        headersMap.add(new Pair(HEADER_NAME1, HEADER_VALUE1));
        headersMap.add(new Pair(HEADER_NAME2, HEADER_VALUE2));

        RegexpTransformer regexpTransformer = new RegexpTransformer();
        XPathTransformer xPathTransformer = new XPathTransformer();
        KeyValueTransformer keyValueTransformer = new KeyValueTransformer();
        AbstractRequest request = new AbstractRequest();

        String id = CommonUtils.generateUniqueId();
        request.setId(id);
        request.setGroupId(groupId);
        request.setUniqueCode(PROJECT_CODE_TEST + id);
        request.setName(REQUEST_NAME_TEST);
        request.setMethod(RequestMethod.POST);
        request.setCreationDate(new Date());
        request.setPath(new Path(Arrays.asList(regexpTransformer, xPathTransformer), REQUEST_PATH));
        request.setParameters(new Parameters(Arrays.asList(keyValueTransformer), paramsMap));
        request.setHeaders(new Headers(Arrays.asList(keyValueTransformer), headersMap));
        request.setBody(new Body(Arrays.asList(regexpTransformer, xPathTransformer), MOCK_REQUEST_BODY));

        MockResponse mockResponse = new MockResponse();
        mockResponse.setHttpStatus(200);
        mockResponse.setHeaders(headersMap);
        mockResponse.setBody(MOCK_RESPONSE_BODY);
        request.setMockResponse(mockResponse);

        request.setCheckSum(CommonUtils.getCheckSum(request));

        return request;
    }
}

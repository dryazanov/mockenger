package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.socialstartup.mockenger.core.config.TestContext;
import com.socialstartup.mockenger.core.config.TestPropertyContext;
import com.socialstartup.mockenger.core.service.EventService;
import com.socialstartup.mockenger.core.service.GroupService;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.core.service.account.AccountService;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.dict.EventType;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.log.Event;
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
import com.socialstartup.mockenger.data.model.persistent.transformer.Transformer;
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
 * @author Dmitry Ryazanov
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
    private AccountService accountService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private EventService eventService;


    protected MockMvc mockMvc;

    protected static final String PROJECT_ID = "PROJECT_ID";
    protected static final String GROUP_ID = "GROUP_ID";
    protected static final String REQUEST_ID = "REQUEST_ID";
    protected static final String PROJECT_NAME_TEST = "Unit-test project";
    protected static final String PROJECT_CODE_TEST = "UTCODE";
    protected static final String GROUP_NAME_TEST = "Unit-test group";
    protected static final String ACCOUNT_FIRST_NAME_TEST = "First name";
    protected static final String ACCOUNT_LAST_NAME_TEST = "Last name";
    protected static final String ACCOUNT_USERNAME_TEST = "UserName";
    protected static final String ACCOUNT_PASSWORD_TEST = "pswd";
    protected static final String REQUEST_NAME_TEST = "Unit-test mock-request";
    protected static final String REQUEST_PATH = "/unit/test/mock/request";
    protected static final String REQUEST_PATH_API = AbstractController.API_PATH + "/projects/%s/groups/%s/requests";

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

    protected Project createProject(boolean useRandomCode) {
        return createProject(getProjectBuilder(useRandomCode).build());
    }

    protected Project createProject(final Project project) {
        return projectService.save(project);
    }

    protected Project.ProjectBuilder getProjectBuilder() {
        return getProjectBuilder(false);
    }

    protected Project.ProjectBuilder getProjectBuilder(final boolean useRandomCode) {
        final String id = CommonUtils.generateUniqueId();
        final String code = PROJECT_CODE_TEST + (useRandomCode ? "-" + id  : "");

        return Project.builder()
                .id(id)
                .name(PROJECT_NAME_TEST)
                .code(code)
                .type(ProjectType.HTTP);
    }

    // ===============
    // GROUP HELPERS
    // ===============

    protected Group getGroup(final String groupId) {
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

    protected void deleteGroup(final Group group) {
        groupService.remove(group);
    }

    protected Group createGroup() {
        return createGroup(true);
    }

    protected Group createGroup(final boolean recording) {
        return groupService.save(
                getGroupBuilder().recording(recording).build()
        );
    }

    protected Group createGroup(final String projectId, final boolean recording) {
        return groupService.save(
                getGroupBuilder().projectId(projectId).recording(recording).build()
        );
    }

    protected Group.GroupBuilder getGroupBuilder() {
        return Group.builder()
                .id(CommonUtils.generateUniqueId())
                .projectId(PROJECT_ID)
                .name(GROUP_NAME_TEST)
                .recording(true);
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

    protected AbstractRequest createRequest(AbstractRequest request) {
        return this.requestService.save(request);
    }

    protected ResultActions sendPostRequest(String endpoint, MediaType mediaType, AbstractRequest request) throws Exception {
        String requestJson = new ObjectMapper(new JsonFactory()).writeValueAsString(request);
        return this.mockMvc.perform(post(endpoint).contentType(mediaType).content(requestJson));
    }

    protected static AbstractRequest getNewRequest(String groupId) {
        AbstractRequest request = new AbstractRequest();
        List<Transformer> mapTransformers = ImmutableList.of(new KeyValueTransformer());
        List<Transformer> transformers = ImmutableList.of(new RegexpTransformer(), new XPathTransformer());
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

    // ===============
    // EVENT HELPERS
    // ===============

    protected Event getEvent(final String eventId) {
        return eventService.findById(eventId);
    }

    protected void deleteAllEvents() {
        getAllEvents().forEach((entity) -> eventService.remove(entity));
    }

    protected Iterable<Event> getAllEvents() {
        return eventService.findAll();
    }

    protected Event createEvent() {
        final Project project = getProjectBuilder(true).build();
		final Event event = Event.<Project>builder()
				.eventType(EventType.SAVE)
				.eventDate(new Date())
				.entity(project)
				.username("test.username")
				.build();

        return eventService.save(event);
    }
}

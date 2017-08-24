package org.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockenger.core.config.TestContext;
import org.mockenger.core.config.TestPropertyContext;
import org.mockenger.core.service.EventService;
import org.mockenger.core.service.GroupService;
import org.mockenger.core.service.ProjectService;
import org.mockenger.core.service.RequestService;
import org.mockenger.core.service.account.AccountService;
import org.mockenger.data.model.dict.EventType;
import org.mockenger.data.model.dict.ProjectType;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.log.Event;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.mockenger.data.model.persistent.mock.request.part.Headers;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
import org.mockenger.data.model.persistent.mock.request.part.Parameters;
import org.mockenger.data.model.persistent.mock.request.part.Path;
import org.mockenger.data.model.persistent.mock.response.MockResponse;
import org.mockenger.data.model.persistent.transformer.KeyValueTransformer;
import org.mockenger.data.model.persistent.transformer.RegexpTransformer;
import org.mockenger.data.model.persistent.transformer.Transformer;
import org.mockenger.data.model.persistent.transformer.XPathTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.mockenger.core.util.CommonUtils.generateUniqueId;
import static org.mockenger.core.util.CommonUtils.getCheckSum;
import static org.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


	protected final ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());

    protected MockMvc mockMvc;

    protected static final String PROJECT_ID = "PROJECT_ID";
    protected static final String PROJECT_CODE = "PRJCT";
    protected static final String GROUP_ID = "GROUP_ID";
    protected static final String GROUP_CODE = "GRP";
    protected static final String REQUEST_ID = "REQUEST_ID";
    protected static final String REQUEST_CODE = "RQST";
    protected static final String PROJECT_NAME_TEST = "Unit-test project";
//    protected static final String PROJECT_CODE_TEST = "UTCODE";
    protected static final String GROUP_NAME_TEST = "Unit-test group";
    protected static final String ACCOUNT_FIRST_NAME_TEST = "First name";
    protected static final String ACCOUNT_LAST_NAME_TEST = "Last name";
    protected static final String ACCOUNT_USERNAME_TEST = "UserName";
    protected static final String ACCOUNT_PASSWORD_TEST = "pswd";
    protected static final String REQUEST_NAME_TEST = "Unit-test mock-request";
    protected static final String REQUEST_PATH = "/unit/test/mock/request";
    protected static final String REQUEST_PATH_API = API_PATH + "/projects/%s/groups/%s/requests";

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
        final String id = generateUniqueId();
        final String code = PROJECT_CODE + (useRandomCode ? id.toUpperCase() : "");

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
        getAllGroups().forEach(this::deleteGroup);
    }

    protected void deleteGroup(final Group group) {
        groupService.remove(group);
    }

    protected Group createGroup() {
        return createGroup(true, true);
    }

	protected Group createGroup(final boolean useRandomCode) {
		return createGroup(useRandomCode, true);
	}

    protected Group createGroup(final boolean useRandomCode, final boolean recording) {
        return groupService.save(
        		getGroupBuilder(useRandomCode).recording(recording).build()
        );
    }

    protected Group createGroup(final String projectId, final boolean recording) {
        return groupService.save(
                getGroupBuilder(true).projectId(projectId).recording(recording).build()
        );
    }

    protected Group.GroupBuilder getGroupBuilder() {
        return getGroupBuilder(false);
    }

	protected Group.GroupBuilder getGroupBuilder(final boolean useRandomCode) {
		final String code = GROUP_CODE + (useRandomCode ? generateUniqueId().toUpperCase() : "");

		return Group.builder()
				.id(generateUniqueId())
				.projectId(PROJECT_ID)
				.code(code)
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

    protected AbstractRequest createRequest(final String groupId) {
        final AbstractRequest request = getNewRequest(groupId);

        requestService.save(request);

        return request;
    }

    protected AbstractRequest createRequest(final AbstractRequest request) {
        return requestService.save(request);
    }

    protected ResultActions sendPostRequest(final String endpoint, final MediaType mediaType, final AbstractRequest request) throws Exception {
        final String requestJson = objectMapper.writeValueAsString(request);

        return mockMvc.perform(post(endpoint).contentType(mediaType).content(requestJson));
    }

    protected static AbstractRequest getNewRequest(final String groupId) {
        final AbstractRequest request = new AbstractRequest();
        final List<Transformer> mapTransformers = ImmutableList.of(new KeyValueTransformer());
        final List<Transformer> transformers = ImmutableList.of(new RegexpTransformer(), new XPathTransformer());
        final Set<Pair> headersSet = ImmutableSet.of(new Pair("header1", "H1"), new Pair("header2", "H2"));
        final Set<Pair> paramsSet = ImmutableSet.of(new Pair("A", "1"), new Pair("b", "2"));
        final String id = generateUniqueId();

        request.setId(id);
        request.setGroupId(groupId);
        request.setCode(PROJECT_CODE + "-" + GROUP_CODE +  "-" + id);
        request.setName(REQUEST_NAME_TEST);
        request.setMethod(RequestMethod.POST);
        request.setCreationDate(new Date());
        request.setPath(new Path(transformers, REQUEST_PATH));
        request.setParameters(new Parameters(mapTransformers, paramsSet));
        request.setHeaders(new Headers(mapTransformers, headersSet));
        request.setBody(new Body(transformers, MOCK_REQUEST_BODY));
        request.setMockResponse(new MockResponse(200, headersSet, MOCK_RESPONSE_BODY));
        request.setCheckSum(getCheckSum(request));

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


	protected MvcResult getMvcResult(final MockHttpServletRequestBuilder builder) throws Exception {
		return mockMvc.perform(builder)
//				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(request().asyncStarted())
				.andReturn();
	}


	protected MockHttpServletRequestBuilder withMediaType(final MockHttpServletRequestBuilder builder, final String mediaType) {
		return builder.contentType(parseMediaType(mediaType));
	}


	protected MockHttpServletRequestBuilder withMediaType(final MockHttpServletRequestBuilder builder) {
		return withMediaType(builder, CONTENT_TYPE_JSON_UTF8);
	}
}

package com.socialstartup.mockenger.core.web.controller.base;

import com.socialstartup.mockenger.core.service.GroupService;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.core.web.exception.ObjectNotFoundException;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 *
 */
public abstract class AbstractController {

//    private final String APPLICATION_JSONP_REQUEST_VALUE = "application/javascript";

//    private final String APPLICATION_JSONP_RESPONSE_VALUE = "application/javascript;charset=UTF-8";

    protected static final String CONTENT_TYPE_KEY = "content-type";
    protected static final String MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";
    protected static final String MEDIA_TYPE_XML = MediaType.APPLICATION_XML_VALUE + ";charset=UTF-8";

    protected static final String PROJECTS_ENDPOINT = "/projects";
    protected static final String PROJECT_ID_ENDPOINT = PROJECTS_ENDPOINT + "/{projectId}";
    protected static final String GROUPS_ENDPOINT = "/groups";
    protected static final String GROUP_ID_ENDPOINT = GROUPS_ENDPOINT + "/{groupId}";
    protected static final String REQUESTS_ENDPOINT = "/requests";
    protected static final String REQUEST_ID_ENDPOINT = REQUESTS_ENDPOINT + "/{requestId}";


    private HttpHeaders responseHeaders = new HttpHeaders();

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RequestService requestService;


    /**
     * Constructor with default content-type for responses
     */
    public AbstractController() {
        getResponseHeaders().set(CONTENT_TYPE_KEY, MEDIA_TYPE_JSON);
    }

    protected HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    protected ProjectService getProjectService() {
        return projectService;
    }

    protected GroupService getGroupService() {
        return groupService;
    }

    protected RequestService getRequestService() {
        return requestService;
    }

    /**
     *
     * @param projectId
     * @return
     */
    protected Project findProjectById(String projectId) {
        Project project = getProjectService().findById(projectId);

        if (project == null) {
            throw new ObjectNotFoundException("Project", projectId);
        }

        return project;
    }

    /**
     *
     * @param groupId
     * @return
     */
    protected Group findGroupById(String groupId) {
        Group group = getGroupService().findById(groupId);

        if (group == null) {
            throw new ObjectNotFoundException("Group", groupId);
        }

        return group;
    }

    /**
     *
     * @param requestId
     * @return
     */
    protected AbstractRequest findRequestById(String requestId) {
        AbstractRequest request = getRequestService().findById(requestId);

        if (request == null) {
            throw new ObjectNotFoundException("MockRequest", requestId);
        }

        return request;
    }

    /**
     *
     * @param requestId
     * @return
     */
    protected AbstractRequest findRequestByIdAndUniqueCode(String requestId, String uniqueCode) {
        AbstractRequest request = getRequestService().findByIdAndUniqueCode(requestId, uniqueCode);

        if (request == null) {
            throw new IllegalArgumentException(String.format("Cannot find MockRequest with ID '%s' and unique code '%s'", requestId, uniqueCode));
        }

        return request;
    }
}

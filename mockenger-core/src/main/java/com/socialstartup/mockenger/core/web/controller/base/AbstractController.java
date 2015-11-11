package com.socialstartup.mockenger.core.web.controller.base;

import com.socialstartup.mockenger.commons.utils.JsonHelper;
import com.socialstartup.mockenger.core.service.GroupService;
import com.socialstartup.mockenger.core.service.HttpHeadersService;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.core.web.exception.ObjectNotFoundException;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

/**
 *
 */
public abstract class AbstractController {

    private Logger LOG = LoggerFactory.getLogger(AbstractController.class);

    protected static final String VALUESET_ENDPOINT = "/valueset";
    protected static final String ACCOUNTS_ENDPOINT = "/accounts";
    protected static final String ACCOUNT_ID_ENDPOINT = ACCOUNTS_ENDPOINT + "/{accountId}";
    protected static final String PROJECTS_ENDPOINT = "/projects";
    protected static final String PROJECT_ID_ENDPOINT = PROJECTS_ENDPOINT + "/{projectId}";
    protected static final String GROUPS_ENDPOINT = "/groups";
    protected static final String GROUP_ID_ENDPOINT = GROUPS_ENDPOINT + "/{groupId}";
    protected static final String REQUESTS_ENDPOINT = "/requests";
    protected static final String REQUEST_ID_ENDPOINT = REQUESTS_ENDPOINT + "/{requestId}";

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RequestService requestService;

    @Autowired
    protected HttpHeadersService httpHeadersService;


    protected HttpHeaders getResponseHeaders() {
        return httpHeadersService.getDefaultHeaders();
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

    /**
     *
     * @param mockRequest
     */
    protected void cleanUpRequestBody(AbstractRequest mockRequest) {
        if (mockRequest.getBody() != null && mockRequest.getBody().getValue() != null) {
            String newBody = mockRequest.getBody().getValue().trim();
            if (newBody.startsWith("{") && newBody.endsWith("}")) {
                try {
                    newBody = getRequestService().prepareRequestJsonBody(newBody);
                } catch (IOException e) {
                    LOG.warn("Cannot remove whitespaces from JSON", e);
                }
            } else {
                newBody = getRequestService().prepareRequestXmlBody(newBody);
            }
            mockRequest.getBody().setValue(newBody);
        }
    }

    /**
     *
     * @param mockResponse
     */
    protected void cleanUpResponseBody(MockResponse mockResponse) {
        if (mockResponse != null && mockResponse.getBody() != null) {
            try {
                String newBody = JsonHelper.removeWhitespaces(mockResponse.getBody());
                mockResponse.setBody(newBody);
            } catch (IOException e) {
                LOG.warn("Cannot remove whitespaces from response body (JSON)", e);
            }
        }
    }
}

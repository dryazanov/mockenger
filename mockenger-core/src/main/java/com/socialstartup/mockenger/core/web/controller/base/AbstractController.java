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
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Dmitry Ryazanov
 */
public abstract class AbstractController {

    private final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

    public static final String API_PATH = "/api";

	protected static final String REVOKE_ENDPOINT = API_PATH + "/oauth/revoke";
	protected static final String USER_ENDPOINT = API_PATH + "/oauth/user";

    protected static final String VALUESET_ENDPOINT = API_PATH + "/valueset";

    protected static final String ACCOUNTS_ENDPOINT = API_PATH + "/accounts";
    protected static final String ACCOUNT_ID_ENDPOINT = ACCOUNTS_ENDPOINT + "/{accountId}";

    protected static final String PROJECTS_ENDPOINT = API_PATH + "/projects";
    protected static final String PROJECT_ID_ENDPOINT = PROJECTS_ENDPOINT + "/{projectId}";

    protected static final String GROUPS_ENDPOINT = "/groups";
    protected static final String GROUP_ID_ENDPOINT = GROUPS_ENDPOINT + "/{groupId}";

    protected static final String REQUESTS_ENDPOINT = "/requests";
    protected static final String REQUEST_ID_ENDPOINT = REQUESTS_ENDPOINT + "/{requestId}";

    protected static final String EVENTS_ENDPOINT = API_PATH + "/events";
    protected static final String EVENT_ID_ENDPOINT = EVENTS_ENDPOINT + "/{eventId}";

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
    protected Project findProjectById(final String projectId) {
        return Optional.ofNullable(getProjectService().findById(projectId))
                .orElseThrow(() -> new ObjectNotFoundException("Project", projectId));
    }

    /**
     *
     * @param groupId
     * @return
     */
    protected Group findGroupById(final String groupId) {
        return Optional.ofNullable(getGroupService().findById(groupId))
                .orElseThrow(() -> new ObjectNotFoundException("Group", groupId));
    }

    /**
     *
     * @param requestId
     * @return
     */
    protected AbstractRequest findRequestById(final String requestId) {
        return Optional.ofNullable(getRequestService().findById(requestId))
                .orElseThrow(() -> new ObjectNotFoundException("MockRequest", requestId));
    }

    /**
     *
     * @param requestId
     * @return
     */
    protected AbstractRequest findRequestByIdAndUniqueCode(final String requestId, final String uniqueCode) {
        return Optional.ofNullable(getRequestService().findByIdAndUniqueCode(requestId, uniqueCode))
                .orElseThrow(() -> new IllegalArgumentException("Cannot find MockRequest with ID '" +
                        requestId + "' and unique code '" + uniqueCode + "'"));
    }

    /**
     *
     * @param mockRequest
     */
    protected void cleanUpRequestBody(final GenericRequest mockRequest) {
        if (mockRequest.getBody() != null && !StringUtils.isEmpty(mockRequest.getBody().getValue())) {
            final String body = mockRequest.getBody().getValue().trim();

            if (body.startsWith("{") && body.endsWith("}")) {
                try {
                    final String jsonBody = getRequestService().prepareRequestJsonBody(body);
                    mockRequest.getBody().setValue(jsonBody);
                } catch (IOException e) {
                    LOG.warn("Cannot remove whitespaces from JSON", e);
                }
            } else {
                final String xmlBody = getRequestService().prepareRequestXmlBody(body);
                mockRequest.getBody().setValue(xmlBody);
            }
        }
    }

    /**
     *
     * @param mockResponse
     */
    protected void cleanUpResponseBody(final MockResponse mockResponse) {
        if (mockResponse != null && mockResponse.getBody() != null) {
            try {
                final String newBody = JsonHelper.removeWhitespaces(mockResponse.getBody());
                mockResponse.setBody(newBody);
            } catch (IOException e) {
                LOG.warn("Cannot remove whitespaces from response body (JSON)", e);
            }
        }
    }
}

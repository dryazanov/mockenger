package org.mockenger.core.web.controller.base;

import org.mockenger.commons.utils.JsonHelper;
import org.mockenger.commons.utils.XmlHelper;
import org.mockenger.core.service.GroupService;
import org.mockenger.core.service.HttpHeadersService;
import org.mockenger.core.service.ProjectService;
import org.mockenger.core.service.RequestService;
import org.mockenger.core.web.exception.ObjectNotFoundException;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static org.mockenger.core.util.CommonUtils.isStartEndWith;
import static java.util.Optional.ofNullable;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Dmitry Ryazanov
 */
public abstract class AbstractController {

    private final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

    public static final String API_PATH = "/api";
    public static final String MOCK_HTTP_TYPE_PATH = API_PATH + "/HTTP";

    public static final String PROJECTS_ENDPOINT = API_PATH + "/projects";
    protected static final String PROJECT_ID_ENDPOINT = PROJECTS_ENDPOINT + "/{projectId}";

    protected static final String GROUPS_ENDPOINT = PROJECT_ID_ENDPOINT + "/groups";
    protected static final String GROUP_ID_ENDPOINT = GROUPS_ENDPOINT + "/{groupId}";

    protected static final String REQUESTS_ENDPOINT = GROUP_ID_ENDPOINT + "/requests";
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
    protected Project findProjectById(final String projectId) {
        return ofNullable(getProjectService().findById(projectId))
                .orElseThrow(() -> new ObjectNotFoundException("Project", projectId));
    }


    /**
     *
     * @param groupId
     * @return
     */
    protected Group findGroupById(final String groupId) {
        return ofNullable(getGroupService().findById(groupId))
                .orElseThrow(() -> new ObjectNotFoundException("Group", groupId));
    }


    /**
     *
     * @param requestId
     * @return
     */
    protected AbstractRequest findRequestById(final String requestId) {
        return ofNullable(getRequestService().findById(requestId))
                .orElseThrow(() -> new ObjectNotFoundException("MockRequest", requestId));
    }


    /**
     *
     * @param requestId
     * @return
     */
    protected AbstractRequest findRequestByIdAndUniqueCode(final String requestId, final String uniqueCode) {
        return ofNullable(getRequestService().findByIdAndUniqueCode(requestId, uniqueCode))
                .orElseThrow(() -> new IllegalArgumentException("Cannot find MockRequest with ID '" +
                        requestId + "' and unique code '" + uniqueCode + "'"));
    }


    /**
     *
     * @param mockRequest
     */
    protected GenericRequest cleanUpRequestBody(final GenericRequest mockRequest) {
        if (mockRequest.getBody() != null) {
            final String body = mockRequest.getBody().getValue();

			if (!isEmpty(body)) {
				mockRequest.getBody().setValue(removeWhitespaces(body));
			}
        }

        return mockRequest;
    }

    protected String removeWhitespaces(final String body) {
    	try {
			if (isStartEndWith(body.trim(), "{", "}")) {
				return JsonHelper.removeWhitespaces(body);
			} else if (isStartEndWith(body.trim(), "<", ">")) {
				return XmlHelper.removeWhitespaces(body);
			}
		} catch (Exception e) {
    		LOG.warn("Cannot remove whitespaces from the string", e);
		}

		return body;
	}

	protected <T> ResponseEntity<T> okResponseWithDefaultHeaders(final T body) {
		return ok().headers(getResponseHeaders()).body(body);
	}


	protected ResponseEntity noContentWithDefaultHeaders() {
		return noContent().headers(getResponseHeaders()).build();
	}


	protected ResponseEntity notFoundWithDefaultHeaders() {
		return notFound().headers(getResponseHeaders()).build();
	}
}

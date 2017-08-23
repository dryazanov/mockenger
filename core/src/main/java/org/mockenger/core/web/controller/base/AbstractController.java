package org.mockenger.core.web.controller.base;

import org.mockenger.core.service.GroupService;
import org.mockenger.core.service.HttpHeadersService;
import org.mockenger.core.service.ProjectService;
import org.mockenger.core.service.RequestService;
import org.mockenger.core.web.exception.ObjectNotFoundException;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static java.util.Optional.ofNullable;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author Dmitry Ryazanov
 */
public abstract class AbstractController {

    private final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

    public static final String API_PATH = "/api";
    public static final String MOCK_HTTP_TYPE_PATH = API_PATH + "/HTTP";

    public static final String PROJECTS_ENDPOINT = API_PATH + "/projects";
    protected static final String PROJECT_CODE_ENDPOINT = PROJECTS_ENDPOINT + "/{projectCode}";

    protected static final String GROUPS_ENDPOINT = PROJECT_CODE_ENDPOINT + "/groups";
    protected static final String GROUP_CODE_ENDPOINT = GROUPS_ENDPOINT + "/{groupCode}";

    protected static final String REQUESTS_ENDPOINT = GROUP_CODE_ENDPOINT + "/requests";
    protected static final String REQUEST_CODE_ENDPOINT = REQUESTS_ENDPOINT + "/{requestCode}";

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
	 * @param projectCode
	 * @return
	 */
	protected Project findProjectByCode(final String projectCode) {
		return ofNullable(getProjectService().findByCode(projectCode))
				.orElseThrow(() -> new ObjectNotFoundException("Project", projectCode));
	}


	/**
	 *
	 * @param groupCode
	 * @return
	 */
	protected Group findGroupByCode(final String groupCode) {
		return ofNullable(getGroupService().findByCode(groupCode))
				.orElseThrow(() -> new ObjectNotFoundException("Group", groupCode));
	}


	/**
	 *
	 * @param requestCode
	 * @return
	 */
	protected AbstractRequest findRequestByCode(final String requestCode) {
		return ofNullable(getRequestService().findByCode(requestCode))
				.orElseThrow(() -> new ObjectNotFoundException("MockRequest", requestCode));
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


	protected String getUniqueCode(final Group group) {
		final String projectId = group.getProjectId();
		final Project project = ofNullable(findProjectById(projectId)).orElseThrow(() -> new ObjectNotFoundException("Project", projectId));

		return getUniqueCode(project, group);
	}

	protected String getUniqueCode(final Project project, final Group group) {
		final long nextSequenceValue = getProjectService().getNextSequenceValue(project.getId());

		return String.format("%s-%s-%d", project.getCode(), group.getCode(), nextSequenceValue);
	}
}

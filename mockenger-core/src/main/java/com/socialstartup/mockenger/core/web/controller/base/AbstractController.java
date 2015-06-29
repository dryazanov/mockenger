package com.socialstartup.mockenger.core.web.controller.base;

import com.socialstartup.mockenger.core.service.GroupService;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.core.web.exception.ObjectNotFoundException;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;


//@Controller
public class AbstractController {

//    private final String APPLICATION_JSONP_REQUEST_VALUE = "application/javascript";

//    private final String APPLICATION_JSONP_RESPONSE_VALUE = "application/javascript;charset=UTF-8";

    private HttpHeaders responseHeaders = new HttpHeaders();

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RequestService requestService;


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
            throw new ObjectNotFoundException(project, projectId);
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
            throw new ObjectNotFoundException(group, groupId);
        }

        return group;
    }
}

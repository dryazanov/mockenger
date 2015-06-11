package org.mockenger.dev.web;

import org.mockenger.dev.model.mock.group.GroupEntity;
import org.mockenger.dev.service.GroupService;
import org.mockenger.dev.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;


@Controller
public class CommonController {

    private final String APPLICATION_JSONP_REQUEST_VALUE = "application/javascript";

    private final String APPLICATION_JSONP_RESPONSE_VALUE = "application/javascript;charset=UTF-8";

    private HttpHeaders responseHeaders = new HttpHeaders();

    @Autowired
    private GroupService groupService;

    @Autowired
    private RequestService requestService;


    public HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    protected GroupService getGroupService() {
        return groupService;
    }

    public RequestService getRequestService() {
        return requestService;
    }

    /**
     *
     * @param groupId
     * @return
     */
    protected GroupEntity findGroupById(String groupId) {
        GroupEntity groupEntity = getGroupService().findById(groupId);

        if (groupEntity == null) {
            // TODO: Create and throw GroupNotFoundException
            throw new RuntimeException("Group with ID '" + groupId + "' not found");
        }

        return groupEntity;
    }
}

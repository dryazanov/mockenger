package com.socialstartup.mockenger.frontend.controller;

import com.socialstartup.mockenger.frontend.service.GroupService;
import com.socialstartup.mockenger.frontend.service.RequestService;
import com.socialstartup.mockenger.model.mock.group.GroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;


//@Controller
public class CommonController {

//    private final String APPLICATION_JSONP_REQUEST_VALUE = "application/javascript";

//    private final String APPLICATION_JSONP_RESPONSE_VALUE = "application/javascript;charset=UTF-8";

    private HttpHeaders responseHeaders = new HttpHeaders();

    @Autowired
    private GroupService groupService;

    @Autowired
    private RequestService requestService;


    protected HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    protected GroupService getGroupService() {
        return groupService;
    }

    protected RequestService getRequestService() {
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

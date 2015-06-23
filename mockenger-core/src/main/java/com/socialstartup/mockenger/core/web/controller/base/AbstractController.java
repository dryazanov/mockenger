package com.socialstartup.mockenger.core.web.controller.base;

import com.socialstartup.mockenger.core.service.GroupService;
import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;


//@Controller
public class AbstractController {

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
    protected Profile findGroupById(String groupId) {
        Profile profile = getGroupService().findById(groupId);

        if (profile == null) {
            // TODO: Create and throw GroupNotFoundException
            throw new RuntimeException("Group with ID '" + groupId + "' not found");
        }

        return profile;
    }
}

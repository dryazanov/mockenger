package com.socialstartup.mockenger.core.service.common;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.DeleteRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class DeleteService extends RequestService {
    public DeleteRequest createMockRequest(String groupId, HttpServletRequest request) {
        return (DeleteRequest) fillUpEntity(new DeleteRequest(), groupId, request);
    }
}

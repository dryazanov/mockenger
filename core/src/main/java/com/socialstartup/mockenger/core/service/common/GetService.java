package com.socialstartup.mockenger.core.service.common;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GetRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
 */
@Component
public class GetService extends RequestService {
    public GetRequest createMockRequest(String groupId, HttpServletRequest request) {
        return (GetRequest) fillUpEntity(new GetRequest(), groupId, request);
    }
}

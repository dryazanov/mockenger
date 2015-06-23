package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.HeadRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class HeadService extends RequestService {
    public HeadRequest createMockRequest(String groupId, HttpServletRequest request) {
        return (HeadRequest) fillUpEntity(new HeadRequest(), groupId, request);
    }
}

package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.TraceRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
 */
@Component
public class TraceService extends RequestService {
    public TraceRequest createMockRequest(String groupId, HttpServletRequest request) {
        return (TraceRequest) fillUpEntity(new TraceRequest(), groupId, request);
    }
}

package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.HeadRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class HeadService extends RequestService {
    public HeadRequest createMockRequest(final String groupId, final HttpServletRequest request) {
        return (HeadRequest) fillUpEntity(new HeadRequest(), groupId, request);
    }
}

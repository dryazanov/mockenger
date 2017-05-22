package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.OptionsRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class OptionsService extends RequestService {
    public OptionsRequest createMockRequest(final String groupId, final HttpServletRequest request) {
        return (OptionsRequest) fillUpEntity(new OptionsRequest(), groupId, request);
    }
}

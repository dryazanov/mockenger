package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.OptionsRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class OptionsService extends RequestService {
    public OptionsRequest createMockRequest(String groupId, HttpServletRequest request) {
        return (OptionsRequest) fillUpEntity(new OptionsRequest(), groupId, request);
    }
}

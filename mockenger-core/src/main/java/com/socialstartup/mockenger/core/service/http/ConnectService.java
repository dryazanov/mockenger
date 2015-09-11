package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.ConnectRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
 */
@Component
public class ConnectService extends RequestService {
    public ConnectRequest createMockRequest(String groupId, HttpServletRequest request) {
        return (ConnectRequest) fillUpEntity(new ConnectRequest(), groupId, request);
    }
}

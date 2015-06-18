package com.socialstartup.mockenger.core.service.common;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.mock.request.entity.GetEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class GetService extends RequestService {
    public GetEntity createMockRequest(String groupId, HttpServletRequest request) {
        return (GetEntity) fillUpEntity(new GetEntity(), groupId, request);
    }
}

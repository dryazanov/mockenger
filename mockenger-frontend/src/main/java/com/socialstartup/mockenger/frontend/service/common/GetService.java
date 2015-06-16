package com.socialstartup.mockenger.frontend.service.common;

import com.socialstartup.mockenger.frontend.service.RequestService;
import com.socialstartup.mockenger.model.mock.request.entity.GetEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class GetService extends RequestService {
    public GetEntity createMockRequest(String groupId, HttpServletRequest request) {
        GetEntity getEntity = new GetEntity();
        BodilessService.fillUpEntity(getEntity, groupId, request);
        return getEntity;
    }
}

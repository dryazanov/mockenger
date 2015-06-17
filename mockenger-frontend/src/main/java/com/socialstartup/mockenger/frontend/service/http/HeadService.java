package com.socialstartup.mockenger.frontend.service.http;

import com.socialstartup.mockenger.frontend.service.RequestService;
import com.socialstartup.mockenger.frontend.service.common.BodilessService;
import com.socialstartup.mockenger.data.model.mock.request.entity.HeadEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class HeadService extends RequestService {
    public HeadEntity createMockRequest(String groupId, HttpServletRequest request) {
        HeadEntity headEntity = new HeadEntity();
        BodilessService.fillUpEntity(headEntity, groupId, request);
        return headEntity;
    }
}

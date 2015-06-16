package com.socialstartup.mockenger.frontend.service.http;

import com.socialstartup.mockenger.frontend.service.RequestService;
import com.socialstartup.mockenger.frontend.service.common.BodilessService;
import com.socialstartup.mockenger.model.mock.request.entity.TraceEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class TraceService extends RequestService {
    public TraceEntity createMockRequest(String groupId, HttpServletRequest request) {
        TraceEntity traceEntity = new TraceEntity();
        BodilessService.fillUpEntity(traceEntity, groupId, request);
        return traceEntity;
    }
}

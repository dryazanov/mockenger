package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.mock.request.entity.HeadEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class HeadService extends RequestService {
    public HeadEntity createMockRequest(String groupId, HttpServletRequest request) {
        return (HeadEntity) fillUpEntity(new HeadEntity(), groupId, request);
    }
}

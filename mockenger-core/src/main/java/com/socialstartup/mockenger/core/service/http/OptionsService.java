package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.mock.request.entity.OptionsEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class OptionsService extends RequestService {
    public OptionsEntity createMockRequest(String groupId, HttpServletRequest request) {
        return (OptionsEntity) fillUpEntity(new OptionsEntity(), groupId, request);
    }
}

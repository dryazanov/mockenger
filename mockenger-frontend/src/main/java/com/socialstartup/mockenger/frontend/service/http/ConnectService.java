package com.socialstartup.mockenger.frontend.service.http;

import com.socialstartup.mockenger.frontend.service.RequestService;
import com.socialstartup.mockenger.frontend.service.common.BodilessService;
import com.socialstartup.mockenger.model.mock.request.entity.ConnectEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class ConnectService extends RequestService {
    public ConnectEntity createMockRequest(String groupId, HttpServletRequest request) {
        ConnectEntity connectEntity = new ConnectEntity();
        BodilessService.fillUpEntity(connectEntity, groupId, request);
        return connectEntity;
    }
}

package com.socialstartup.mockenger.frontend.service.common;

import com.socialstartup.mockenger.frontend.service.RequestService;
import com.socialstartup.mockenger.data.model.mock.request.entity.DeleteEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class DeleteService extends RequestService {
    public DeleteEntity createMockRequest(String groupId, HttpServletRequest request) {
        DeleteEntity deleteEntity = new DeleteEntity();
        BodilessService.fillUpEntity(deleteEntity, groupId, request);
        return deleteEntity;
    }
}

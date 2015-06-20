package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.mock.request.entity.PatchEntity;
import com.socialstartup.mockenger.data.model.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class PatchService extends RequestService {
    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     */
    public PatchEntity createMockRequest(String groupId, String requestBody, HttpServletRequest request) {
        // TODO: Think about the best way to store requestBody. Now we store requestBody as it is,
        // TODO: maybe we should replace whitespaces or encode somehow.
        Body body = new Body(requestBody);
        return (PatchEntity) fillUpEntity(new PatchEntity(body), groupId, request);
    }
}

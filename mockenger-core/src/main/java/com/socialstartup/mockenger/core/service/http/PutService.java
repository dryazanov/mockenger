package com.socialstartup.mockenger.core.service.http;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.mock.request.entity.PutEntity;
import com.socialstartup.mockenger.data.model.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component(value = "httpPutService")
public class PutService extends RequestService {

    public PutEntity createMockRequest(String groupId, String requestBody, HttpServletRequest request) {
        // TODO: Think about the best way to store requestBody. Now we store requestBody as it is,
        // TODO: maybe we should replace whitespaces or encode somehow.
        Body body = new Body(requestBody);
        return (PutEntity) fillUpEntity(new PutEntity(body), groupId, request);
    }
}

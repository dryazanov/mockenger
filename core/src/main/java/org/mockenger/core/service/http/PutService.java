package org.mockenger.core.service.http;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.PutRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
 */
@Component(value = "httpPutService")
public class PutService extends RequestService {

    public PutRequest createMockRequest(String groupId, String requestBody, HttpServletRequest request) {
        // TODO: Think about the best way to store requestBody. Now we store requestBody as it is,
        // TODO: maybe we should replace whitespaces or encode somehow.
        Body body = new Body(requestBody);
        return (PutRequest) fillUpEntity(new PutRequest(body), groupId, request);
    }
}

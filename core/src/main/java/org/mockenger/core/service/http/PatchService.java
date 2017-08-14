package org.mockenger.core.service.http;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.PatchRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class PatchService extends RequestService {
    public PatchRequest createMockRequest(final String groupId, final String requestBody, final HttpServletRequest request) {
        final Body body = new Body(requestBody);
        return (PatchRequest) fillUpEntity(new PatchRequest(body), groupId, request);
    }
}

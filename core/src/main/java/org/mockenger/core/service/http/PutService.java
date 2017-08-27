package org.mockenger.core.service.http;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.PutRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dmitry Ryazanov
 */
@Component(value = "httpPutService")
public class PutService extends RequestService {
    public PutRequest createMockRequest(final String groupId, final String requestBody, final HttpServletRequest request) {
		return createMockRequest(new PutRequest(new Body(requestBody)), groupId, request);
    }
}

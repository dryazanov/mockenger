package org.mockenger.core.service.http;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.HeadRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class HeadService extends RequestService {
    public HeadRequest createMockRequest(final String groupId, final HttpServletRequest request) {
        return fillUpEntity(new HeadRequest(), groupId, request);
    }
}

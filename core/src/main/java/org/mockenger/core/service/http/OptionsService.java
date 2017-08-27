package org.mockenger.core.service.http;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.OptionsRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class OptionsService extends RequestService {
    public OptionsRequest createMockRequest(final String groupId, final HttpServletRequest request) {
        return fillUpEntity(new OptionsRequest(), groupId, request);
    }
}

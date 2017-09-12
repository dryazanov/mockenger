package org.mockenger.core.service.common;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.DeleteRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
 */
@Component
public class DeleteService extends RequestService {
    public DeleteRequest createMockRequest(String groupId, HttpServletRequest request) {
        return fillUpEntity(new DeleteRequest(), groupId, request);
    }
}

package org.mockenger.core.service.http;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dmitry Ryazanov
 */
@Component(value = "httpPostService")
public class PostService extends RequestService {
	public PostRequest createGenericRequest(final String groupId, final String requestBody, final HttpServletRequest request) {
		return createMockRequest(new PostRequest(new Body(requestBody)), groupId, request);
	}
}

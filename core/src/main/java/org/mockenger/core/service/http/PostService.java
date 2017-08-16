package org.mockenger.core.service.http;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dmitry Ryazanov
 */
@Component(value = "httpPostService")
public class PostService extends RequestService {

    public PostRequest createMockRequest(final String groupId, final String requestBody, final HttpServletRequest request) {
        // TODO: Think about the best way to store requestBody. Now we store requestBody as it is,
        // TODO: maybe we should replace whitespaces or encode somehow.
        final Body body = new Body(requestBody);
        return (PostRequest) fillUpEntity(new PostRequest(body), groupId, request);
    }

	public GenericRequest createGenericRequest(final String groupId, final String requestBody, final HttpServletRequest request) {
		// TODO: Think about the best way to store requestBody. Now we store requestBody as it is,
		// TODO: maybe we should replace whitespaces or encode somehow.
		final Body body = new Body(requestBody);

		GenericRequest genericRequest = new GenericRequest();
		genericRequest.setMethod(RequestMethod.POST);
		genericRequest.setBody(body);

		return fillUpEntity(genericRequest, groupId, request);
	}
}

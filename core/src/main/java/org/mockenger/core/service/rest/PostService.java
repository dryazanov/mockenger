package org.mockenger.core.service.rest;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.mockenger.core.util.MockRequestUtils.prepareJsonBody;
import static org.mockenger.core.util.MockRequestUtils.prepareXmlBody;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Dmitry Ryazanov
 */
@Component(value = "restPostService")
public class PostService extends RequestService {

    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     * @throws IOException
     */
    public PostRequest createMockRequestFromJson(final String groupId, final String requestBody,
													final HttpServletRequest request) throws IOException {

        final String preparedBody = (isEmpty(requestBody) ? "" : prepareJsonBody(requestBody));

        return createRequest(groupId, preparedBody, request);
    }


    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     * @throws TransformerException
     */
    public PostRequest createMockRequestFromXml(final String groupId, final String requestBody, final HttpServletRequest request) {
        return createRequest(groupId, prepareXmlBody(requestBody), request);
    }


	/**
	 *
	 * @param groupId
	 * @param requestBody
	 * @param request
	 * @return
	 */
	private PostRequest createRequest(final String groupId, final String requestBody, final HttpServletRequest request) {
        return fillUpEntity(new PostRequest(new Body(requestBody)), groupId, request);
    }
}

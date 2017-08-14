package org.mockenger.core.service.rest;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.request.PostRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;
import java.io.IOException;

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
    public GenericRequest createMockRequestFromJson(final String groupId, final String requestBody,
													final HttpServletRequest request) throws IOException {

        final String preparedBody = (isEmpty(requestBody) ? "" : prepareRequestJsonBody(requestBody));
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
    public GenericRequest createMockRequestFromXml(final String groupId, final String requestBody, final HttpServletRequest request) {
        return createRequest(groupId, prepareRequestXmlBody(requestBody), request);
    }


	/**
	 *
	 * @param groupId
	 * @param requestBody
	 * @param request
	 * @return
	 */
	private GenericRequest createRequest(final String groupId, final String requestBody, final HttpServletRequest request) {
        return fillUpEntity(new PostRequest(new Body(requestBody)), groupId, request);
    }
}

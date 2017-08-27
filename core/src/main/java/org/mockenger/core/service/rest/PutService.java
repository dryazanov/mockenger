package org.mockenger.core.service.rest;

import org.mockenger.core.service.RequestService;
import org.mockenger.data.model.persistent.mock.request.PutRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.mockenger.data.model.persistent.transformer.TransformerException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.mockenger.core.util.MockRequestUtils.prepareJsonBody;
import static org.mockenger.core.util.MockRequestUtils.prepareXmlBody;

/**
 * @author Dmitry Ryazanov
 */
@Component(value = "restPutService")
public class PutService extends RequestService {
    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     * @throws IOException
     */
    public PutRequest createMockRequestFromJson(final String groupId,
												final String requestBody,
												final HttpServletRequest request) throws IOException {

        return createRequest(groupId, prepareJsonBody(requestBody), request);
    }


    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     * @throws javax.xml.transform.TransformerException
     */
    public PutRequest createMockRequestFromXml(final String groupId,
											   final String requestBody,
											   final HttpServletRequest request) throws TransformerException {

        return createRequest(groupId, prepareXmlBody(requestBody), request);
    }


    private PutRequest createRequest(final String groupId, final String requestBody, final HttpServletRequest request) {
        return fillUpEntity(new PutRequest(new Body(requestBody)), groupId, request);
    }
}

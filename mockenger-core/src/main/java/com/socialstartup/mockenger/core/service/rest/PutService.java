package com.socialstartup.mockenger.core.service.rest;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PutRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.transformer.TransformerException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
    public GenericRequest createMockRequestFromJson(final String groupId, final String requestBody,
													final HttpServletRequest request) throws IOException {

        return createRequest(groupId, prepareRequestJsonBody(requestBody), request);
    }

    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     * @throws javax.xml.transform.TransformerException
     */
    public GenericRequest createMockRequestFromXml(final String groupId, final String requestBody,
												   final HttpServletRequest request) throws TransformerException {

        return createRequest(groupId, prepareRequestXmlBody(requestBody), request);
    }

    private GenericRequest createRequest(final String groupId, final String requestBody, final HttpServletRequest request) {
        return fillUpEntity(new PutRequest(new Body(requestBody)), groupId, request);
    }
}

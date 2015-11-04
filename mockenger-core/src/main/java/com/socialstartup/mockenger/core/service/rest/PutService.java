package com.socialstartup.mockenger.core.service.rest;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PutRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.persistent.transformer.TransformerException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
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
    public PutRequest createMockRequestFromJson(String groupId, String requestBody, HttpServletRequest request) throws IOException {
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
    public PutRequest createMockRequestFromXml(String groupId, String requestBody, HttpServletRequest request) throws TransformerException {
        return createRequest(groupId, prepareRequestXmlBody(requestBody), request);
    }

    private PutRequest createRequest(String groupId, String requestBody, HttpServletRequest request) {
        Body body = new Body(requestBody);
        return (PutRequest) fillUpEntity(new PutRequest(body), groupId, request);
    }
}

package com.socialstartup.mockenger.core.service.rest;

import com.socialstartup.mockenger.core.service.RequestService;
import com.socialstartup.mockenger.data.model.persistent.mock.request.PostRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Body;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
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
    public PostRequest createMockRequestFromJson(String groupId, String requestBody, HttpServletRequest request) throws IOException {
        return createRequest(groupId, prepareRequestJsonBody(requestBody), request);
    }

    /**
     *
     * @param groupId
     * @param requestBody
     * @param request
     * @return
     * @throws TransformerException
     */
    public PostRequest createMockRequestFromXml(String groupId, String requestBody, HttpServletRequest request) {
        return createRequest(groupId, prepareRequestXmlBody(requestBody), request);
    }

    private PostRequest createRequest(String groupId, String requestBody, HttpServletRequest request) {
        Body body = new Body(requestBody);
        return (PostRequest) fillUpEntity(new PostRequest(body), groupId, request);
    }
}

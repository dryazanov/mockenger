package org.mockenger.dev.service.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockenger.dev.common.HttpUtils;
import org.mockenger.dev.model.mock.request.IRequestEntity;
import org.mockenger.dev.model.mock.request.part.Body;
import org.mockenger.dev.model.mock.request.part.Headers;
import org.mockenger.dev.model.mock.request.part.Parameters;
import org.mockenger.dev.model.mock.request.part.Path;
import org.mockenger.dev.model.mock.request.rest.PostEntity;
import org.mockenger.dev.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class PostService<T extends IRequestEntity> extends RequestService<T> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(PostService.class);


    public T createMockRequest(String groupId, String requestBody, HttpServletRequest request) throws IOException {
        // Prepare request body
        // Convert String to String using ObjectMapper to remove whitespaces
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        String strMsg = mapper.writeValueAsString(mapper.readTree(requestBody));
        String checkSum = DigestUtils.md5DigestAsHex(strMsg.getBytes());

        Body body = new Body(strMsg);
        Path path = new Path(HttpUtils.getUrlPath(request));
        Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        T mockRequest = (T) new PostEntity();
        mockRequest.setGroupId(groupId);
        mockRequest.setCreationDate(new Date());
        mockRequest.setBody(body);
        mockRequest.setPath(path);
        mockRequest.setHeaders(headers);
        mockRequest.setParameters(parameters);
        mockRequest.setCheckSum(checkSum);

        return mockRequest;
    }
}
package com.socialstartup.mockenger.frontend.service.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.frontend.common.HttpUtils;
import com.socialstartup.mockenger.model.mock.request.part.Headers;
import com.socialstartup.mockenger.model.mock.request.IRequestEntity;
import com.socialstartup.mockenger.model.mock.request.part.Body;
import com.socialstartup.mockenger.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.model.mock.request.part.Path;
import com.socialstartup.mockenger.model.mock.request.rest.PutEntity;
import com.socialstartup.mockenger.frontend.service.RequestService;
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
public class PutService<T extends IRequestEntity> extends RequestService<T> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(PutService.class);


    public T createMockRequest(String groupId, String requestBody, HttpServletRequest request) throws IOException {
        // Prepare request body
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        String strMsg = mapper.writeValueAsString(mapper.readTree(requestBody));
        String checkSum = DigestUtils.md5DigestAsHex(strMsg.getBytes());

        Body body = new Body(strMsg);
        Path path = new Path(HttpUtils.getUrlPath(request));
        Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        T mockRequest = (T) new PutEntity();
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

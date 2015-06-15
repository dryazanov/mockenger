package com.socialstartup.mockenger.frontend.service.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.frontend.common.CommonUtils;
import com.socialstartup.mockenger.frontend.common.HttpUtils;
import com.socialstartup.mockenger.frontend.service.RequestService;
import com.socialstartup.mockenger.model.mock.request.entity.PutEntity;
import com.socialstartup.mockenger.model.mock.request.part.Body;
import com.socialstartup.mockenger.model.mock.request.part.Headers;
import com.socialstartup.mockenger.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.model.mock.request.part.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class PutService extends RequestService {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(PutService.class);


    public PutEntity createMockRequest(String groupId, String requestBody, HttpServletRequest request) throws IOException {
        // Prepare request body
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        String strMsg = mapper.writeValueAsString(mapper.readTree(requestBody));

        Body body = new Body(strMsg);
        Path path = new Path(HttpUtils.getUrlPath(request));
        Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        PutEntity mockRequest = new PutEntity();
        mockRequest.setGroupId(groupId);
        mockRequest.setCreationDate(new Date());
        mockRequest.setBody(body);
        mockRequest.setPath(path);
        mockRequest.setHeaders(headers);
        mockRequest.setParameters(parameters);
        mockRequest.setCheckSum(CommonUtils.getCheckSum(mockRequest));

        return mockRequest;
    }
}

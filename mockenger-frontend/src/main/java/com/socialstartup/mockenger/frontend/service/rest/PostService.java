package com.socialstartup.mockenger.frontend.service.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.frontend.common.CommonUtils;
import com.socialstartup.mockenger.frontend.common.HttpUtils;
import com.socialstartup.mockenger.frontend.service.RequestService;
import com.socialstartup.mockenger.data.model.mock.request.entity.PostEntity;
import com.socialstartup.mockenger.data.model.mock.request.part.Body;
import com.socialstartup.mockenger.data.model.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.mock.request.part.Path;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component(value = "restPostService")
public class PostService extends RequestService {

    public PostEntity createMockRequest(String groupId, String requestBody, HttpServletRequest request) throws IOException {
        // Prepare request body
        // Convert String to String using ObjectMapper to remove whitespaces
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        String strMsg = mapper.writeValueAsString(mapper.readTree(requestBody));

        Body body = new Body(strMsg);
        Path path = new Path(HttpUtils.getUrlPath(request));
        Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        PostEntity mockRequest = new PostEntity();
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

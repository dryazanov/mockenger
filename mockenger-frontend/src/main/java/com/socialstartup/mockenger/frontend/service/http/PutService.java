package com.socialstartup.mockenger.frontend.service.http;

import com.socialstartup.mockenger.frontend.common.CommonUtils;
import com.socialstartup.mockenger.frontend.common.HttpUtils;
import com.socialstartup.mockenger.frontend.service.RequestService;
import com.socialstartup.mockenger.model.mock.request.entity.PutEntity;
import com.socialstartup.mockenger.model.mock.request.part.Body;
import com.socialstartup.mockenger.model.mock.request.part.Headers;
import com.socialstartup.mockenger.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.model.mock.request.part.Path;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component(value = "httpPutService")
public class PutService extends RequestService {

    public PutEntity createMockRequest(String groupId, String requestBody, HttpServletRequest request) {
        // TODO: Think about the best way to store requestBody. Now we store requestBody as it is,
        // TODO: maybe we should replace whitespaces or encode somehow.
        Body body = new Body(requestBody);
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

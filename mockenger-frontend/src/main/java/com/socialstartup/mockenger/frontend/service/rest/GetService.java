package com.socialstartup.mockenger.frontend.service.rest;

import com.socialstartup.mockenger.frontend.common.HttpUtils;
import com.socialstartup.mockenger.model.mock.request.IRequestEntity;
import com.socialstartup.mockenger.model.mock.request.part.Headers;
import com.socialstartup.mockenger.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.model.mock.request.part.Path;
import com.socialstartup.mockenger.model.mock.request.rest.GetEntity;
import com.socialstartup.mockenger.frontend.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class GetService<T extends IRequestEntity> extends RequestService<T> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(GetService.class);


    public T createMockRequest(String groupId, HttpServletRequest request) {
        Path path = new Path(HttpUtils.getUrlPath(request));
        Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        T mockRequest = (T) new GetEntity();
        mockRequest.setGroupId(groupId);
        mockRequest.setCreationDate(new Date());
//        mockRequest.setBody(null);
        mockRequest.setPath(path);
        mockRequest.setHeaders(headers);
        mockRequest.setParameters(parameters);

        String checkSum = DigestUtils.md5DigestAsHex(mockRequest.toString().getBytes());
        mockRequest.setCheckSum(checkSum);

        return mockRequest;
    }
}

package com.socialstartup.mockenger.frontend.service.common;

import com.socialstartup.mockenger.frontend.common.CommonUtils;
import com.socialstartup.mockenger.frontend.common.HttpUtils;
import com.socialstartup.mockenger.model.mock.request.RequestEntity;
import com.socialstartup.mockenger.model.mock.request.part.Headers;
import com.socialstartup.mockenger.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.model.mock.request.part.Path;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by x079089 on 6/16/2015.
 */
public class BodilessService {

    public static void fillUpEntity(RequestEntity mockRequest, String groupId, HttpServletRequest request) {
        Path path = new Path(HttpUtils.getUrlPath(request));
        Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        mockRequest.setGroupId(groupId);
        mockRequest.setCreationDate(new Date());
        mockRequest.setPath(path);
        mockRequest.setHeaders(headers);
        mockRequest.setParameters(parameters);
        mockRequest.setCheckSum(CommonUtils.generateCheckSum(mockRequest));
    }
}

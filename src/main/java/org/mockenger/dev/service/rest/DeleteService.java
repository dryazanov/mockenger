package org.mockenger.dev.service.rest;

import org.mockenger.dev.common.HttpUtils;
import org.mockenger.dev.model.mocks.request.IRequestEntity;
import org.mockenger.dev.model.mocks.request.part.Headers;
import org.mockenger.dev.model.mocks.request.part.Parameters;
import org.mockenger.dev.model.mocks.request.part.Path;
import org.mockenger.dev.model.mocks.request.rest.DeleteEntity;
import org.mockenger.dev.service.CommonService;
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
public class DeleteService<T extends IRequestEntity> extends CommonService<T> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(DeleteService.class);


    public T createMockRequest(String groupId, HttpServletRequest request) {
        Path path = new Path(HttpUtils.getUrlPath(request));
        Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        T mockRequest = (T) new DeleteEntity();
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

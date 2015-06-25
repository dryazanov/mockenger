package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.core.service.common.DeleteService;
import com.socialstartup.mockenger.core.service.common.GetService;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 */
public class ParentController extends AbstractController {

    @Autowired
    private GetService getService;

    @Autowired
    private DeleteService deleteService;

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    protected ResponseEntity doGetRequest(String groupId, HttpServletRequest request) {
        Group group = findGroupById(groupId);
        AbstractRequest mockRequest = getService.createMockRequest(group.getId(), request);
        return findMockedEntities(mockRequest, group.isRecording());
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    protected ResponseEntity doDeleteRequest(String groupId, HttpServletRequest request) {
        Group group = findGroupById(groupId);
        AbstractRequest mockRequest = deleteService.createMockRequest(group.getId(), request);
        return findMockedEntities(mockRequest, group.isRecording());
    }

    /**
     *
     * @param mockRequest
     * @param recordRequests
     * @return
     */
    protected ResponseEntity findMockedEntities(AbstractRequest mockRequest, boolean recordRequests) {
        if (mockRequest == null) {
            // TODO: Create and throw MockObjectNotCreatedException
            throw new RuntimeException("Can't create mock object");
        }

        AbstractRequest mockResult = getRequestService().findMockedEntities(mockRequest);
        return generateResponse(mockRequest, mockResult, recordRequests);
    }

    /**
     *
     * @param mockRequest
     * @param mockResult
     * @param recordRequests
     * @return
     */
    protected ResponseEntity generateResponse(AbstractRequest mockRequest, AbstractRequest mockResult, boolean recordRequests) {
        if (mockResult != null) {
            // TODO: Check mockResult.getMockResponse().getBody() for null values
            int httpStatusCode = mockResult.getMockResponse().getHttpStatus();
            if (!CollectionUtils.isEmpty(mockResult.getMockResponse().getHeaders())) {
                for (Map.Entry<String, String> header : mockResult.getMockResponse().getHeaders().entrySet()) {
                    getResponseHeaders().set(header.getKey(), header.getValue());
                }
            }
            return new ResponseEntity(mockResult.getMockResponse().getBody(), getResponseHeaders(), HttpStatus.valueOf(httpStatusCode));
        } else {
            HttpStatus status = HttpStatus.NOT_FOUND;
            if (recordRequests) {
                // TODO: Decide which unique id generator is better
                mockRequest.setId(CommonUtils.generateUniqueId());
                getRequestService().save(mockRequest);
                status = HttpStatus.CREATED;
            }
            return new ResponseEntity(getResponseHeaders(), status);
        }
    }
}

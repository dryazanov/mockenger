package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.common.DeleteService;
import com.socialstartup.mockenger.core.service.common.GetService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.core.web.exception.MockObjectNotCreatedException;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

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
            throw new MockObjectNotCreatedException("Provided mock-request is null or empty");
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
            MockResponse mockResponse;
            if (mockResult.getMockResponse() == null) {
                mockResponse = new MockResponse();
                mockResponse.setHttpStatus(HttpStatus.FOUND.value());
                mockResponse.setBody("The mock you are looking for does exist but the response object is null");
            } else {
                mockResponse = mockResult.getMockResponse();
            }
            if (!CollectionUtils.isEmpty(mockResponse.getHeaders())) {
                for (Pair header : mockResponse.getHeaders()) {
                    getResponseHeaders().set(header.getKey(), header.getValue());
                }
            } else if (mockRequest.getHeaders() != null) {
                Set<Pair> headerValues = mockRequest.getHeaders().getValues();
                if (!CollectionUtils.isEmpty(headerValues)) {
                    for (Pair pair : headerValues) {
                        if (pair.getKey().equals(CONTENT_TYPE_KEY) && pair.getValue().contains(MediaType.APPLICATION_XML_VALUE)) {
                            getResponseHeaders().set(CONTENT_TYPE_KEY, MEDIA_TYPE_XML);
                        }
                    }
                }

            }
            return new ResponseEntity(mockResponse.getBody(), getResponseHeaders(), HttpStatus.valueOf(mockResponse.getHttpStatus()));
        } else {
            HttpStatus status = HttpStatus.NOT_FOUND;
            if (recordRequests) {
                getRequestService().save(mockRequest);
                status = HttpStatus.CREATED;
            }
            return new ResponseEntity(getResponseHeaders(), status);
        }
    }
}

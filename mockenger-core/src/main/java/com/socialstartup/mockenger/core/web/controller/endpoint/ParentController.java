package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.service.ProxyService;
import com.socialstartup.mockenger.core.service.common.DeleteService;
import com.socialstartup.mockenger.core.service.common.GetService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.core.web.exception.MockObjectNotCreatedException;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static com.socialstartup.mockenger.core.service.HttpHeadersService.CONTENT_TYPE_KEY;
import static com.socialstartup.mockenger.core.service.HttpHeadersService.MEDIA_TYPE_XML;

/**
 *
 */
public class ParentController extends AbstractController {

    @Autowired
    private GetService getService;

    @Autowired
    private DeleteService deleteService;

    @Autowired
    private ProxyService proxyService;

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    protected ResponseEntity doGetRequest(String groupId, HttpServletRequest request) {
        Group group = findGroupById(groupId);
        AbstractRequest mockRequest = getService.createMockRequest(group.getId(), request);
        return findMockedEntities(mockRequest, group);
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
        return findMockedEntities(mockRequest, group);
    }

    /**
     *
     * @param mockRequest
     * @param group
     * @return
     */
    protected ResponseEntity findMockedEntities(AbstractRequest mockRequest, Group group) {
        if (mockRequest == null) {
            throw new MockObjectNotCreatedException("Provided mock-request is null or empty");
        }
        AbstractRequest mockResult = getRequestService().findMockedEntities(mockRequest);
        return generateResponse(mockRequest, mockResult, group);
    }

    /**
     *
     * @param mockRequest
     * @param mockResult
     * @param group
     * @return
     */
    protected ResponseEntity generateResponse(AbstractRequest mockRequest, AbstractRequest mockResult, Group group) {
        if (mockResult != null) {
            MockResponse mockResponse;
            org.springframework.http.HttpHeaders headers = httpHeadersService.getDefaultHeaders();
            if (mockResult.getMockResponse() == null) {
                mockResponse = new MockResponse();
                mockResponse.setHttpStatus(HttpStatus.FOUND.value());
                mockResponse.setBody("The mock you are looking for does exist but the response object is null");
            } else {
                mockResponse = mockResult.getMockResponse();
            }
            if (!CollectionUtils.isEmpty(mockResponse.getHeaders())) {
                headers = httpHeadersService.createHeaders(mockResponse.getHeaders());
            } else if (mockRequest.getHeaders() != null) {
                Set<Pair> headerValues = mockRequest.getHeaders().getValues();
                if (!CollectionUtils.isEmpty(headerValues)) {
                    for (Pair pair : headerValues) {
                        if (pair.getKey().equals(CONTENT_TYPE_KEY) && pair.getValue().contains(MediaType.APPLICATION_XML_VALUE)) {
                            headers.set(CONTENT_TYPE_KEY, MEDIA_TYPE_XML);
                        }
                    }
                }
            }
            return new ResponseEntity(mockResponse.getBody(), headers, HttpStatus.valueOf(mockResponse.getHttpStatus()));
        } else {
            HttpStatus status = HttpStatus.NOT_FOUND;
            if (group.isRecording()) {
                Project project = findProjectById(group.getProjectId());

                if (StringUtils.isEmpty(mockRequest.getName())) {
                    mockRequest.setName(String.valueOf(mockRequest.getCreationDate().getTime()));
                }

                mockRequest.setUniqueCode(String.format("%s-%d", project.getCode(), getProjectService().getNextSequenceValue(group.getProjectId())));
                cleanUpRequestBody(mockRequest);

                if (group.isForwarding()) {
                    mockRequest = proxyService.forwardRequest(mockRequest, group.getForwardTo());
                }

                getRequestService().save(mockRequest);
                status = HttpStatus.CREATED;
            } else {
                mockRequest = null;
            }
            return new ResponseEntity(mockRequest, getResponseHeaders(), status);
        }
    }
}

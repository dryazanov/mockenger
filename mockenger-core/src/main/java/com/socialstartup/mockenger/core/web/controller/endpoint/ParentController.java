package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialstartup.mockenger.core.service.ProxyService;
import com.socialstartup.mockenger.core.service.common.DeleteService;
import com.socialstartup.mockenger.core.service.common.GetService;
import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.core.web.exception.MockObjectNotCreatedException;
import com.socialstartup.mockenger.data.model.dto.Message;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.socialstartup.mockenger.core.service.HttpHeadersService.CONTENT_TYPE_KEY;
import static com.socialstartup.mockenger.core.service.HttpHeadersService.MEDIA_TYPE_XML;

/**
 *
 */
public class ParentController extends AbstractController {

    private final Logger LOG = LoggerFactory.getLogger(ProxyService.class);

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
        final Group group = findGroupById(groupId);
        final AbstractRequest mockRequest = getService.createMockRequest(group.getId(), request);
        return findMockedEntities(mockRequest, group);
    }

    /**
     *
     * @param groupId
     * @param request
     * @return
     */
    protected ResponseEntity doDeleteRequest(String groupId, HttpServletRequest request) {
        final Group group = findGroupById(groupId);
        final AbstractRequest mockRequest = deleteService.createMockRequest(group.getId(), request);
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
        final AbstractRequest mockResult = getRequestService().findMockedEntities(mockRequest);
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
            final MockResponse mockResponse = createMockResponse(mockResult);
            org.springframework.http.HttpHeaders headers = httpHeadersService.getDefaultHeaders();

            if (!CollectionUtils.isEmpty(mockResponse.getHeaders())) {
                headers = httpHeadersService.createHeaders(mockResponse.getHeaders());
            } else if (mockRequest.getHeaders() != null) {
                headers = updateHeaders(mockRequest, headers);
            }
            return new ResponseEntity<>(mockResponse.getBody(), headers, HttpStatus.valueOf(mockResponse.getHttpStatus()));
        } else if (group.isRecording()) {
            return processRecording(group, mockRequest);
        }
        return new ResponseEntity<>(null, getResponseHeaders(), HttpStatus.NOT_FOUND);
    }

    private org.springframework.http.HttpHeaders updateHeaders(final AbstractRequest mockRequest, final HttpHeaders headers) {
        final Set<Pair> headerValues = mockRequest.getHeaders().getValues();
        if (!CollectionUtils.isEmpty(headerValues)) {
            headerValues.forEach(pair -> {
                if (pair.getKey().equals(CONTENT_TYPE_KEY) && pair.getValue().contains(MediaType.APPLICATION_XML_VALUE)) {
                    headers.set(CONTENT_TYPE_KEY, MEDIA_TYPE_XML);
                }
            });
        }
        return headers;
    }

    private ResponseEntity processRecording(final Group group, final AbstractRequest mockRequest) {
        if (StringUtils.isEmpty(mockRequest.getName())) {
            mockRequest.setName(String.valueOf(mockRequest.getCreationDate().getTime()));
        }

        final Project project = findProjectById(group.getProjectId());
        final long nextSequenceValue = getProjectService().getNextSequenceValue(group.getProjectId());
        mockRequest.setUniqueCode(project.getCode() + "-" + nextSequenceValue);
        cleanUpRequestBody(mockRequest);

        if (group.isForwarding()) {
            mockRequest.setMockResponse(proxyService.forwardRequest(mockRequest, group.getForwardTo()));
        }

        getRequestService().save(mockRequest);
        return new ResponseEntity<Object>(mockRequest, getResponseHeaders(), HttpStatus.CREATED);
    }

    private MockResponse createMockResponse(final AbstractRequest mockResult) {
        return Optional.ofNullable(mockResult.getMockResponse()).map(res -> res).orElse(createResponse302());
    }

    private MockResponse createResponse302() {
        String jsonMessageToSend = "";
        final String message = "The mock you are looking for does exist but the response object is null";
        try {
            jsonMessageToSend = new ObjectMapper().writeValueAsString(new Message(message));
        } catch (JsonProcessingException e) {
            LOG.warn("Can't convert message '" + message + "' to json. Message will be send as a plain text", e);
        }
        return new MockResponse(HttpStatus.FOUND.value(), Collections.emptySet(), jsonMessageToSend);
    }
}

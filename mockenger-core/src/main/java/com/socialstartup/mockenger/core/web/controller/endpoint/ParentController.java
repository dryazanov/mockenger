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
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

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
    protected ResponseEntity findMockedEntities(GenericRequest mockRequest, Group group) {
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
    protected ResponseEntity generateResponse(GenericRequest mockRequest, AbstractRequest mockResult, Group group) {
        if (mockResult != null) {
            final MockResponse mockResponse = createMockResponse(mockResult);
			final HttpHeaders headers = getHttpHeaders(mockRequest, mockResponse);
			final HttpStatus httpStatus = HttpStatus.valueOf(mockResponse.getHttpStatus());

			return new ResponseEntity<>(mockResponse.getBody(), headers, httpStatus);
        } else if (group.isRecording()) {
            return processRecording(group, mockRequest);
        }

        return new ResponseEntity<>(null, getResponseHeaders(), HttpStatus.NOT_FOUND);
    }


	private HttpHeaders getHttpHeaders(final GenericRequest mockRequest, final MockResponse mockResponse) {
		final HttpHeaders headers = httpHeadersService.getDefaultHeaders();

		if (!CollectionUtils.isEmpty(mockResponse.getHeaders())) {
			return httpHeadersService.createHeaders(mockResponse.getHeaders());
		} else if (mockRequest.getHeaders() != null) {
			return httpHeadersService.updateContentTypeHeader(mockRequest, headers);
		}

		return headers;
	}


	private ResponseEntity processRecording(final Group group, final GenericRequest mockRequest) {
		final AbstractRequest abstractRequest = getRequestService().toAbstractRequest(mockRequest);

		abstractRequest.setUniqueCode(getUniqueCode(group.getProjectId()));
        cleanUpRequestBody(abstractRequest);

        if (group.isForwarding()) {
			abstractRequest.setMockResponse(proxyService.forwardRequest(abstractRequest, group.getForwardTo()));
        }

        getRequestService().save(abstractRequest);

        return new ResponseEntity<Object>(mockRequest, getResponseHeaders(), HttpStatus.CREATED);
    }


	private String getUniqueCode(final String projectId) {
		final Project project = findProjectById(projectId);
		final long nextSequenceValue = getProjectService().getNextSequenceValue(projectId);

		return project.getCode() + "-" + nextSequenceValue;
	}


	private MockResponse createMockResponse(final AbstractRequest mockResult) {
        return Optional.ofNullable(mockResult.getMockResponse()).orElse(createResponse302());
    }


    private MockResponse createResponse302() {
        return new MockResponse(HttpStatus.FOUND.value(), Collections.emptySet(), get302ResponseMessage());
    }


	private String get302ResponseMessage() {
		final String message = "The mock you are looking for does exist but the response object is null";

		try {
			return new ObjectMapper().writeValueAsString(new Message(message));
		} catch (JsonProcessingException e) {
			LOG.warn("Can't convert message '" + message + "' to json. Message will be send as a plain text", e);
		}

		return message;
	}
}

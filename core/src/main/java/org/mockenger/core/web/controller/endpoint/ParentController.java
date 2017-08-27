package org.mockenger.core.web.controller.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mockenger.core.service.ProxyService;
import org.mockenger.core.service.common.DeleteService;
import org.mockenger.core.service.common.GetService;
import org.mockenger.core.web.controller.base.AbstractController;
import org.mockenger.core.web.exception.MockObjectNotCreatedException;
import org.mockenger.data.model.dto.Message;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.response.MockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Objects;

import static java.util.Collections.EMPTY_SET;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.mockenger.core.util.MockRequestUtils.toAbstractRequest;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.ResponseEntity.created;

/**
 *
 */
@Slf4j
public class ParentController extends AbstractController {

    @Autowired
    private GetService getService;

    @Autowired
    private DeleteService deleteService;

    @Autowired
    private ProxyService proxyService;

	@Autowired
	private ObjectMapper objectMapper;

    /**
     *
     * @param groupCode
     * @param request
     * @return
     */
    protected ResponseEntity doGetRequest(final String groupCode, final HttpServletRequest request) {
        final Group group = findGroupByCode(groupCode);
        final AbstractRequest mockRequest = getService.createMockRequest(group.getId(), request);

        return findMockedEntities(mockRequest, group);
    }

    /**
     *
     * @param groupCode
     * @param request
     * @return
     */
    protected ResponseEntity doDeleteRequest(final String groupCode, final HttpServletRequest request) {
        final Group group = findGroupByCode(groupCode);
        final AbstractRequest mockRequest = deleteService.createMockRequest(group.getId(), request);

        return findMockedEntities(mockRequest, group);
    }

    /**
     *
     * @param mockRequest
     * @param group
     * @return
     */
    protected ResponseEntity findMockedEntities(final GenericRequest mockRequest, final Group group) {
        if (Objects.isNull(mockRequest)) {
            throw new MockObjectNotCreatedException("Provided mock-request is null or empty");
        }

        final AbstractRequest mockResult = requestService.findMockedEntities(mockRequest);

		return generateResponse(mockRequest, mockResult, group);
    }

    /**
     *
     * @param mockRequest
     * @param mockResult
     * @param group
     * @return
     */
    protected ResponseEntity generateResponse(final GenericRequest mockRequest, final AbstractRequest mockResult, final Group group) {
        if (nonNull(mockResult)) {
            final MockResponse mockResponse = createMockResponse(mockResult);
			final HttpHeaders headers = getHttpHeaders(mockRequest, mockResponse);
			final HttpStatus httpStatus = HttpStatus.valueOf(mockResponse.getHttpStatus());

			return new ResponseEntity<>(mockResponse.getBody(), headers, httpStatus);
        } else if (group.isRecording()) {
            return processRecording(group, mockRequest);
        }

        return notFoundWithDefaultHeaders();
    }


	private HttpHeaders getHttpHeaders(final GenericRequest mockRequest, final MockResponse mockResponse) {
		final HttpHeaders headers = httpHeadersService.getDefaultHeaders();

		if (!CollectionUtils.isEmpty(mockResponse.getHeaders())) {
			return httpHeadersService.createHeaders(mockResponse.getHeaders());
		} else if (nonNull(mockRequest.getHeaders())) {
			return httpHeadersService.updateContentTypeHeader(mockRequest, headers);
		}

		return headers;
	}


	private ResponseEntity processRecording(final Group group, final GenericRequest mockRequest) {
		final AbstractRequest abstractRequest = toAbstractRequest(mockRequest);

		abstractRequest.setCode(getUniqueCode(group));

        if (group.isForwarding()) {
			final MockResponse mockResponse = proxyService.forwardRequest(abstractRequest, group.getForwardTo());

			abstractRequest.setMockResponse(mockResponse);
        }

        requestService.save(abstractRequest);

        return created(URI.create("")).headers(getResponseHeaders()).body(mockRequest);
    }


	private MockResponse createMockResponse(final AbstractRequest mockResult) {
        return ofNullable(mockResult.getMockResponse()).orElse(createResponse302());
    }


    private MockResponse createResponse302() {
        return new MockResponse(FOUND.value(), EMPTY_SET, get302ResponseMessage());
    }


	private String get302ResponseMessage() {
		final String message = "The mock you are looking for does exist but the response object is null";

		try {
			return objectMapper.writeValueAsString(new Message(message));
		} catch (JsonProcessingException e) {
			log.warn("Can't convert message '" + message + "' to json. Message will be send as a plain text", e);
		}

		return message;
	}
}

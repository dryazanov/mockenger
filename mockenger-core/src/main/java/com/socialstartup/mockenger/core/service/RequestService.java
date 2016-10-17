package com.socialstartup.mockenger.core.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.socialstartup.mockenger.commons.utils.JsonHelper;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.core.util.HttpUtils;
import com.socialstartup.mockenger.core.web.exception.NotUniqueValueException;
import com.socialstartup.mockenger.data.model.persistent.log.Eventable;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import com.socialstartup.mockenger.data.model.persistent.transformer.RegexpTransformer;
import com.socialstartup.mockenger.data.repository.RequestEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class RequestService {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private RequestEntityRepository requestEntityRepository;


    public AbstractRequest findById(final String id) {
        return requestEntityRepository.findOne(id);
    }


    public AbstractRequest findByIdAndUniqueCode(final String id, final String code) {
        return requestEntityRepository.findByIdAndUniqueCode(id, code);
    }


    public Iterable<AbstractRequest> findAll() {
        return Optional.ofNullable(requestEntityRepository.findAll()).orElse(ImmutableList.of());
    }


    public Iterable<AbstractRequest> findByGroupId(final String groupId) {
        return requestEntityRepository.findByGroupId(groupId);
    }

    @Eventable
    public AbstractRequest save(final AbstractRequest entity) {
        try {
            return requestEntityRepository.save(entity);
        } catch (DuplicateKeyException ex) {
            throw new NotUniqueValueException(String.format("Request with the code '%s' already exists", entity.getUniqueCode()));
        }
    }

    @Eventable
    public void remove(final AbstractRequest entity) {
        requestEntityRepository.delete(entity);
    }

    public String prepareRequestXmlBody(final String requestBody) {
        final String body = new RegexpTransformer(">\\s+<", "><").transform(requestBody.trim());
        if (body.startsWith("<?xml")) {
            return body.substring(body.indexOf("?>") + 2);
        }
        return body;
    }


    public String prepareRequestJsonBody(final String requestBody) throws IOException {
        return JsonHelper.removeWhitespaces(requestBody);
    }


    public AbstractRequest findMockedEntities(final GenericRequest mockRequest) {
        List<AbstractRequest> entities = requestEntityRepository.findByGroupIdAndMethod(mockRequest.getGroupId(), mockRequest.getMethod());

        if (entities != null && entities.size() > 0) {
            return doFilter(mockRequest, entities);
        }

        return null;
    }


    public GenericRequest fillUpEntity(final GenericRequest mockRequest, final String groupId, final HttpServletRequest request) {
        final Path path = new Path(HttpUtils.getUrlPath(request));
        final Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        final Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        mockRequest.setGroupId(groupId);
        mockRequest.setPath(path);
        mockRequest.setHeaders(headers);
        mockRequest.setParameters(parameters);
        mockRequest.setCheckSum(CommonUtils.getCheckSum(mockRequest));

        return mockRequest;
    }
    
    public AbstractRequest toAbstractRequest(final GenericRequest genericRequest) {
		final Date now = new Date();
		final AbstractRequest abstractRequest = new AbstractRequest();

		abstractRequest.setGroupId(genericRequest.getGroupId());
		abstractRequest.setMethod(genericRequest.getMethod());
		abstractRequest.setName(String.valueOf(now.getTime()));
		abstractRequest.setCreationDate(now);
		abstractRequest.setPath(genericRequest.getPath());
		abstractRequest.setParameters(genericRequest.getParameters());
		abstractRequest.setHeaders(genericRequest.getHeaders());
		abstractRequest.setBody(genericRequest.getBody());
		abstractRequest.setCheckSum(CommonUtils.getCheckSum(genericRequest));

		return abstractRequest;
	}


    /**
     *
     * @param incomingRequest
     * @param requestsFromDb
     * @return
     */
    public AbstractRequest doFilter(final GenericRequest incomingRequest, final List<AbstractRequest> requestsFromDb) {
        final RequestComparator requestComparator = new RequestComparator(incomingRequest);

        for (AbstractRequest storedRequest : requestsFromDb) {
            if (requestComparator.compareTo(storedRequest)) {
                return storedRequest;
            }
        }

		LOG.debug(Strings.repeat("*", 25));
		LOG.debug("TOO BAD, NO MOCKS FOUND...");
		LOG.debug(Strings.repeat("*", 25));
        return null;
    }

}

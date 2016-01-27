package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.commons.utils.JsonHelper;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.core.util.HttpUtils;
import com.socialstartup.mockenger.core.web.exception.NotUniqueValueException;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
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
 * Created by Dmitry Ryazanov on 3/24/2015.
 */
@Component
public class RequestService {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private RequestEntityRepository requestEntityRepository;


    public AbstractRequest findById(String id) {
        return requestEntityRepository.findOne(id);
    }

    public AbstractRequest findByIdAndUniqueCode(String id, String code) {
        return requestEntityRepository.findByIdAndUniqueCode(id, code);
    }

    public Iterable<AbstractRequest> findAll() {
        return requestEntityRepository.findAll();
    }

    public List<AbstractRequest> findByGroupId(String groupId) {
        return requestEntityRepository.findByGroupId(groupId);
    }

    public void save(final AbstractRequest entity) {
        try {
            requestEntityRepository.save(entity);
        } catch (DuplicateKeyException ex) {
            throw new NotUniqueValueException(String.format("Request with the code '%s' already exist", entity.getUniqueCode()));
        }
    }

    public void remove(final AbstractRequest entity) {
        requestEntityRepository.delete(entity);
    }

    public String prepareRequestXmlBody(String requestBody) {
        requestBody = new RegexpTransformer(">\\s+<", "><").transform(requestBody.trim());
        if (requestBody.startsWith("<?xml")) {
            requestBody = requestBody.substring(requestBody.indexOf("?>") + 2);
        }
        return requestBody;
    }

    public String prepareRequestJsonBody(final String requestBody) throws IOException {
        return JsonHelper.removeWhitespaces(requestBody);
    }

    public AbstractRequest findMockedEntities(final AbstractRequest mockRequest) {
        List<AbstractRequest> entities = requestEntityRepository.findByGroupIdAndMethod(mockRequest.getGroupId(), mockRequest.getMethod());

        if (entities != null && entities.size() > 0) {
            return doFilter(mockRequest, entities);
        }

        return null;
    }

    public AbstractRequest fillUpEntity(final AbstractRequest mockRequest, final String groupId, final HttpServletRequest request) {
        final Path path = new Path(HttpUtils.getUrlPath(request));
        final Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        final Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        mockRequest.setGroupId(groupId);
        mockRequest.setCreationDate(new Date());
        mockRequest.setPath(path);
        mockRequest.setHeaders(headers);
        mockRequest.setParameters(parameters);
        mockRequest.setCheckSum(CommonUtils.getCheckSum(mockRequest));

        return mockRequest;
    }


    /**
     *
     * @param incomingRequest
     * @param requestsFromDb
     * @return
     */
    public AbstractRequest doFilter(final AbstractRequest incomingRequest, final List<AbstractRequest> requestsFromDb) {
        final RequestComparator requestComparator = new RequestComparator(incomingRequest);

        for (AbstractRequest storedRequest : requestsFromDb) {
            if (requestComparator.compareTo(storedRequest)) {
                return storedRequest;
            }
        }

        LOG.debug("No mocks found!");
        return null;
    }

}

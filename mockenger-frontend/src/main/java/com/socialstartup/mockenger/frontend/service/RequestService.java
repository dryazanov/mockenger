package com.socialstartup.mockenger.frontend.service;

import com.socialstartup.mockenger.frontend.repository.RequestRepository;
import com.socialstartup.mockenger.model.mock.request.IMockRequest;
import com.socialstartup.mockenger.model.mock.request.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class RequestService extends CommonService {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private RequestRepository requestRepository;


    public RequestEntity findById(String id) {
        return requestRepository.findById(id);
    }

    public RequestEntity findMockedEntities(RequestEntity mockRequest) {
        List<RequestEntity> entities = requestRepository.findAll(mockRequest);

        if (entities != null && entities.size() > 0) {
            return this.doFilter(mockRequest, entities);
        }

        return null;
    }

    public List<RequestEntity> findAllByGroupId(String groupId) {
        return requestRepository.findAllByGroupId(groupId);
    }

    public void save(RequestEntity entity) {
        requestRepository.save(entity);
    }

    public void remove(RequestEntity entity) {
        requestRepository.remove(entity);
    }
}

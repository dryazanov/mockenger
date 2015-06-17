package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.repository.RequestEntityRepository;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
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
    private RequestEntityRepository requestEntityRepository;


    public RequestEntity findById(String id) {
        return requestEntityRepository.findOne(id);
    }

    public RequestEntity findMockedEntities(RequestEntity mockRequest) {
        List<RequestEntity> entities = requestEntityRepository.findByGroupIdAndMethod(mockRequest.getGroupId(), mockRequest.getMethod());

        if (entities != null && entities.size() > 0) {
            return this.doFilter(mockRequest, entities);
        }

        return null;
    }

    public List<RequestEntity> findByGroupId(String groupId) {
        return requestEntityRepository.findByGroupId(groupId);
    }

    public void save(RequestEntity entity) {
        requestEntityRepository.save(entity);
    }

    public void remove(RequestEntity entity) {
        requestEntityRepository.delete(entity);
    }
}

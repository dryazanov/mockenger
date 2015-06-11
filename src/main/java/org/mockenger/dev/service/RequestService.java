package org.mockenger.dev.service;

import org.mockenger.dev.model.mocks.request.IMockRequest;
import org.mockenger.dev.model.mocks.request.IRequestEntity;
import org.mockenger.dev.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class RequestService<T extends IRequestEntity> extends CommonService<T> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private RequestRepository requestRepository;


    public T findMockedEntities(T mockRequest) {
        List<T> entities = (List<T>) requestRepository.findAll((IMockRequest) mockRequest);

        if (entities != null && entities.size() > 0) {
            return this.doFilter(mockRequest, entities);
        }

        return null;
    }

    public List<T> findAllByGroupId(String groupId) {
        return (List<T>) requestRepository.findAllByGroupId(groupId);
    }

    public void save(T entity) {
        requestRepository.save(entity);
    }
}

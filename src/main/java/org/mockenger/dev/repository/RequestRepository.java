package org.mockenger.dev.repository;

import org.mockenger.dev.model.mock.request.IMockRequest;
import org.mockenger.dev.model.mock.request.IRequestEntity;
import org.mockenger.dev.model.mock.request.RequestEntity;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class RequestRepository extends GenericRepository<RequestEntity> {

    public static final String COLLECTION_NAME = "request";

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }

    public void removeAllByGroupId(String groupId) {
        Query query = new Query(Criteria.where(GROUP_ID).is(groupId));
        getMongoTemplate().findAllAndRemove(query, getType(), getCollectionName());
    }

    public List<RequestEntity> findAll(IMockRequest mockRequest) {
        Query query = new Query(
                Criteria.where(GROUP_ID).is(mockRequest.getGroupId())
                        //.and(PATH).is(mockRequest.getPath().getValue())
                        .and(METHOD).is(mockRequest.getMethod())
                //.and(PARAMETERS).is(mockRequest.getParameters().getValues())
        );

        return getMongoTemplate().find(query, getType(), getCollectionName());
    }

    public void save(IRequestEntity entity) {
        getMongoTemplate().save(entity, getCollectionName());
    }
}
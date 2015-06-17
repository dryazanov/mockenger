package com.socialstartup.mockenger.data.repository.impl;

import com.socialstartup.mockenger.data.repository.RequestEntityRepository;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by x079089 on 3/24/2015.
 */
public class RequestEntityRepositoryImpl extends GenericRepository<RequestEntity> implements RequestEntityRepository {

    public static final String COLLECTION_NAME = "request";

    public RequestEntityRepositoryImpl(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    public String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public Class<RequestEntity> getType() {
        return RequestEntity.class;
    }

    public List<RequestEntity> findAll(RequestEntity mockRequest) {
        Query query = new Query(
                Criteria.where(GROUP_ID).is(mockRequest.getGroupId())
                        .and(METHOD).is(mockRequest.getMethod())
        );

        return getMongoTemplate().find(query, getType(), getCollectionName());
    }

    public List<RequestEntity> findByGroupId(String groupId) {
        Query query = new Query(Criteria.where(GROUP_ID).is(groupId));
        return getMongoTemplate().find(query, getType(), getCollectionName());
    }
}
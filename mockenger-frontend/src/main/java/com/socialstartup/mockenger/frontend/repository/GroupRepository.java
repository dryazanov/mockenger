package com.socialstartup.mockenger.frontend.repository;

import com.socialstartup.mockenger.model.mock.group.GroupEntity;
import com.socialstartup.mockenger.model.mock.group.GroupType;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by x079089 on 3/29/2015.
 */
@Component
public class GroupRepository extends GenericRepository<GroupEntity> {

    public static final String COLLECTION_NAME = "group";

    protected final static String GROUP_TYPE = "type";

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public Class<GroupEntity> getType() {
        return GroupEntity.class;
    }

    public List<GroupEntity> findAllByType(GroupType type) {
        Query query = new Query(Criteria.where(GROUP_TYPE).is(type));
        return getMongoTemplate().find(query, getType(), getCollectionName());
    }

    public List<GroupEntity> findAllByProjectId(String projectId) {
        Query query = new Query(Criteria.where(PROJECT_ID).is(projectId));
        return getMongoTemplate().find(query, getType(), getCollectionName());
    }
}

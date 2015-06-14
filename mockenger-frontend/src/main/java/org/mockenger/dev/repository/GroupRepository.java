package org.mockenger.dev.repository;

import org.mockenger.dev.model.mock.MockRequestType;
import org.mockenger.dev.model.mock.group.GroupEntity;
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

    protected final static String MOCK_TYPE = "type";

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }

    public List<GroupEntity> findAllByType(MockRequestType type) {
        Query query = new Query(Criteria.where(MOCK_TYPE).is(type));
        return getMongoTemplate().find(query, getType(), getCollectionName());
    }

}

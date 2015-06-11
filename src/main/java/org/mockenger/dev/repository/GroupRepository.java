package org.mockenger.dev.repository;

import org.mockenger.dev.model.mocks.group.GroupEntity;
import org.springframework.stereotype.Component;

/**
 * Created by x079089 on 3/29/2015.
 */
@Component
public class GroupRepository extends GenericRepository<GroupEntity> {

    public static final String COLLECTION_NAME = "group";

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }

}

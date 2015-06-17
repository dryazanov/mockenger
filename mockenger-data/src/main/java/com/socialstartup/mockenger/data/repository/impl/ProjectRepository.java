package com.socialstartup.mockenger.data.repository.impl;

import com.socialstartup.mockenger.data.repository.impl.GenericRepository;
import com.socialstartup.mockenger.model.mock.project.ProjectEntity;
import com.socialstartup.mockenger.model.mock.project.ProjectType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by x079089 on 3/29/2015.
 */
@Component
public class ProjectRepository extends GenericRepository<ProjectEntity> {

    public static final String COLLECTION_NAME = "project";

    protected final static String PROJECT_TYPE = "type";

    public ProjectRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    protected String getCollectionName() {
        return COLLECTION_NAME;
    }

    @Override
    public Class<ProjectEntity> getType() {
        return ProjectEntity.class;
    }

    public List<ProjectEntity> findAllByType(ProjectType type) {
        Query query = new Query(Criteria.where(PROJECT_TYPE).is(type));
        return getMongoTemplate().find(query, getType(), getCollectionName());
    }

}

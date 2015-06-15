package com.socialstartup.mockenger.frontend.repository;

import com.socialstartup.mockenger.model.persistent.base.AbstractPersistentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * Created by x079089 on 3/24/2015.
 */
public abstract class GenericRepository<T extends AbstractPersistentEntity> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(GenericRepository.class);


    @Autowired
    private MongoTemplate mongoTemplate;

//    protected Class<T> type;

    protected final static String PROJECT_ID = "projectId";

    protected final static String GROUP_ID = "groupId";

    protected final static String METHOD = "method";



    /*public GenericRepository() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public GenericRepository(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return this.type;
    }*/

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public abstract Class<T> getType();

    protected abstract String getCollectionName();

    public List<T> findAll() {
        return getMongoTemplate().findAll(getType(), getCollectionName());
    }

    public T findById(String id) {
        return getMongoTemplate().findById(id, getType(), getCollectionName());
    }

    public void save(T entity) {
        getMongoTemplate().save(entity, getCollectionName());
    }

    public void remove(T entity) {
        getMongoTemplate().remove(entity, getCollectionName());
    }
}
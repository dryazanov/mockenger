package org.mockenger.dev.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by x079089 on 3/24/2015.
 */
public abstract class GenericRepository<T> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(GenericRepository.class);


    @Autowired
    private MongoTemplate mongoTemplate;

    protected Class<T> type;


    protected final static String GROUP_ID = "groupId";

    protected final static String METHOD = "method";

//    protected final static String PATH = "path.value";

//    protected final static String PARAMETERS = "parameters.values";



    public GenericRepository() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public GenericRepository(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return this.type;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }



    public List<T> findAll() {
        return getMongoTemplate().findAll(getType(), getCollectionName());
    }

    public T findById(String id) {
        return getMongoTemplate().findById(id, getType(), getCollectionName());
    }

    public List<T> findAllByGroupId(String groupId) {
        Query query = new Query(Criteria.where(GROUP_ID).is(groupId));
        return getMongoTemplate().find(query, getType(), getCollectionName());
    }

    public void save(T entity) {
        getMongoTemplate().save(entity, getCollectionName());
    }

    public void remove(T entity) {
        getMongoTemplate().remove(entity, getCollectionName());
    }

    protected abstract String getCollectionName();
}
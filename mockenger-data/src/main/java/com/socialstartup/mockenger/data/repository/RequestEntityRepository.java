package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Created by ydolzhenko on 17.06.15.
 */
//TODO make it normal spring data repo
@NoRepositoryBean
public interface RequestEntityRepository {

    public List<RequestEntity> findAll();

    RequestEntity findById(String id);

    void save(RequestEntity entity);

    void remove(RequestEntity entity);

    List<RequestEntity> findByGroupId(String groupId);

    List<RequestEntity> findAll(RequestEntity mockRequest);
}

package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ydolzhenko on 17.06.15.
 */
public interface RequestEntityRepository extends CrudRepository<RequestEntity, String> {

    List<RequestEntity> findByGroupId(String groupId);

    List<RequestEntity> findByGroupIdAndMethod(String groupId, RequestType method);
}

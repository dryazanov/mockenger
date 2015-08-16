package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ydolzhenko on 17.06.15.
 */
public interface RequestEntityRepository extends CrudRepository<AbstractRequest, String> {

    AbstractRequest findByIdAndUniqueCode(String requestId, String uniqueCode);

    List<AbstractRequest> findByGroupId(String groupId);

    List<AbstractRequest> findByGroupIdAndMethod(String groupId, RequestMethod method);
}

package org.mockenger.data.repository;

import org.mockenger.data.model.dict.RequestMethod;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Dmitry Ryazanov
 */
public interface RequestEntityRepository extends CrudRepository<AbstractRequest, String> {

	AbstractRequest findByCode(String code);

    List<AbstractRequest> findByGroupId(String groupId);

    List<AbstractRequest> findByGroupIdAndMethod(String groupId, RequestMethod method);
}

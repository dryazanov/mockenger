package org.mockenger.data.repository;

import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.springframework.data.repository.Repository;

/**
 * @author Dmitry Ryazanov
 */
public interface RequestEntityCustomRepository extends Repository<AbstractRequest, String> {

	void updateRequestCounter(AbstractRequest entity);
}

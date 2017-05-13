package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.springframework.data.repository.Repository;

/**
 * @author Dmitry Ryazanov
 */
public interface RequestEntityCustomRepository extends Repository<AbstractRequest, String> {

	void updateRequestCounter(AbstractRequest entity);
}

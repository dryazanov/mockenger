package com.socialstartup.mockenger.data.repository;

import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.aggregation.Fields.UNDERSCORE_ID;

/**
 * @author Dmitry Ryazanov
 */
public class RequestEntityCustomRepositoryImpl implements RequestEntityCustomRepository {

	@Autowired
	private MongoOperations mongoOperations;


	public void updateRequestCounter(final AbstractRequest entity) {
		final Criteria criteria = Criteria.where(UNDERSCORE_ID).is(entity.getId());
		final Update update = new Update().inc("requestCounter", 1);

		mongoOperations.updateFirst(new Query(criteria), update, AbstractRequest.class);
	}
}

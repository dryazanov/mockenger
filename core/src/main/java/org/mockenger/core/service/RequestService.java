package org.mockenger.core.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.mockenger.core.util.HttpUtils;
import org.mockenger.core.web.exception.NotUniqueValueException;
import org.mockenger.data.model.persistent.log.Eventable;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.request.Latency;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.mockenger.data.model.persistent.mock.request.part.Headers;
import org.mockenger.data.model.persistent.mock.request.part.Parameters;
import org.mockenger.data.model.persistent.mock.request.part.Path;
import org.mockenger.data.repository.RequestEntityCustomRepository;
import org.mockenger.data.repository.RequestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;

import static java.util.Collections.EMPTY_SET;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.mockenger.core.util.CommonUtils.getCheckSum;
import static org.mockenger.core.util.CommonUtils.getRandom;
import static org.mockenger.core.util.CommonUtils.joinParams;
import static org.mockenger.core.util.HttpUtils.getParameterSortedSet;
import static org.mockenger.core.util.MockRequestUtils.isURLEncodedForm;

/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@Component
public class RequestService {

	private static final String MOCKS_CACHE_NAME = "mocksCache";

	@Autowired
    private RequestEntityRepository requestEntityRepository;

    @Autowired
    private RequestEntityCustomRepository requestEntityCustomRepository;


    public AbstractRequest findById(final String id) {
        return requestEntityRepository.findOne(id);
    }


	public AbstractRequest findByCode(final String code) {
		return requestEntityRepository.findByCode(code);
	}


    public Iterable<AbstractRequest> findAll() {
        return Optional.ofNullable(requestEntityRepository.findAll()).orElse(ImmutableList.of());
    }


    public Iterable<AbstractRequest> findByGroupId(final String groupId) {
        return requestEntityRepository.findByGroupId(groupId);
    }

	public void updateRequestCounter(final AbstractRequest entity) {
		requestEntityCustomRepository.updateRequestCounter(entity);
	}


    @Eventable
	@CacheEvict(value = MOCKS_CACHE_NAME, allEntries = true)
    public AbstractRequest save(final AbstractRequest entity) {
		try {
			return requestEntityRepository.save(entity);
		} catch (DuplicateKeyException ex) {
			throw new NotUniqueValueException("Request with the code '" + entity.getCode() + "' already exists");
		}
    }

    @Eventable
	@CacheEvict(value = MOCKS_CACHE_NAME, allEntries = true)
    public void remove(final AbstractRequest entity) {
        requestEntityRepository.delete(entity);
    }


	@Eventable
	@CacheEvict(value = MOCKS_CACHE_NAME, allEntries = true)
	public void removeAll() {
		requestEntityRepository.deleteAll();
	}


	/**
	 *
	 * @param mockRequest
	 * @return
	 */
	@Cacheable(value = MOCKS_CACHE_NAME, keyGenerator = "keyGenerator", unless="#result == null")
	public AbstractRequest findMockedEntities(final GenericRequest mockRequest) {
        final List<AbstractRequest> entities = requestEntityRepository.findByGroupIdAndMethod(mockRequest.getGroupId(), mockRequest.getMethod());

        if (!CollectionUtils.isEmpty(entities)) {
            return doFilter(mockRequest, entities);
        }

        return null;
    }


	/**
	 *
	 * @param incomingRequest
	 * @param requestsFromDb
	 * @return
	 */
	public AbstractRequest doFilter(final GenericRequest incomingRequest, final List<AbstractRequest> requestsFromDb) {
		final RequestComparator requestComparator = new RequestComparator(incomingRequest);

		for (AbstractRequest storedRequest : requestsFromDb) {
			if (requestComparator.compareTo(storedRequest)) {
				return storedRequest;
			}
		}

		if (log.isDebugEnabled()) {
			final StringBuilder sb = new StringBuilder()
					.append(Strings.repeat("*", 25))
					.append("\n")
					.append("TOO BAD, NO MOCKS FOUND...")
					.append("\n")
					.append(Strings.repeat("*", 25));

			log.debug(sb.toString());
		}

		return null;
	}


	public void simulateDelay(final Latency latency) {
		if (nonNull(latency)) {
			try {
				final long sleep = (latency.getMin() > 0 && latency.getMax() > 0 ? getRandom(latency.getMin(), latency.getMax()) : latency.getFixed());

				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}


	public <T extends GenericRequest> T createMockRequest(final T genericRequest, final String groupId, final HttpServletRequest request) {
		final Headers headers = new Headers(HttpUtils.getHeaders(request, true));
		final boolean isURLEncodedForm = isURLEncodedForm(headers);

		genericRequest.setHeaders(headers);

		if (isURLEncodedForm) {
			return fillUpForURLEncodedBody(genericRequest, groupId, request);
		}

		return fillUpEntity(genericRequest, groupId, request);
	}


    public <T extends GenericRequest> T fillUpForURLEncodedBody(final T genericRequest, final String groupId, final HttpServletRequest request) {
		final SortedSet sortedSet = getParameterSortedSet(request);
		final String joinedParams = joinParams(sortedSet, "=", "&");

		genericRequest.setBody(new Body(joinedParams));
		genericRequest.setParameters(new Parameters(EMPTY_SET));

		return fillUpEntity(genericRequest, groupId, request);
	}


    public <T extends GenericRequest> T fillUpEntity(final T genericRequest, final String groupId, final HttpServletRequest request) {
        final Path path = new Path(HttpUtils.getUrlPath(request));

        if (isNull(genericRequest.getHeaders())) {
			final Headers headers = new Headers(HttpUtils.getHeaders(request, true));

        	genericRequest.setHeaders(headers);
		}

		if (isNull(genericRequest.getParameters())) {
			final Parameters parameters = new Parameters(HttpUtils.getParameterSet(request));

			genericRequest.setParameters(parameters);
		}

        genericRequest.setGroupId(groupId);
        genericRequest.setPath(path);
        genericRequest.setCheckSum(getCheckSum(genericRequest));

        return genericRequest;
    }
}

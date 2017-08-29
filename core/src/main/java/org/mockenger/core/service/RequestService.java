package org.mockenger.core.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.mockenger.commons.utils.JsonHelper;
import org.mockenger.commons.utils.XmlHelper;
import org.mockenger.core.util.CommonUtils;
import org.mockenger.core.util.HttpUtils;
import org.mockenger.core.web.exception.NotUniqueValueException;
import org.mockenger.data.model.persistent.log.Eventable;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.mockenger.data.model.persistent.mock.request.part.Headers;
import org.mockenger.data.model.persistent.mock.request.part.Parameters;
import org.mockenger.data.model.persistent.mock.request.part.Path;
import org.mockenger.data.model.persistent.transformer.RegexpTransformer;
import org.mockenger.data.repository.RequestEntityCustomRepository;
import org.mockenger.data.repository.RequestEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.mockenger.core.util.CommonUtils.startAndEndsWith;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class RequestService {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestService.class);


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
    public AbstractRequest save(final AbstractRequest entity) {
		try {
			return requestEntityRepository.save(entity);
		} catch (DuplicateKeyException ex) {
			throw new NotUniqueValueException(String.format("Request with the code '%s' already exists", entity.getCode()));
		}
    }

    @Eventable
    public void remove(final AbstractRequest entity) {
        requestEntityRepository.delete(entity);
    }


    public String prepareRequestXmlBody(final String requestBody) {
        final String body = new RegexpTransformer(">\\s+<", "><").transform(requestBody.trim());
        if (body.startsWith("<?xml")) {
            return body.substring(body.indexOf("?>") + 2);
        }
        return body;
    }


    public String prepareRequestJsonBody(final String requestBody) throws IOException {
        return JsonHelper.removeWhitespaces(requestBody);
    }


    public AbstractRequest findMockedEntities(final GenericRequest mockRequest) {
        final List<AbstractRequest> entities = requestEntityRepository.findByGroupIdAndMethod(mockRequest.getGroupId(), mockRequest.getMethod());

        if (!CollectionUtils.isEmpty(entities)) {
            return doFilter(mockRequest, entities);
        }

        return null;
    }


    public GenericRequest fillUpEntity(final GenericRequest mockRequest, final String groupId, final HttpServletRequest request) {
        final Path path = new Path(HttpUtils.getUrlPath(request));
        final Headers headers = new Headers(HttpUtils.getHeaders(request, true));
		final boolean isURLEncodedForm = ofNullable(request.getHeader(CONTENT_TYPE))
				.map(h -> h.equalsIgnoreCase(APPLICATION_FORM_URLENCODED_VALUE))
				.orElse(false);
		final Parameters parameters = new Parameters((isURLEncodedForm ? null : HttpUtils.getParameterSet(request)));

        mockRequest.setGroupId(groupId);
        mockRequest.setPath(path);
        mockRequest.setHeaders(headers);
        mockRequest.setParameters(parameters);
        mockRequest.setCheckSum(CommonUtils.getCheckSum(mockRequest));

        return mockRequest;
    }
    
    public AbstractRequest toAbstractRequest(final GenericRequest genericRequest) {
		final Date now = new Date();
		final AbstractRequest abstractRequest = new AbstractRequest();

		abstractRequest.setGroupId(genericRequest.getGroupId());
		abstractRequest.setMethod(genericRequest.getMethod());
		abstractRequest.setName(String.valueOf(now.getTime()));
		abstractRequest.setCreationDate(now);
		abstractRequest.setPath(genericRequest.getPath());
		abstractRequest.setParameters(genericRequest.getParameters());
		abstractRequest.setHeaders(genericRequest.getHeaders());
		abstractRequest.setBody(genericRequest.getBody());
		abstractRequest.setCheckSum(CommonUtils.getCheckSum(genericRequest));

		return abstractRequest;
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

		LOG.debug(Strings.repeat("*", 25));
		LOG.debug("TOO BAD, NO MOCKS FOUND...");
		LOG.debug(Strings.repeat("*", 25));
        return null;
    }


	/**
	 *
	 * @param mockRequest
	 */
	public String cleanUpRequestBody(final GenericRequest mockRequest) {
		return ofNullable(mockRequest)
				.map(GenericRequest::getBody)
				.map(Body::getValue)
				.filter(s -> !isEmpty(s))
				.map(s -> removeWhitespaces(s))
				.orElse("");
	}

	public String removeWhitespaces(final String body) {
		try {
			if (startAndEndsWith(body.trim(), "{", "}")) {
				return JsonHelper.removeWhitespaces(body);
			} else if (startAndEndsWith(body.trim(), "<", ">")) {
				return XmlHelper.removeWhitespaces(body);
			}
		} catch (Exception e) {
			LOG.warn("Cannot remove whitespaces from the string", e);
		}

		return body;
	}


    public GenericRequest getCleanCopy(final GenericRequest genericRequest) {
		final String cleanRequestBody = cleanUpRequestBody(genericRequest);
		final Body build = Body.builder().value(cleanRequestBody).build();

		return cloneRequest(genericRequest).body(build).build();
	}


	/**
	 *
	 * @param genericRequest
	 * @return
	 */
	public static GenericRequest.GenericRequestBuilder cloneRequest(final GenericRequest genericRequest) {
		return GenericRequest.builder()
				.groupId(genericRequest.getGroupId())
				.method(genericRequest.getMethod())
				.path(genericRequest.getPath())
				.parameters(genericRequest.getParameters())
				.headers(genericRequest.getHeaders())
				.body(genericRequest.getBody());
	}
}

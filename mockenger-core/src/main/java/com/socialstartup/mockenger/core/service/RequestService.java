package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.core.util.HttpUtils;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.Transformer;
import com.socialstartup.mockenger.data.repository.RequestEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class RequestService {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private RequestEntityRepository requestEntityRepository;


    public AbstractRequest findById(String id) {
        return requestEntityRepository.findOne(id);
    }

    public Iterable<AbstractRequest> findAll() {
        return requestEntityRepository.findAll();
    }

    public List<AbstractRequest> findByGroupId(String groupId) {
        return requestEntityRepository.findByGroupId(groupId);
    }

    public void save(AbstractRequest entity) {
        requestEntityRepository.save(entity);
    }

    public void remove(AbstractRequest entity) {
        requestEntityRepository.delete(entity);
    }

    public AbstractRequest findMockedEntities(AbstractRequest mockRequest) {
        List<AbstractRequest> entities = requestEntityRepository.findByGroupIdAndMethod(mockRequest.getGroupId(), mockRequest.getMethod());

        if (entities != null && entities.size() > 0) {
            return doFilter(mockRequest, entities);
        }

        return null;
    }

    public AbstractRequest fillUpEntity(AbstractRequest mockRequest, String groupId, HttpServletRequest request) {
        Path path = new Path(HttpUtils.getUrlPath(request));
        Headers headers = new Headers(HttpUtils.getHeaders(request, false));
        Parameters parameters = new Parameters(HttpUtils.getParameterMap(request));

        mockRequest.setGroupId(groupId);
        mockRequest.setCreationDate(new Date());
        mockRequest.setPath(path);
        mockRequest.setHeaders(headers);
        mockRequest.setParameters(parameters);
        mockRequest.setCheckSum(CommonUtils.getCheckSum(mockRequest));

        return mockRequest;
    }


    /**
     *
     * @param r
     * @param list
     * @return
     */
    public AbstractRequest doFilter(AbstractRequest r, List<AbstractRequest> list) {
        for (AbstractRequest m : list) {
            LOG.debug("");
            if (!pathsEqual(r, m) || !parametersEqual(r, m) || !headersEqual(r, m)) {
                LOG.debug("------------------------------");
                LOG.debug("Skip this mock");
                continue;
            }

            if (bodiesEqual(r, m)) {
                return m;
            }
        }
        LOG.debug("==============================");
        LOG.debug("No mocks found!");
        return null;
    }

    /**
     * Compare request paths
     *
     * @param abstractRequestFromUser
     * @param mockedAbstractRequest
     * @return
     */
    private boolean pathsEqual(AbstractRequest abstractRequestFromUser, AbstractRequest mockedAbstractRequest) {

        if (abstractRequestFromUser.getPath() != null && mockedAbstractRequest.getPath() != null) {
            String path = abstractRequestFromUser.getPath().getValue();
            List<AbstractTransformer> transformers = mockedAbstractRequest.getPath().getTransformers();
            if (!CollectionUtils.isEmpty(transformers)) {
                for (Transformer transformer : transformers) {
                    path = transformer.transform(path);
                }
            }

            LOG.debug("PATHS");
            LOG.debug(path);
            LOG.debug(mockedAbstractRequest.getPath().getValue());

            if (!path.equals(mockedAbstractRequest.getPath().getValue())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compare request parameters
     *
     * @param abstractRequestFromUser
     * @param mockedAbstractRequest
     * @return
     */
    private boolean parametersEqual(AbstractRequest abstractRequestFromUser, AbstractRequest mockedAbstractRequest) {
        List<AbstractMapTransformer> transformers;
        Map<String, String> usersParameters;
        Map<String, String> mockedParameters;

        Parameters abstractParams = abstractRequestFromUser.getParameters();
        Parameters mockedParams = mockedAbstractRequest.getParameters();

        if (abstractParams != null && mockedParams != null) {
            // Transform and check query parameters
            usersParameters = (abstractParams.getValues() != null ? abstractParams.getValues() : new HashMap<>());
            mockedParameters = (mockedParams.getValues() != null ? mockedParams.getValues() : new HashMap<>());

            if (CommonUtils.allNotEmpty(usersParameters, mockedParameters)) {
                transformers = mockedAbstractRequest.getParameters().getTransformers();
                if (!CollectionUtils.isEmpty(transformers)) {
                    for (AbstractMapTransformer transformer : transformers) {
                        String value = usersParameters.get(transformer.getKey());
                        if (!StringUtils.isEmpty(value)) {
                            usersParameters.put(transformer.getKey(), transformer.transform(value));
                        }
                    }
                }

                LOG.debug("PARAMETERS");
                LOG.debug(usersParameters.toString());
                LOG.debug(mockedParameters.toString());

                if (!CommonUtils.containsEqualEntries(usersParameters, mockedParameters)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Compare request headers
     *
     * @param abstractRequestFromUser
     * @param mockedAbstractRequest
     * @return
     */
    private boolean headersEqual(AbstractRequest abstractRequestFromUser, AbstractRequest mockedAbstractRequest) {
        List<AbstractMapTransformer> transformers;
        Map<String, String> usersHeaders;
        Map<String, String> mockedHeaders;

        if (abstractRequestFromUser.getHeaders() != null && mockedAbstractRequest.getHeaders() != null) {
            // Transform and check headers
            usersHeaders = new HashMap<>(abstractRequestFromUser.getHeaders().getValues());
            mockedHeaders = new HashMap<>(mockedAbstractRequest.getHeaders().getValues());

            if (CommonUtils.allNotEmpty(usersHeaders, mockedHeaders)) {
                transformers = mockedAbstractRequest.getHeaders().getTransformers();
                if (!CollectionUtils.isEmpty(transformers)) {
                    for (AbstractMapTransformer transformer : transformers) {
                        String value = usersHeaders.get(transformer.getKey());
                        if (!StringUtils.isEmpty(value)) {
                            usersHeaders.put(transformer.getKey(), transformer.transform(value));
                        }
                    }
                }

                LOG.debug("HEADERS");
                LOG.debug(usersHeaders.toString());
                LOG.debug(mockedHeaders.toString());

                if (!CommonUtils.containsAll(usersHeaders, mockedHeaders)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Compare request bodies
     *
     * @param abstractRequestFromUser
     * @param mockedAbstractRequest
     * @return
     */
    private boolean bodiesEqual(AbstractRequest abstractRequestFromUser, AbstractRequest mockedAbstractRequest) {
        List<AbstractTransformer> transformers;
        String usersRequestCheckSum = "";
        String mockedCheckSum = mockedAbstractRequest.getCheckSum();

        if (abstractRequestFromUser.getMethod() != null) {
            if (abstractRequestFromUser.getMethod().equals(RequestMethod.POST) || abstractRequestFromUser.getMethod().equals(RequestMethod.PUT)) {
                // Transform request body
                if (abstractRequestFromUser.getBody() != null && mockedAbstractRequest.getBody() != null) {
                    String body = abstractRequestFromUser.getBody().getValue();
                    transformers = mockedAbstractRequest.getBody().getTransformers();
                    if (!CollectionUtils.isEmpty(transformers)) {
                        for (Transformer transformer : transformers) {
                            body = transformer.transform(body);
                        }
                    }
                    usersRequestCheckSum = CommonUtils.generateCheckSum(body);

                    LOG.debug("BODIES");
                    LOG.debug(body);
                    LOG.debug(mockedAbstractRequest.getBody().getValue());

                    LOG.debug("CHECKSUMS");
                    LOG.debug(usersRequestCheckSum);
                    LOG.debug(mockedCheckSum);
                }
            } else {
                // For other methods we only compare checksums
                usersRequestCheckSum = CommonUtils.generateCheckSum(abstractRequestFromUser);

                LOG.debug("CHECKSUMS");
                LOG.debug(usersRequestCheckSum);
                LOG.debug(mockedCheckSum);
            }

            if (usersRequestCheckSum != null && usersRequestCheckSum.equals(mockedCheckSum)) {
                LOG.debug("##############################");
                LOG.debug("Mock found!");
                return true;
            }
        }

        return false;
    }
}

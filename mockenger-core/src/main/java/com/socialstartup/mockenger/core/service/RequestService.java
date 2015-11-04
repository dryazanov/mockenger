package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.commons.utils.JsonHelper;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.core.util.HttpUtils;
import com.socialstartup.mockenger.core.web.exception.NotUniqueValueException;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Path;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.RegexpTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.Transformer;
import com.socialstartup.mockenger.data.repository.RequestEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Dmitry Ryazanov on 3/24/2015.
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

    public AbstractRequest findByIdAndUniqueCode(String id, String code) {
        return requestEntityRepository.findByIdAndUniqueCode(id, code);
    }

    public Iterable<AbstractRequest> findAll() {
        return requestEntityRepository.findAll();
    }

    public List<AbstractRequest> findByGroupId(String groupId) {
        return requestEntityRepository.findByGroupId(groupId);
    }

    public void save(AbstractRequest entity) {
        try {
            requestEntityRepository.save(entity);
        } catch (DuplicateKeyException ex) {
            throw new NotUniqueValueException(String.format("Request with the code '%s' already exist", entity.getUniqueCode()));
        }
    }

    public void remove(AbstractRequest entity) {
        requestEntityRepository.delete(entity);
    }

    public String prepareRequestXmlBody(String requestBody) {
        requestBody = new RegexpTransformer(">\\s+<", "><").transform(requestBody.trim());
        if (requestBody.startsWith("<?xml")) {
            requestBody = requestBody.substring(requestBody.indexOf("?>") + 2);
        }
        return requestBody;
    }

    public String prepareRequestJsonBody(String requestBody) throws IOException {
        return JsonHelper.removeWhitespaces(requestBody);
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
        Set<Pair> usersParameters;
        Set<Pair> mockedParameters;

        Parameters abstractParams = abstractRequestFromUser.getParameters();
        Parameters mockedParams = mockedAbstractRequest.getParameters();

        if (abstractParams != null && mockedParams != null) {
            // Transform and check query parameters
            usersParameters = (abstractParams.getValues() != null ? abstractParams.getValues() : new HashSet<>());
            mockedParameters = (mockedParams.getValues() != null ? mockedParams.getValues() : new HashSet<>());

            if (CommonUtils.allNotEmpty(usersParameters, mockedParameters)) {
                usersParameters = applyTransformers(usersParameters, mockedAbstractRequest.getParameters().getTransformers());

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
        Set<Pair> usersHeaders;
        Set<Pair> mockedHeaders;

        if (abstractRequestFromUser.getHeaders() != null && mockedAbstractRequest.getHeaders() != null) {
            // Transform and check headers
            usersHeaders = new HashSet<>(abstractRequestFromUser.getHeaders().getValues());
            mockedHeaders = new HashSet<>(mockedAbstractRequest.getHeaders().getValues());

            if (CommonUtils.allNotEmpty(usersHeaders, mockedHeaders)) {
                usersHeaders = applyTransformers(usersHeaders, mockedAbstractRequest.getHeaders().getTransformers());

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

    private Set<Pair> applyTransformers(Set<Pair> source, List<AbstractMapTransformer> transformers) {
        if (!CollectionUtils.isEmpty(transformers)) {
            for (AbstractMapTransformer transformer : transformers) {
                Set<Pair> paramsTemp = new TreeSet<>();
                for (Pair pair : source) {
                    if (pair.getKey().equals(transformer.getKey()) && !StringUtils.isEmpty(pair.getValue())) {
                        paramsTemp.add(new Pair(pair.getKey(), transformer.transform(pair.getValue())));
                    } else {
                        paramsTemp.add(pair);
                    }
                }
                source = paramsTemp;
            }
        }
        return source;
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
        String usersRequestCheckSum = null;
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

package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.core.util.HttpUtils;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import com.socialstartup.mockenger.data.model.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.mock.request.part.Path;
import com.socialstartup.mockenger.data.model.transformer.IMapTransformer;
import com.socialstartup.mockenger.data.model.transformer.ITransformer;
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


    public RequestEntity findById(String id) {
        return requestEntityRepository.findOne(id);
    }

    public List<RequestEntity> findByGroupId(String groupId) {
        return requestEntityRepository.findByGroupId(groupId);
    }

    public void save(RequestEntity entity) {
        requestEntityRepository.save(entity);
    }

    public void remove(RequestEntity entity) {
        requestEntityRepository.delete(entity);
    }

    public RequestEntity findMockedEntities(RequestEntity mockRequest) {
        List<RequestEntity> entities = requestEntityRepository.findByGroupIdAndMethod(mockRequest.getGroupId(), mockRequest.getMethod());

        if (entities != null && entities.size() > 0) {
            return doFilter(mockRequest, entities);
        }

        return null;
    }

    public RequestEntity fillUpEntity(RequestEntity mockRequest, String groupId, HttpServletRequest request) {
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
    public RequestEntity doFilter(RequestEntity r, List<RequestEntity> list) {
        for (RequestEntity m : list) {
            if (!pathsEqual(r, m) || !parametersEqual(r, m) || !headersEqual(r, m)) {
                continue;
            }

            if (bodiesEqual(r, m)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Compare request paths
     *
     * @param requestEntityFromUser
     * @param mockedRequestEntity
     * @return
     */
    private boolean pathsEqual(RequestEntity requestEntityFromUser, RequestEntity mockedRequestEntity) {
        List<IMapTransformer> transformers;

        if (requestEntityFromUser.getPath() != null && mockedRequestEntity.getPath() != null) {
            String path = requestEntityFromUser.getPath().getValue();
            transformers = mockedRequestEntity.getPath().getTransformers();
            if (!CollectionUtils.isEmpty(transformers)) {
                for (ITransformer transformer : transformers) {
                    path = transformer.transform(path);
                }
            }

            LOG.debug("PATHS");
            LOG.debug(path);
            LOG.debug(mockedRequestEntity.getPath().getValue());

            if (!path.equals(mockedRequestEntity.getPath().getValue())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compare request parameters
     *
     * @param requestEntityFromUser
     * @param mockedRequestEntity
     * @return
     */
    private boolean parametersEqual(RequestEntity requestEntityFromUser, RequestEntity mockedRequestEntity) {
        List<IMapTransformer> transformers;
        Map<String, String> usersParameters;
        Map<String, String> mockedParameters;

        if (requestEntityFromUser.getParameters() != null && mockedRequestEntity.getParameters() != null) {
            // Transform and check query parameters
            usersParameters = new HashMap<>(requestEntityFromUser.getParameters().getValues());
            mockedParameters = new HashMap<>(mockedRequestEntity.getParameters().getValues());

            if (CommonUtils.allNotEmpty(usersParameters, mockedParameters)) {
                transformers = mockedRequestEntity.getParameters().getTransformers();
                if (!CollectionUtils.isEmpty(transformers)) {
                    for (IMapTransformer transformer : transformers) {
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
     * @param requestEntityFromUser
     * @param mockedRequestEntity
     * @return
     */
    private boolean headersEqual(RequestEntity requestEntityFromUser, RequestEntity mockedRequestEntity) {
        List<IMapTransformer> transformers;
        Map<String, String> usersHeaders;
        Map<String, String> mockedHeaders;

        if (requestEntityFromUser.getHeaders() != null && mockedRequestEntity.getHeaders() != null) {
            // Transform and check headers
            usersHeaders = new HashMap<>(requestEntityFromUser.getHeaders().getValues());
            mockedHeaders = new HashMap<>(mockedRequestEntity.getHeaders().getValues());

            if (CommonUtils.allNotEmpty(usersHeaders, mockedHeaders)) {
                transformers = mockedRequestEntity.getHeaders().getTransformers();
                if (!CollectionUtils.isEmpty(transformers)) {
                    for (IMapTransformer transformer : transformers) {
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
     * @param requestEntityFromUser
     * @param mockedRequestEntity
     * @return
     */
    private boolean bodiesEqual(RequestEntity requestEntityFromUser, RequestEntity mockedRequestEntity) {
        List<IMapTransformer> transformers;
        String usersRequestCheckSum = "";
        String mockedCheckSum = mockedRequestEntity.getCheckSum();

        if (requestEntityFromUser.getMethod() != null) {
            if (requestEntityFromUser.getMethod().equals(RequestType.POST) || requestEntityFromUser.getMethod().equals(RequestType.PUT)) {
                // Transform request body
                if (requestEntityFromUser.getBody() != null && mockedRequestEntity.getBody() != null) {
                    String body = requestEntityFromUser.getBody().getValue();
                    transformers = mockedRequestEntity.getBody().getTransformers();
                    if (!CollectionUtils.isEmpty(transformers)) {
                        for (ITransformer transformer : transformers) {
                            body = transformer.transform(body);
                        }
                    }
                    usersRequestCheckSum = CommonUtils.generateCheckSum(body);

                    LOG.debug("BODIES");
                    LOG.debug(body);
                    LOG.debug(mockedRequestEntity.getBody().getValue());

                    LOG.debug("CHECKSUMS");
                    LOG.debug(usersRequestCheckSum);
                    LOG.debug(mockedCheckSum);
                }
            } else {
                // For other methods we only compare checksums
                usersRequestCheckSum = CommonUtils.generateCheckSum(requestEntityFromUser);

                LOG.debug("CHECKSUMS");
                LOG.debug(usersRequestCheckSum);
                LOG.debug(mockedCheckSum);
            }

            if (usersRequestCheckSum != null && usersRequestCheckSum.equals(mockedCheckSum)) {
                return true;
            }
        }

        return false;
    }
}

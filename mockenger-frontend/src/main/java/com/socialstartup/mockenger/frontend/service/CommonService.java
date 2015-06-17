package com.socialstartup.mockenger.frontend.service;

import com.socialstartup.mockenger.frontend.common.CommonUtils;
import com.socialstartup.mockenger.data.model.RequestType;
import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import com.socialstartup.mockenger.data.model.transformer.IMapTransformer;
import com.socialstartup.mockenger.data.model.transformer.ITransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class CommonService {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(CommonService.class);


    /**
     * Checks if all the provided map are empty
     *
     * @param parameters
     * @return
     */
    public boolean allEmpty(Map<String, String>... parameters) {
        for (Map<String, String> map : parameters) {
            if (!CollectionUtils.isEmpty(map)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param parameters
     * @return
     */
    public boolean allNotEmpty(Map<String, String>... parameters) {
        for (Map<String, String> map : parameters) {
            if (CollectionUtils.isEmpty(map)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param inputMap
     * @param template
     * @return
     */
    public boolean containsAll(Map<String, String> inputMap, Map<String, String> template) {
        return inputMap.entrySet().containsAll(template.entrySet());
    }

    /**
     *
     * @param inputMap
     * @param template
     * @return
     */
    public boolean containsEqualEntries(Map<String, String> inputMap, Map<String, String> template) {
        if (CollectionUtils.isEmpty(inputMap) || CollectionUtils.isEmpty(template)) {
            return false;
        }
        for (Map.Entry<String, String> entry : template.entrySet()) {
            if (!inputMap.containsKey(entry.getKey()) || !inputMap.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param mockRequest
     * @param requestList
     * @return
     */
    protected RequestEntity doFilter(RequestEntity mockRequest, List<RequestEntity> requestList) {
        Map<String, String> mParams = null;
        Map<String, String> tParams = null;

        for (RequestEntity template : requestList) {
            List<IMapTransformer> transformers;

            // Transform and check request path
            if (mockRequest.getPath() != null && template.getPath() != null) {
                String path = mockRequest.getPath().getValue();
                transformers = template.getPath().getTransformers();
                if (!CollectionUtils.isEmpty(transformers)) {
                    for (ITransformer transformer : transformers) {
                        path = transformer.transform(path);
                    }
                }

                LOG.debug("PATHS");
                LOG.debug(path);
                LOG.debug(template.getPath().getValue());

                if (!path.equals(template.getPath().getValue())) {
                    continue;
                }
            }

            if (mockRequest.getParameters() != null && template.getParameters() != null) {
                // Transform and check query parameters
                mParams = new HashMap<>(mockRequest.getParameters().getValues());
                tParams = new HashMap<>(template.getParameters().getValues());

                if (allNotEmpty(mParams, tParams)) {
                    transformers = template.getParameters().getTransformers();
                    if (!CollectionUtils.isEmpty(transformers)) {
                        for (IMapTransformer transformer : transformers) {
                            String value = mParams.get(transformer.getKey());
                            if (!StringUtils.isEmpty(value)) {
                                mParams.put(transformer.getKey(), transformer.transform(value));
                            }
                        }
                    }

                    LOG.debug("PARAMETERS");
                    LOG.debug(mParams.toString());
                    LOG.debug(tParams.toString());

                    if (!containsEqualEntries(mParams, tParams)) {
                        continue;
                    }
                }
            }

            if (mockRequest.getHeaders() != null && template.getHeaders() != null) {
                // Transform and check headers
                mParams = new HashMap<>(mockRequest.getHeaders().getValues());
                tParams = new HashMap<>(template.getHeaders().getValues());

                if (allNotEmpty(mParams, tParams)) {
                    transformers = template.getHeaders().getTransformers();
                    if (!CollectionUtils.isEmpty(transformers)) {
                        for (IMapTransformer transformer : transformers) {
                            String value = mParams.get(transformer.getKey());
                            if (!StringUtils.isEmpty(value)) {
                                mParams.put(transformer.getKey(), transformer.transform(value));
                            }
                        }
                    }

                    LOG.debug("HEADERS");
                    LOG.debug(mParams.toString());
                    LOG.debug(tParams.toString());

                    if (!containsAll(mParams, tParams)) {
                        continue;
                    }
                }
            }

            String requestCheckSum = "";
            String mockCheckSum = CommonUtils.getCheckSum(template);
            RequestType method = mockRequest.getMethod();
            if (method != null) {
                if (method.equals(RequestType.POST) || method.equals(RequestType.PUT)) {
                    // Transform request body
                    if (mockRequest.getBody() != null && template.getBody() != null) {
                        String body = mockRequest.getBody().getValue();
                        transformers = template.getBody().getTransformers();
                        if (!CollectionUtils.isEmpty(transformers)) {
                            for (ITransformer transformer : transformers) {
                                body = transformer.transform(body);
                            }
                        }
                        requestCheckSum = CommonUtils.getCheckSum(mockRequest);

                        LOG.debug("Bodies");
                        LOG.debug(body);
                        LOG.debug(template.getBody().getValue());

                        LOG.debug("CHECKSUMS");
                        LOG.debug(requestCheckSum);
                        LOG.debug(mockCheckSum);
                    }
                } else {
                    // For GET and DELETE just comparing checksums
                    requestCheckSum = CommonUtils.generateCheckSum(mockRequest);

                    LOG.debug("CHECKSUMS");
                    LOG.debug(requestCheckSum);
                    LOG.debug(mockCheckSum);
                }

                if (requestCheckSum != null && requestCheckSum.equals(mockCheckSum)) {
                    return template;
                }
            }
        }
        return null;
    }
}

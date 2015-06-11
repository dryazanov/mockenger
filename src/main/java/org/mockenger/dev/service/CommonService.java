package org.mockenger.dev.service;

import org.mockenger.dev.model.mocks.request.IRequestEntity;
import org.mockenger.dev.model.transformer.IMapTransformer;
import org.mockenger.dev.model.transformer.ITransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by x079089 on 3/24/2015.
 */
@Component
public class CommonService<T extends IRequestEntity> {

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
    protected T doFilter(T mockRequest, List<T> requestList) {
        Map<String, String> mParams = null;
        Map<String, String> tParams = null;
        List<T> filteredMocks = new ArrayList<T>();

        LOG.debug("Looking for mock template in the MongoDB");
        LOG.debug("");

        for (T template : requestList) {
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

                LOG.debug("Compare paths");
                LOG.debug(path + "\t:\t" + template.getPath().getValue());

                if (!path.equals(template.getPath().getValue())) {
                    continue;
                }
            }

            // Transform and check query parameters
            mParams = new HashMap<String, String>(mockRequest.getParameters().getValues());
            tParams = new HashMap<String, String>(template.getParameters().getValues());

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

                LOG.debug("Compare parameters");
                LOG.debug(mParams + "\t:\t" + tParams);

                if (!containsEqualEntries(mParams, tParams)) {
                    continue;
                }
            }

            // Transform and check headers
            mParams = new HashMap<String, String>(mockRequest.getHeaders().getValues());
            tParams = new HashMap<String, String>(template.getHeaders().getValues());

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

                LOG.debug("Compare headers");
                LOG.debug(mParams.toString());
                LOG.debug(tParams.toString());

                if (!containsAll(mParams, tParams)) {
                    continue;
                }
            }

            String checkSum = "";
            if (mockRequest.getMethod().equals(RequestMethod.POST) || mockRequest.getMethod().equals(RequestMethod.PUT)) {
                // Transform request body
                if (mockRequest.getBody() != null && template.getBody() != null) {
                    String body = mockRequest.getBody().getValue();
                    transformers = template.getBody().getTransformers();
                    if (!CollectionUtils.isEmpty(transformers)) {
                        for (ITransformer transformer : transformers) {
                            body = transformer.transform(body);
                        }
                    }

//                    LOG.debug("Comparing bodies");
//                    LOG.debug(body + "\t:\t" + template.getBody().getValue());

                    checkSum = DigestUtils.md5DigestAsHex(body.getBytes());
                    LOG.debug("Comparing checksums: " + checkSum + " <==> " + template.getCheckSum());
                }
            } else {
                // For GET and DELETE just comparing checksums
                checkSum = mockRequest.getCheckSum();
                LOG.debug("Comparing checksums: " + checkSum + " <==> " + template.getCheckSum());
            }

            if (checkSum != null && checkSum.equals(template.getCheckSum())) {
                filteredMocks.add(template);
            }
        }

        return (filteredMocks != null && filteredMocks.size() > 0 ? filteredMocks.get(0) : null);
    }

    /*public IRequestEntity validateCheckSum(IMockRequest mockRequest, List<? extends IRequestEntity> filteredMocks) {
        String checkSum = mockRequest.getCheckSum();

        for (IRequestEntity template : filteredMocks) {
            if (mockRequest.getBody() != null && !StringUtils.isEmpty(mockRequest.getBody().getValue())) {
                List<ITransformer> transformers = template.getBody().getTransformers();

                if (!CollectionUtils.isEmpty(transformers)) {
                    String requestBody = mockRequest.getBody().getValue();
                    LOG.debug("Transformers found. Updating request body...");

                    for (ITransformer transformer : transformers) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(new StringBuilder()
                                .append("Transformer type: ")
                                .append(transformer.getType())
                                .append("; pattern: ")
                                .append(transformer.getPattern())
                                .append("; replacement: ")
                                .append(transformer.getReplacement())
                                .toString());
                        }
                        requestBody = transformer.transform(requestBody);
                    }

                    checkSum = DigestUtils.md5DigestAsHex(requestBody.getBytes());
                }
            }

            LOG.debug("Comparing checksums: " + checkSum + " <==> " + template.getCheckSum());
            if (checkSum.equals(template.getCheckSum())) {
                return template;
            }
        }

        return null;
    }*/
}

package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Headers;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Parameters;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by Dmitry Ryazanov on 27/01/2016.
 */
public class RequestComparator {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestService.class);
    
    private final AbstractRequest requestFromClient;
    private AbstractRequest requestsFromDb;

    /**
     * Constructor
     *
     * @param requestFromClient
     */
    public RequestComparator(final AbstractRequest requestFromClient) {
        this.requestFromClient = requestFromClient;
    }

    /**
     * Compares requestFromClient with every passed requestsFromDb
     *
     * @param requestsFromDb
     * @return
     */
    public boolean compareTo(final AbstractRequest requestsFromDb) {
        if (requestsFromDb != null) {
            this.requestsFromDb = requestsFromDb;

            if (comparePaths() && compareParameters() && compareHeaders() && compareBodies()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compare request paths

     * @return
     */
    private boolean comparePaths() {
        String path = requestFromClient.getPath().getValue();
        final List<AbstractTransformer> transformers = requestsFromDb.getPath().getTransformers();

        if (!CollectionUtils.isEmpty(transformers)) {
            for (Transformer transformer : transformers) {
                path = transformer.transform(path);
            }
        }

        printPathDebugInfo(path);
        return path.equals(requestsFromDb.getPath().getValue());
    }

    /**
     * Compare request parameters

     * @return
     */
    private boolean compareParameters() {
        Set<Pair> paramsFromClient = extractParamsSafely(requestFromClient);
        final Set<Pair> paramsFromDb = extractParamsSafely(requestsFromDb);

        if (CommonUtils.allNotEmpty(paramsFromClient, paramsFromDb)) {
            final List<AbstractMapTransformer> transformers = requestsFromDb.getParameters().getTransformers();
            paramsFromClient = applyTransformers(paramsFromClient, transformers);
            printParametersDebugInfo(paramsFromClient, paramsFromDb);

            return CommonUtils.containsEqualEntries(paramsFromClient, paramsFromDb);
        }

        return true;
    }

    /**
     * Compare request headers

     * @return
     */
    private boolean compareHeaders() {
        Set<Pair> userHeaders = extractHeadersSafely(requestFromClient);
        final Set<Pair> mockHeaders = extractHeadersSafely(requestsFromDb);

        if (CommonUtils.allNotEmpty(userHeaders, mockHeaders)) {
            final List<AbstractMapTransformer> transformers = requestsFromDb.getHeaders().getTransformers();
            userHeaders = applyTransformers(userHeaders, transformers);
            printHeadersDebugInfo(userHeaders, mockHeaders);

            return CommonUtils.containsAll(userHeaders, mockHeaders);
        }

        return true;
    }

    private Set<Pair> applyTransformers(Set<Pair> pairsToBeTransformed, final List<AbstractMapTransformer> transformers) {
        if (!CollectionUtils.isEmpty(transformers)) {
            for (final AbstractMapTransformer transformer : transformers) {
                final Set<Pair> transformedPairs = new TreeSet<>();
                pairsToBeTransformed.forEach((pair) -> transformedPairs.add(transformPairAndGet(transformer, pair)));
                pairsToBeTransformed = transformedPairs;
            }
        }
        return pairsToBeTransformed;
    }

    private Pair transformPairAndGet(final AbstractMapTransformer transformer, final Pair pair) {
        if (pair.getKey().equals(transformer.getKey()) && !StringUtils.isEmpty(pair.getValue())) {
            return new Pair(pair.getKey(), transformer.transform(pair.getValue()));
        }
        return pair;
    }

    /**
     * Compare request bodies
     *
     * @return
     */
    private boolean compareBodies() {
        String checksumFromClient;
        if (isMethodWithBody(requestFromClient)) {
            checksumFromClient = transformBodyAndGetChecksum();
        } else {
            // For other methods we only compare checksums
            checksumFromClient = CommonUtils.generateCheckSum(requestFromClient);
            printChecksumDebugInfo(checksumFromClient);
        }

        if (checksumFromClient.equals(requestsFromDb.getCheckSum())) {
            LOG.debug("Mock found!");
            return true;
        }

        return false;
    }

    private String transformBodyAndGetChecksum() {
        String checksumFromClient = "";
        String bodyFromClient = requestFromClient.getBody().getValue();

        if (!StringUtils.isEmpty(bodyFromClient)) {
            final List<AbstractTransformer> transformers = requestsFromDb.getBody().getTransformers();
            if (!CollectionUtils.isEmpty(transformers)) {
                for (Transformer transformer : transformers) {
                    bodyFromClient = transformer.transform(bodyFromClient);
                }
            }

            printRequestBodyDebugInfo(bodyFromClient);
            checksumFromClient = CommonUtils.generateCheckSum(bodyFromClient);
            printChecksumDebugInfo(checksumFromClient);
        }
        return checksumFromClient;
    }

    private void printDebugInfo(final String type, final String clientData, final String dbData) {
        LOG.debug(type + ": client - " + clientData + " | db - " + dbData);
    }

    private void printPathDebugInfo(final String path) {
        printDebugInfo("PATHS", path, requestsFromDb.getPath().getValue());
    }

    private void printParametersDebugInfo(final Set<Pair> paramsFromClient, final Set<Pair> paramsFromDb) {
        printDebugInfo("PARAMETERS", paramsFromClient.toString(), paramsFromDb.toString());
    }

    private void printHeadersDebugInfo(final Set<Pair> headersFromClient, final Set<Pair> headersFromDb) {
        printDebugInfo("HEADERS", headersFromClient.toString(), headersFromDb.toString());
    }

    private void printRequestBodyDebugInfo(String bodyFromClient) {
        printDebugInfo("BODIES", bodyFromClient, requestsFromDb.getBody().getValue());
    }

    private void printChecksumDebugInfo(String checksumFromClient) {
        printDebugInfo("CHECKSUMS", checksumFromClient, requestsFromDb.getCheckSum());
    }

    private Set<Pair> extractParamsSafely(AbstractRequest request) {
        final Parameters params = Optional.ofNullable(request.getParameters()).orElse(new Parameters());
        return Optional.ofNullable(params.getValues()).orElse(new HashSet<>());
    }

    private Set<Pair> extractHeadersSafely(AbstractRequest request) {
        final Headers headers = Optional.ofNullable(request.getHeaders()).orElse(new Headers());
        return Optional.ofNullable(headers.getValues()).orElse(new HashSet<>());
    }

    private boolean isMethodWithBody(AbstractRequest request) {
        return request.getMethod() != null && (request.getMethod().equals(RequestMethod.POST) || request.getMethod().equals(RequestMethod.PUT));
    }
}

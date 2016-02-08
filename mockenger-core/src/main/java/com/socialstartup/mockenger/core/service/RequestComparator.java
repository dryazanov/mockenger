package com.socialstartup.mockenger.core.service;

import com.google.common.collect.Sets;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;

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
        LOG.debug("Not equal, skip mock");
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

        ComparatorLogger.printPaths(path, requestsFromDb.getPath().getValue());

        return path.equals(requestsFromDb.getPath().getValue());
    }

    /**
     * Compare request parameters

     * @return
     */
    private boolean compareParameters() {
        Set<Pair> paramsFromClient = requestFromClient.getParameters().getValues();
        final Set<Pair> paramsFromDb = requestsFromDb.getParameters().getValues();

        if (CommonUtils.allNotEmpty(paramsFromClient, paramsFromDb)) {
            final List<AbstractMapTransformer> transformers = requestsFromDb.getParameters().getTransformers();
            paramsFromClient = applyTransformers(paramsFromClient, transformers);

            ComparatorLogger.printParameters(paramsFromClient, paramsFromDb);

            return CommonUtils.containsEqualEntries(paramsFromClient, paramsFromDb);
        }

        return true;
    }

    /**
     * Compare request headers

     * @return
     */
    private boolean compareHeaders() {
        Set<Pair> userHeaders = requestFromClient.getHeaders().getValues();
        final Set<Pair> mockHeaders = requestsFromDb.getHeaders().getValues();

        if (CommonUtils.allNotEmpty(userHeaders, mockHeaders)) {
            final List<AbstractMapTransformer> transformers = requestsFromDb.getHeaders().getTransformers();
            userHeaders = applyTransformers(userHeaders, transformers);

            ComparatorLogger.printHeaders(userHeaders, mockHeaders);

            return CommonUtils.containsAll(userHeaders, mockHeaders);
        }

        return true;
    }

    /**
     * Compare request bodies
     *
     * @return
     */
    private boolean compareBodies() {
        final String checksumFromClient;

        if (isHttpMethodWithBody(requestFromClient)) {
            checksumFromClient = transformBodyAndGetChecksum();
        } else {
            // For other methods we only compare checksums
            checksumFromClient = CommonUtils.generateCheckSum(requestFromClient);
            ComparatorLogger.printChecksums(checksumFromClient, requestsFromDb.getCheckSum());
        }

        if (checksumFromClient.equals(requestsFromDb.getCheckSum())) {
            LOG.debug("Mock found!");
            return true;
        }

        return false;
    }

    private Set<Pair> applyTransformers(final Set<Pair> pairsToBeTransformed, final List<AbstractMapTransformer> transformers) {
        if (!CollectionUtils.isEmpty(transformers)) {
            final Set<Pair> resultSet = Sets.newHashSet();
            pairsToBeTransformed.forEach(pair -> resultSet.add(transformAndGet(pair, transformers)));
            return resultSet;
        }
        return pairsToBeTransformed;
    }

    private Pair transformAndGet(final Pair pair, final List<AbstractMapTransformer> transformers) {
        return new Pair(pair.getKey(), transformers.stream()
                .map(t -> t.transform(pair.getValue()))
                .findFirst()
                .orElse(pair.getValue()));
    }

    private String transformBodyAndGetChecksum() {
        String bodyFromClient = requestFromClient.getBody().getValue();

        if (StringUtils.isEmpty(bodyFromClient)) {
            return "";
        }

        final List<AbstractTransformer> transformers = requestsFromDb.getBody().getTransformers();
        if (!CollectionUtils.isEmpty(transformers)) {
            bodyFromClient = transformers.stream()
                    .map(t -> t.transform(requestFromClient.getBody().getValue()))
                    .findFirst()
                    .orElse(bodyFromClient);
        }

        ComparatorLogger.printRequestBodies(bodyFromClient, requestsFromDb.getBody().getValue());

        final String checksum = CommonUtils.generateCheckSum(bodyFromClient);
        ComparatorLogger.printChecksums(checksum, requestsFromDb.getCheckSum());
        return checksum;
    }

    private boolean isHttpMethodWithBody(AbstractRequest request) {
        return request.getMethod() != null && (request.getMethod().equals(RequestMethod.POST) || request.getMethod().equals(RequestMethod.PUT));
    }


    private final static class ComparatorLogger {

        public static final String PATHS_TITLE = "PATHS";
        public static final String PARAMETERS_TITLE = "PARAMETERS";
        public static final String HEADERS_TITLE = "HEADERS";
        public static final String BODIES_TITLE = "BODIES";
        public static final String CHECKSUMS_TITLE = "CHECKSUMS";

        private static void print(final String type, final String clientData, final String dbData) {
            LOG.debug(String.format("%s: client - %s | db - %s", type, clientData, dbData));
        }

        private static void printPaths(final String pathFromClient, final String pathFromDb) {
            print(PATHS_TITLE, pathFromClient, pathFromDb);
        }

        private static void printParameters(final Set<Pair> paramsFromClient, final Set<Pair> paramsFromDb) {
            print(PARAMETERS_TITLE, paramsFromClient.toString(), paramsFromDb.toString());
        }

        private static void printHeaders(final Set<Pair> headersFromClient, final Set<Pair> headersFromDb) {
            print(HEADERS_TITLE, headersFromClient.toString(), headersFromDb.toString());
        }

        private static void printRequestBodies(final String bodyFromClient, final String bodyFromDb) {
            print(BODIES_TITLE, bodyFromClient, bodyFromDb);
        }

        private static void printChecksums(final String checksumFromClient, final String checksumFromDb) {
            print(CHECKSUMS_TITLE, checksumFromClient, checksumFromDb);
        }
    }
}

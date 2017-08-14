package com.socialstartup.mockenger.core.service;

import com.google.common.base.Strings;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.transformer.AbstractMapTransformer;
import com.socialstartup.mockenger.data.model.persistent.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static com.socialstartup.mockenger.core.util.CommonUtils.allNotEmpty;
import static com.socialstartup.mockenger.core.util.CommonUtils.containsAll;
import static com.socialstartup.mockenger.core.util.CommonUtils.containsEqualEntries;
import static com.socialstartup.mockenger.core.util.CommonUtils.generateCheckSum;
import static com.socialstartup.mockenger.data.model.dict.RequestMethod.PATCH;
import static com.socialstartup.mockenger.data.model.dict.RequestMethod.POST;
import static com.socialstartup.mockenger.data.model.dict.RequestMethod.PUT;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author Dmitry Ryazanov
 */
public class RequestComparator {

    private final GenericRequest incoming;

    private AbstractRequest persistent;

	private Printer prnt;


    /**
     * Constructor
     *
     * @param incomingRequest
     */
    public RequestComparator(final GenericRequest incomingRequest) {
        this.incoming = incomingRequest;
		this.prnt = new Printer();
    }


    /**
     * Compares incoming with every passed persistent
     *
     * @param persistentRequest
     * @return
     */
    public boolean compareTo(final AbstractRequest persistentRequest) {
        if (persistentRequest != null) {
            this.persistent = persistentRequest;

            if (comparePaths() && compareParameters() && compareHeaders() && compareBodies()) {
                return true;
            }
        }

        prnt.printSkipMock();
        return false;
    }


    /**
     * Compare request paths
	 *
     * @return
     */
    private boolean comparePaths() {
		final String incomingPath = getPath(incoming.getPath().getValue());
		final String persistentPath = persistent.getPath().getValue();

		prnt.printPaths(incomingPath, persistentPath);

        return incomingPath.equals(persistentPath);
    }


    /**
     * Compare request parameters
	 *
     * @return
     */
    private boolean compareParameters() {
        Set<Pair> incomingParams = incoming.getParameters().getValues();
        final Set<Pair> persistentParams = persistent.getParameters().getValues();

        if (allNotEmpty(incomingParams, persistentParams)) {
            incomingParams = applyTransformers(incomingParams, persistent.getParameters().getTransformers());
            prnt.printParams(incomingParams, persistentParams);

            return containsEqualEntries(incomingParams, persistentParams);
        }

        return true;
    }

    /**
     * Compare request headers
	 *
     * @return
     */
    private boolean compareHeaders() {
        Set<Pair> incomingHeaders = incoming.getHeaders().getValues();
        final Set<Pair> persistentHeaders = persistent.getHeaders().getValues();

        if (allNotEmpty(incomingHeaders, persistentHeaders)) {
            incomingHeaders = applyTransformers(incomingHeaders, persistent.getHeaders().getTransformers());
            prnt.printHeaders(incomingHeaders, persistentHeaders);

            return containsAll(incomingHeaders, persistentHeaders);
        }

        return true;
    }

    /**
     * Compare request bodies
     *
     * @return
     */
    private boolean compareBodies() {
        final String checksum;

        if (isHttpMethodWithBody(incoming)) {
            checksum = transformBodyAndGetChecksum();
        } else {
            // For other methods we only compare checksums
            checksum = generateCheckSum(incoming);
            prnt.printChecksums(checksum, persistent.getCheckSum());
        }

        if (checksum.equals(persistent.getCheckSum())) {
            prnt.printMockFound();
            return true;
        }

        return false;
    }


	private boolean isHttpMethodWithBody(final GenericRequest request) {
		return ofNullable(request)
				.map(GenericRequest::getMethod)
				.map(m -> m.equals(POST) || m.equals(PUT) || m.equals(PATCH))
				.orElse(false);
	}


	private String transformBodyAndGetChecksum() {
		final String body = getBody(incoming.getBody().getValue());
		prnt.printBodies(body, persistent.getBody().getValue());

		final String checksum = generateCheckSum(body);
		prnt.printChecksums(checksum, persistent.getCheckSum());

		return checksum;
	}


    private Set<Pair> applyTransformers(final Set<Pair> pairsToBeTransformed, final List<Transformer> transformers) {
        if (!isEmpty(transformers)) {
			return pairsToBeTransformed.stream()
					.map(pair -> transformAndGet(pair, transformers))
					.collect(toSet());
        }

        return pairsToBeTransformed;
    }


    private Pair transformAndGet(final Pair pair, final List<Transformer> transformers) {
		final String pairKey = pair.getKey();
		final List<Transformer> filteredTransformers = transformers.stream()
				.filter(t -> t instanceof AbstractMapTransformer)
				.filter(t -> pairKey.equals(((AbstractMapTransformer) t).getKey()))
				.collect(toList());

		return new Pair(pairKey, transformString(pair.getValue(), filteredTransformers));
    }


	private String transformString(final String stringToTransform, final  List<Transformer> transformers) {
		final int size = transformers.size();

		if (size > 0) {
			final Transformer transformer = transformers.get(size - 1);

			if (size == 1) {
				return transformer.transform(stringToTransform);
			}

			return transformer.transform(transformString(stringToTransform, transformers.subList(0, size - 1)));
		}

		return stringToTransform;
	}


	private String getPath(final String initialPath) {
		final String path = ofNullable(initialPath).orElse("");
		final List<Transformer> transformers = persistent.getPath().getTransformers();

		if (!isEmpty(transformers)) {
			return transformString(initialPath, transformers);
		}

		return path;
	}


	private String getBody(final String initialBody) {
		final String body = ofNullable(initialBody).orElse("");
		final List<Transformer> transformers = persistent.getBody().getTransformers();

		if (!isEmpty(transformers)) {
			return transformString(body, transformers);
		}

		return body;
	}



	/**
	 * Local printer of the comparison steps
	 */
	private final class Printer {

		private final Logger LOG = LoggerFactory.getLogger(RequestComparator.Printer.class);


        public void print(final String type, final String incomingData, final String persistentData) {
            LOG.debug(String.format("%s: in - %s | db - %s", type, incomingData, persistentData));
        }

		public void printMockFound() {
			LOG.debug(Strings.repeat("*", 25));
			LOG.debug("MOCK FOUND!");
			LOG.debug(Strings.repeat("*", 25));
		}

		public void printSkipMock() {
			LOG.debug("Mocks are not NOT equal, skip");
		}

		public void printPaths(final String path1, final String path2) {
            print("PATHS", path1, path2);
        }

		public void printParams(final Set<Pair> params1, final Set<Pair> params2) {
            print("PARAMETERS", params1.toString(), params2.toString());
        }

		public void printHeaders(final Set<Pair> headers1, final Set<Pair> headers2) {
            print("HEADERS", headers1.toString(), headers2.toString());
        }

		public void printBodies(final String body1, final String body2) {
            print("BODIES", body1, body2);
        }

		public void printChecksums(final String checksum1, final String checksum2) {
            print("CHECKSUMS", checksum1, checksum2);
        }
    }
}

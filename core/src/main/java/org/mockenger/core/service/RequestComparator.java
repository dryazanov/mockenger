package org.mockenger.core.service;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.mockenger.commons.utils.JsonHelper;
import org.mockenger.commons.utils.XmlHelper;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.mockenger.data.model.persistent.mock.request.part.Headers;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
import org.mockenger.data.model.persistent.mock.request.part.Parameters;
import org.mockenger.data.model.persistent.mock.request.part.Path;
import org.mockenger.data.model.persistent.transformer.AbstractMapTransformer;
import org.mockenger.data.model.persistent.transformer.Transformer;
import org.xmlunit.XMLUnitException;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.mockenger.core.util.CommonUtils.allNotEmpty;
import static org.mockenger.core.util.CommonUtils.containsAllIgnoreCase;
import static org.mockenger.core.util.CommonUtils.containsEqualEntries;
import static org.mockenger.core.util.CommonUtils.generateCheckSum;
import static org.mockenger.core.util.MockRequestUtils.getBody;
import static org.mockenger.core.util.MockRequestUtils.getBodyValue;
import static org.mockenger.core.util.MockRequestUtils.getHeaderValues;
import static org.mockenger.core.util.MockRequestUtils.getHeaders;
import static org.mockenger.core.util.MockRequestUtils.getParamValues;
import static org.mockenger.core.util.MockRequestUtils.getParameters;
import static org.mockenger.core.util.MockRequestUtils.getPath;
import static org.mockenger.core.util.MockRequestUtils.getPathValue;
import static org.mockenger.core.util.MockRequestUtils.isHttpMethodWithBody;
import static org.mockenger.core.util.MockRequestUtils.isJson;
import static org.mockenger.core.util.MockRequestUtils.isXml;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author Dmitry Ryazanov
 */
@Slf4j
public class RequestComparator {

    private final GenericRequest incoming;

    private AbstractRequest persistent;

	final private Printer printer;


    /**
     * Constructor
     *
     * @param incomingRequest
     */
    public RequestComparator(final GenericRequest incomingRequest) {
        this.incoming = incomingRequest;
		this.printer = new Printer();
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
				printer.printMockFound();

                return true;
            }
        }

        printer.printSkipMock();

        return false;
    }


    /**
     * Compare request paths
	 *
     * @return
     */
    private boolean comparePaths() {
		final Path persistentPath = getPath(persistent);
		final List<Transformer> transformers = persistentPath.getTransformers();
		final String incomingPath = applyPathTransformers(getPathValue(incoming), transformers);

		printer.printPaths(incomingPath, persistentPath.getValue());

        if (incomingPath.equals(persistentPath.getValue())) {
			incoming.getPath().setValue(incomingPath);

			return true;
		}

        return false;
    }


    /**
     * Compare request parameters
	 *
     * @return
     */
    private boolean compareParameters() {
		final Parameters persistParams = getParameters(persistent);
		final Set<Pair> persistentParams = persistParams.getValues();
        Set<Pair> incomingParams = getParamValues(incoming);

        if (allNotEmpty(incomingParams, persistentParams)) {
            incomingParams = applyTransformers(incomingParams, persistParams.getTransformers());
            printer.printParams(incomingParams, persistentParams);

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
		final Headers persistHeaders = getHeaders(persistent);
		final Set<Pair> persistHeaderValues = persistHeaders.getValues();
        final Set<Pair> incomingHeaders = getHeaderValues(incoming);

        if (allNotEmpty(incomingHeaders, persistHeaderValues)) {
            final Set<Pair> transformedHeaders = applyTransformers(incomingHeaders, persistHeaders.getTransformers());

            printer.printHeaders(transformedHeaders, persistHeaderValues);

            return containsAllIgnoreCase(transformedHeaders, persistHeaderValues);
        }

        return true;
    }

    /**
     * Compare request bodies
     *
     * @return
     */
    private boolean compareBodies() {
        if (isHttpMethodWithBody(incoming)) {
            return transformBodyAndCompare();
        }

		// For other methods we only compare checksums
		return compareCheckSums(generateCheckSum(incoming), persistent.getCheckSum());
    }


	private boolean compareCheckSums(final String checksum1, final String checksum2) {
		printer.printChecksums(checksum1, checksum2);

		return checksum1.equals(checksum2);
	}


	private boolean transformBodyAndCompare() {
		final Body persistBody = getBody(persistent);
		final String persistBodyValue = getBodyValue(persistBody);

		if (persistBodyValue.length() > 0) {
			final String incomingBodyValue = applyBodyTransformers(getBodyValue(incoming), persistBody.getTransformers());

			printer.printBodies(incomingBodyValue, persistBodyValue);

			try {
				if (isJson(getHeaders(incoming))) {
					return !JsonHelper.hasDifferences(incomingBodyValue, persistBodyValue);
				} else if (isXml(getHeaders(incoming))) {
					return !XmlHelper.hasDifferences(incomingBodyValue, persistBodyValue);
				} else {
					return compareCheckSums(generateCheckSum(incomingBodyValue), persistent.getCheckSum());
				}
			} catch (XMLUnitException | JSONException e) {
				final String type = (e instanceof XMLUnitException ? "XML" : "JSON");

				log.info("Treat objects as not equal because there is a failure during " + type + " objects creation");
				log.debug("Unable to create " + type + " objects from provided strings", e);

				return false;
			}
		}

		return false;
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


	private String applyPathTransformers(final String path, final List<Transformer> transformers) {
		if (!isEmpty(transformers)) {
			return transformString(path, transformers);
		}

		return path;
	}


	private String applyBodyTransformers(final String body, final List<Transformer> transformers) {
		if (!isEmpty(transformers)) {
			return transformString(body, transformers);
		}

		return body;
	}



	/**
	 * Local printer of the comparison steps
	 */
	private final class Printer {

        public void print(final String type, final String incomingData, final String persistentData) {
            log.info(String.format("%s: in - %s | db - %s", type, incomingData, persistentData));
        }

		public void printMockFound() {
			log.info(Strings.repeat("*", 25));
			log.info("MOCK FOUND!");
			log.info(Strings.repeat("*", 25));
		}

		public void printSkipMock() {
			log.info("Mocks are not NOT equal, skip");
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

package org.mockenger.core.service;

import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static org.mockenger.core.util.MockRequestUtils.containsHeader;
import static org.mockenger.core.util.MockRequestUtils.getHeaders;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class HttpHeadersService {

    public static final String CONTENT_TYPE_KEY = CONTENT_TYPE.toLowerCase();
    public static final String CHARSET_UTF_8 = "charset=UTF-8";
    public static final String MEDIA_TYPE_JSON = APPLICATION_JSON_VALUE + ";" + CHARSET_UTF_8;
    public static final String MEDIA_TYPE_XML = APPLICATION_XML_VALUE + ";" + CHARSET_UTF_8;

    @Value("#{'${mockenger.mock.response.ignore.headers}'.split(',')}")
    private List<String> headersToIgnore;


    /**
     * @return object HttpHeaders with default headers inside
     */
    public HttpHeaders getDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();

        // Default content-type
        headers.set(CONTENT_TYPE, MEDIA_TYPE_JSON);

        return headers;
    }

    /**
     * Creates header list using headersToIgnore as a filter
     *
     * @param headers collection of headers to be added
     * @return object HttpHeaders with added headers
     */
    public HttpHeaders createHeaders(final Collection<Pair> headers) {
        final HttpHeaders defaultHeaders = getDefaultHeaders();

		ofNullable(headers)
				.map(Collection::stream)
				.orElse(Stream.empty())
				.filter(pair -> !headersToIgnore.contains(pair.getKey().toLowerCase()))
				.forEach(pair -> defaultHeaders.set(pair.getKey(), pair.getValue()));

        return defaultHeaders;
    }


	/**
	 *
	 * @param genericRequest
	 * @param headers
	 * @return
	 */
	public HttpHeaders updateContentTypeHeader(final GenericRequest genericRequest, final HttpHeaders headers) {
		if (containsHeader(getHeaders(genericRequest), CONTENT_TYPE, APPLICATION_XML_VALUE)) {
			headers.set(CONTENT_TYPE, MEDIA_TYPE_XML);
		}

		return headers;
	}
}

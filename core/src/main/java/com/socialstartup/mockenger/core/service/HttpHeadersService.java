package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.model.persistent.mock.request.GenericRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitry Ryazanov
 */
@Component
public class HttpHeadersService {

    public static final String CONTENT_TYPE_KEY = HttpHeaders.CONTENT_TYPE.toLowerCase();
    public static final String CHARSET_UTF_8 = "charset=UTF-8";
    public static final String MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE + ";" + CHARSET_UTF_8;
    public static final String MEDIA_TYPE_XML = MediaType.APPLICATION_XML_VALUE + ";" + CHARSET_UTF_8;

    @Value("#{'${mockenger.mock.response.ignore.headers}'.split(',')}")
    private List<String> headersToIgnore;


    /**
     * @return object HttpHeaders with default headers inside
     */
    public HttpHeaders getDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();

        // Default content-type
        headers.set(HttpHeaders.CONTENT_TYPE, MEDIA_TYPE_JSON);
        return headers;
    }

    /**
     * Creates header list using headersToIgnore as a filter
     *
     * @param headerList collection of headers to be added
     * @return object HttpHeaders with added headers
     */
    public HttpHeaders createHeaders(final Collection<Pair> headerList) {
        final HttpHeaders headers = getDefaultHeaders();

        headerList.parallelStream()
				.filter(pair -> !headersToIgnore.contains(pair.getKey().toLowerCase()))
				.forEach(pair -> headers.set(pair.getKey(), pair.getValue()));

        return headers;
    }

	/**
	 *
	 * @param mockRequest
	 * @param headers
	 * @return
	 */
	public HttpHeaders updateContentTypeHeader(final GenericRequest mockRequest, final HttpHeaders headers) {
		final Set<Pair> headerValues = mockRequest.getHeaders().getValues();

		if (!CollectionUtils.isEmpty(headerValues)) {
			headerValues.parallelStream()
					.filter(pair -> pair.getKey().equals(CONTENT_TYPE_KEY))
					.filter(pair -> pair.getValue().contains(MediaType.APPLICATION_XML_VALUE))
					.forEach(pair -> headers.set(CONTENT_TYPE_KEY, MEDIA_TYPE_XML));
		}

		return headers;
	}
}

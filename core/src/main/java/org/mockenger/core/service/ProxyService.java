package org.mockenger.core.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
import org.mockenger.data.model.persistent.mock.response.MockResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.mockenger.core.util.MockRequestUtils.getBodyValue;
import static org.mockenger.core.util.MockRequestUtils.getHeaderValues;
import static org.mockenger.core.util.MockRequestUtils.getParamValues;
import static org.mockenger.core.util.MockRequestUtils.getPathValue;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.util.StringUtils.isEmpty;


/**
 * @author Dmitry Ryazanov
 */
@Slf4j
@Component
public class ProxyService {

	private static final int TIMEOUT = 60;

	@Value("#{'${mockenger.proxy.request.ignore.headers}'.split(',')}")
    private List<String> headersToIgnore;


    /**
     * Proxy request to defined destination and complete mock-request with a real response
     *
     * @param mockRequest Mock-request to forward
     * @param baseHost Host name where request will be forwarded
     * @return
     */
    public MockResponse forwardRequest(final AbstractRequest mockRequest, final String baseHost) {
        final HttpClient client = HttpClientBuilder.create()
				.setDefaultRequestConfig(createRequestConfig())
				.build();

        final HttpUriRequest httpUriRequest = createHttpRequest(mockRequest, baseHost);

        try {
            // SEND REQUEST
            final HttpResponse response = client.execute(httpUriRequest);
            final MockResponse mockResponse;

            if (mockRequest.getMockResponse() == null) {
                mockResponse = new MockResponse();
                mockResponse.setHeaders(new HashSet<>());
                mockRequest.setMockResponse(mockResponse);
            } else {
                mockResponse = mockRequest.getMockResponse();
            }

            // Set response status
			mockResponse.setHttpStatus(
            		ofNullable(response.getStatusLine())
							.map(StatusLine::getStatusCode)
							.orElse(500));

			mockResponse.getHeaders().addAll(
					Stream.of(response.getAllHeaders())
							.map(h -> new Pair(h.getName(), h.getValue()))
							.collect(toList()));

            // Set response body
            if (response.getEntity() != null && response.getEntity().getContent() != null) {
                final String responseBody = IOUtils.toString(response.getEntity().getContent(), UTF_8);
                mockResponse.setBody(responseBody);
            }

            return mockResponse;

        } catch (IOException e) {
            log.error(String.format("Forwarding process failed for request '%s' with path '%s'",
					mockRequest.getName(), getPathValue(mockRequest)), e);
        }

        return new MockResponse();
    }


	/**
     *
     * @return
     */
    private HttpUriRequest createHttpRequest(final AbstractRequest mockRequest, final String baseHost) {
        if (mockRequest == null || baseHost == null) {
            throw new IllegalArgumentException();
        }

		final String uri = new StringBuilder()
				.append(baseHost)
				.append(baseHost.endsWith("/") ? "" : "/")
				.append(getPathValue(mockRequest))
				.toString();

        final RequestBuilder requestBuilder = RequestBuilder.create(mockRequest.getMethod().name());

		requestBuilder.setUri(uri);

		// Set request parameters
		getParamValues(mockRequest)
				.forEach(pair -> requestBuilder.addParameter(pair.getKey(), pair.getValue()));

		final String body = getBodyValue(mockRequest);

        // Set request headers (excl. headers from black-list)
		getHeaderValues(mockRequest)
				.stream()
				.filter(p -> !ignoreHeader(p))
				.filter(p -> !isContentLengthHeader(p) || isEmpty(body))
				.forEach(pair -> requestBuilder.addHeader(pair.getKey(), pair.getValue()));

		// Set request body, if present
		try {
			if (!isEmpty(body)) {
				final StringEntity stringEntity = new StringEntity(body);
				final String contentType = ofNullable(requestBuilder.getFirstHeader(CONTENT_TYPE.toLowerCase()))
						.map(Header::getValue)
						.orElse("");

				if (!isEmpty(contentType)) {
					stringEntity.setContentType(contentType);
				}

				requestBuilder.setEntity(stringEntity);
			}
		} catch (UnsupportedEncodingException e) {
			log.error("An error occurred during the request's body preparation", e);
		}

        return requestBuilder.build();
    }


	private boolean ignoreHeader(final Pair p) {
		return headersToIgnore.contains(p.getKey().toLowerCase());
	}


	private RequestConfig createRequestConfig() {
		return RequestConfig.custom()
				.setConnectTimeout(TIMEOUT * 1000)
				.setConnectionRequestTimeout(TIMEOUT * 1000)
				.setSocketTimeout(TIMEOUT * 1000).build();
	}


    private boolean isContentLengthHeader(final Pair pair) {
		return CONTENT_LENGTH.equalsIgnoreCase(pair.getKey());
	}
}

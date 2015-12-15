package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Dmitry Ryazanov
 */
@Component
public class ProxyService {

    private final Logger LOG = LoggerFactory.getLogger(ProxyService.class);

    public final int DEFAULT_RESPONSE_HTTP_STATUS = 500;

    @Value("#{'${mockenger.proxy.request.ignore.headers}'.split(',')}")
    private List<String> headersToIgnore;


    /**
     * Proxy request to defined destination and complete mock-request with a real response
     *
     * @param mockRequest Mock-request to forward
     * @param baseHost Host name where request will be forwarded
     * @return
     */
    public AbstractRequest forwardRequest(AbstractRequest mockRequest, String baseHost) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpUriRequest httpUriRequest = createHttpRequest(mockRequest, baseHost);

        try {
            // SEND REQUEST
            HttpResponse response = client.execute(httpUriRequest);

            MockResponse mockResponse;
            if (mockRequest.getMockResponse() == null) {
                mockResponse = new MockResponse();
                mockResponse.setHeaders(new HashSet<>());
                mockRequest.setMockResponse(mockResponse);
            } else {
                mockResponse = mockRequest.getMockResponse();
            }

            // Set response status
            mockResponse.setHttpStatus(response.getStatusLine() != null ? response.getStatusLine().getStatusCode() : DEFAULT_RESPONSE_HTTP_STATUS);

            // Set response headers
            Stream.of(response.getAllHeaders()).forEach(header -> mockResponse.getHeaders().add(new Pair(header.getName(), header.getValue())));

            // Set response body
            if (response.getEntity() != null && response.getEntity().getContent() != null) {
                String responseBody = IOUtils.toString(response.getEntity().getContent());
                mockResponse.setBody(responseBody);
            }
        } catch (IOException e) {
            LOG.error(String.format("Forwarding process failed for request '%s' with path '%s'", mockRequest.getName(), mockRequest.getPath().getValue()), e);
        }

        return mockRequest;
    }

    /**
     *
     * @return
     */
    private HttpUriRequest createHttpRequest(AbstractRequest mockRequest, String baseHost) {
        if (mockRequest == null || baseHost == null) {
            throw new IllegalArgumentException();
        }

        StringBuilder sb = new StringBuilder(baseHost);
        if (mockRequest.getPath() != null && mockRequest.getPath().getValue() != null) {
            sb.append(mockRequest.getPath().getValue());
        }

        RequestBuilder requestBuilder = RequestBuilder.create(mockRequest.getMethod().name());
        requestBuilder.setUri(sb.toString());

        // Set request parameters
        if (mockRequest.getParameters() != null && mockRequest.getParameters().getValues() != null) {
            mockRequest.getParameters().getValues().forEach( pair -> requestBuilder.addParameter(pair.getKey(), pair.getValue()) );
        }

        // Set request headers (excl. headers from black-list)
        if (mockRequest.getHeaders() != null && mockRequest.getHeaders().getValues() != null) {
            mockRequest.getHeaders().getValues().forEach(pair -> {
                if (!headersToIgnore.contains(pair.getKey())) {
                    requestBuilder.addHeader(pair.getKey(), pair.getValue());
                }
            });
        }

        return requestBuilder.build();
    }
}

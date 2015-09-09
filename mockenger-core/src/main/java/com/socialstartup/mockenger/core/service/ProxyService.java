package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import com.socialstartup.mockenger.data.model.persistent.mock.response.MockResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Dmitry Ryazanov
 */
public class ProxyService {

    Logger LOG = LoggerFactory.getLogger(ProxyService.class);

    public final int DEFAULT_RESPONSE_HTTP_STATUS = 500;

    public final List<String> HEADERS_TO_IGNORE = new ArrayList<>(Arrays.asList("host"));

    // Mock-request to forward
    private AbstractRequest mockRequest;

    // Host name where request will be forwarded
    private String forwardTo;


    /**
     * Constructor
     *
     * @param mockRequest
     * @param forwardTo
     */
    public ProxyService(AbstractRequest mockRequest, String forwardTo) {
        this.mockRequest = mockRequest;
        this.forwardTo = forwardTo;
    }

    /**
     *
     * @return
     */
    public AbstractRequest forwardRequest() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpUriRequest httpUriRequest = createHttpRequest();

        try {
            // SEND REQUEST
            HttpResponse response = client.execute(httpUriRequest);

            if (mockRequest.getMockResponse() == null) {
                MockResponse mockResponse = new MockResponse();
                mockResponse.setHeaders(new HashSet<>());
                mockRequest.setMockResponse(mockResponse);
            }

            mockRequest.getMockResponse().setHttpStatus(response.getStatusLine() != null ? response.getStatusLine().getStatusCode() : DEFAULT_RESPONSE_HTTP_STATUS);

            for (Header header : response.getAllHeaders()) {
                mockRequest.getMockResponse().getHeaders().add(new Pair(header.getName(), header.getValue()));
            }

            if (response.getEntity() != null && response.getEntity().getContent() != null) {
                String responseBody = IOUtils.toString(response.getEntity().getContent());
                mockRequest.getMockResponse().setBody(responseBody);
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
    private HttpUriRequest createHttpRequest() {
        if (mockRequest == null || forwardTo == null) {
            throw new IllegalArgumentException();
        }

        StringBuilder sb = new StringBuilder(forwardTo);

        if (mockRequest.getPath() != null && mockRequest.getPath().getValue() != null) {
            sb.append(mockRequest.getPath().getValue());
        }

        RequestBuilder requestBuilder = RequestBuilder.create(mockRequest.getMethod().name());
        requestBuilder.setUri(sb.toString());

        if (mockRequest.getParameters() != null && mockRequest.getParameters().getValues() != null) {
            for (Pair pair : mockRequest.getParameters().getValues()) {
                requestBuilder.addParameter(pair.getKey(), pair.getValue());
            }
        }

        if (mockRequest.getHeaders() != null && mockRequest.getHeaders().getValues() != null) {
            for (Pair pair : mockRequest.getHeaders().getValues()) {
                if (HEADERS_TO_IGNORE.contains(pair.getKey())) {
                    continue;
                }
                requestBuilder.addHeader(pair.getKey(), pair.getValue());
            }
        }

        return requestBuilder.build();
    }
}

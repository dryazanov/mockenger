package com.socialstartup.mockenger.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dmitry Ryazanov
 */
@Component
public class MockengerHeadersStopListConfigParam {

    @Value("${mockenger.proxy.request.ignore.headers}")
    private String requestValues;

    @Value("${mockenger.mock.response.ignore.headers}")
    private String responseValues;

    private List<String> requestHeaders;

    private List<String> responseHeaders;


    public void setResponseValues(String responseValues) {
        this.responseValues = responseValues;
    }

    public void setRequestValues(String requestValues) {
        this.requestValues = requestValues;
    }

    public List<String> getRequestHeaders() {
        if (requestHeaders == null) {
            requestHeaders = new ArrayList<>(Arrays.asList(requestValues.split(",")));
        }

        return requestHeaders;
    }

    public List<String> getResponseHeaders() {
        if (responseHeaders == null) {
            responseHeaders = new ArrayList<>(Arrays.asList(responseValues.split(",")));
        }

        return responseHeaders;
    }
}

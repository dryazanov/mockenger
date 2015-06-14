package com.socialstartup.mockenger.model.mock.response;

import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Created by x079089 on 3/12/2015.
 */
public class MockResponse implements IMockResponse {

    protected HttpStatus httpStatus;

    private Map<String, String> responseHeaders;

    protected String responseBody;


    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    @Override
    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}

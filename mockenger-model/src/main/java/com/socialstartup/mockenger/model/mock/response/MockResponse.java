package com.socialstartup.mockenger.model.mock.response;

import java.util.Map;

/**
 * Created by x079089 on 3/12/2015.
 */
public class MockResponse implements IMockResponse {

    protected int httpStatus;

    private Map<String, String> responseHeaders;

    protected String responseBody;


    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public void setHttpStatus(int httpStatus) {
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

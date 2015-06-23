package com.socialstartup.mockenger.data.model.persistent.mock.response;

import java.util.Map;

/**
 * Created by x079089 on 3/12/2015.
 */
public class MockResponse implements IMockResponse {

    private int httpStatus;

    private Map<String, String> headers;

    private String body;


    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }
}
